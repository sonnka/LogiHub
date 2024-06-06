package com.logihub.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.logihub.exception.InvoiceException;
import com.logihub.exception.UserException;
import com.logihub.model.entity.User;
import com.logihub.model.enums.Role;
import com.logihub.model.response.DatabaseHistoryDTO;
import com.logihub.model.response.InvoiceDTO;
import com.logihub.model.response.ItemDTO;
import com.logihub.repository.AdminRepository;
import com.logihub.service.DatabaseHistoryService;
import com.logihub.service.FileService;
import com.logihub.service.InvoiceService;
import com.logihub.util.AuthUtil;
import com.smattme.MysqlExportService;
import com.smattme.MysqlImportService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FileServiceImpl implements FileService {

    private final Font font12 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 12, BaseColor.BLACK);
    private final Font font14 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, BaseColor.BLACK);
    private final Font boldFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, Font.FontStyle.BOLD.ordinal(),
            BaseColor.BLACK);
    private final LocalDateTime today = LocalDate.now().atTime(0, 0, 0);
    private final LocalDateTime startOfWeek = today.minusDays(today.getDayOfWeek().getValue() - 1L);
    private final LocalDateTime startOfMonth = today.minusDays(today.getDayOfMonth() - 1L);

    private final AdminRepository adminRepository;
    private final InvoiceService invoiceService;
    private final AuthUtil authUtil;
    private final DatabaseHistoryService databaseHistoryService;

    @Value("${database.name}")
    private String dbName;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Value("${spring.datasource.password}")
    private String dbPassword;

    @Value("${spring.datasource.url}")
    private String dbUrl;


    public FileServiceImpl(AdminRepository adminRepository, InvoiceService invoiceService, AuthUtil authUtil,
                           DatabaseHistoryService databaseHistoryService) {
        this.adminRepository = adminRepository;
        this.invoiceService = invoiceService;
        this.authUtil = authUtil;
        this.databaseHistoryService = databaseHistoryService;
    }


    @Override
    public void generateWeekDatabaseReport(String email, Long adminId, HttpServletResponse response)
            throws IOException, DocumentException, UserException {
        configureResponse(response, "_week_database_report");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        configureDocument(document, email, adminId, "Week database report", startOfWeek);

        List<DatabaseHistoryDTO> history = databaseHistoryService.getWeekDatabaseHistory();

        fillDatabaseData(document, history);

        document.close();
    }

    @Override
    public void generateMonthDatabaseReport(String email, Long adminId, HttpServletResponse response)
            throws IOException, DocumentException, UserException {
        configureResponse(response, "_month_database_report");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        configureDocument(document, email, adminId, "Month database report", startOfMonth);

        List<DatabaseHistoryDTO> history = databaseHistoryService.getMonthDatabaseHistory();

        fillDatabaseData(document, history);

        document.close();
    }

    @Override
    public void generateInvoice(String email, Long userId, Long invoiceId, HttpServletResponse response)
            throws IOException, DocumentException, UserException, InvoiceException {
        configureResponse(response, "_invoice");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        var invoice = configureInvoiceDocument(document, email, userId, invoiceId);

        fillInvoiceData(document, invoice);

        document.close();
    }

    @Override
    public void generateDatabaseZipDump(HttpServletResponse response) throws SQLException, IOException,
            ClassNotFoundException {
        MysqlExportService mysqlExportService = getMysqlExportService();

        File file = mysqlExportService.getGeneratedZipFile();

        response.setContentType("application/zip");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + file.getName();
        response.setHeader(headerKey, headerValue);

        ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());
        zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
        FileInputStream fileInputStream = new FileInputStream(file);

        IOUtils.copy(fileInputStream, zipOutputStream);

        fileInputStream.close();
        zipOutputStream.closeEntry();

        zipOutputStream.close();

        mysqlExportService.clearTempFiles();
    }

    @Override
    @Transactional
    public void importDatabase(String sql) throws SQLException, ClassNotFoundException {
        MysqlImportService.builder()
                .setDatabase(dbName)
                .setSqlString(sql)
                .setUsername(dbUsername)
                .setPassword(dbPassword)
                .setDeleteExisting(true)
                .setDropExisting(false)
                .setJdbcConnString(dbUrl)
                .importDatabase();
    }

    private MysqlExportService getMysqlExportService() throws IOException, SQLException, ClassNotFoundException {
        Properties properties = new Properties();
        properties.setProperty(MysqlExportService.DB_NAME, dbName);
        properties.setProperty(MysqlExportService.DB_USERNAME, dbUsername);
        properties.setProperty(MysqlExportService.DB_PASSWORD, dbPassword);
        properties.setProperty(MysqlExportService.TEMP_DIR, new File("external").getPath());
        properties.setProperty(MysqlExportService.JDBC_CONNECTION_STRING, dbUrl);
        properties.setProperty(MysqlExportService.PRESERVE_GENERATED_ZIP, "true");
        properties.setProperty(MysqlExportService.ADD_IF_NOT_EXISTS, "true");

        MysqlExportService mysqlExportService = new MysqlExportService(properties);

        mysqlExportService.export();
        return mysqlExportService;
    }

    private void configureDocument(Document document, String email, Long adminId, String reportType,
                                   LocalDateTime startOfPeriod) throws UserException, DocumentException {
        var reportName = reportType + "\n" + startOfPeriod.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) +
                " - " + today.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        var admin = adminRepository.findByEmailIgnoreCase(email).orElseThrow(
                () -> new UserException(UserException.UserExceptionProfile.ADMIN_NOT_FOUND)
        );

        if (!Objects.equals(admin.getId(), adminId)) {
            throw new UserException(UserException.UserExceptionProfile.PERMISSION_DENIED);
        }

        setTitleOfDocument(document, reportName, admin);
    }

    private InvoiceDTO configureInvoiceDocument(Document document, String email, Long userId, Long invoiceId)
            throws UserException, DocumentException, InvoiceException {
        var user = authUtil.findUserByEmailAndId(email, userId);
        InvoiceDTO invoice;
        if (user.getRole().equals(Role.TRUCK_MANAGER)) {
            invoice = invoiceService.getInvoiceByTruckManager(email, userId, invoiceId);
        } else if (user.getRole().equals(Role.PARKING_MANAGER)) {
            invoice = invoiceService.getInvoiceByParkingManager(email, userId, invoiceId);
        } else {
            throw new UserException(UserException.UserExceptionProfile.SOMETHING_WRONG);
        }

        var reportName = invoice.getType().name().replace("_", " ") + "\n" +
                invoice.getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));

        setTitleOfDocument(document, reportName, user);
        return invoice;
    }

    private void configureResponse(HttpServletResponse response, String reportType) {
        response.setContentType("application/pdf");
        var fileName = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy_HH.mm.ss")) + reportType + ".pdf";
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=" + fileName;
        response.setHeader(headerKey, headerValue);
    }

    private void setTitleOfDocument(Document document, String reportName, User user)
            throws DocumentException {
        var todayDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
        Paragraph time = new Paragraph(todayDate, font12);

        Paragraph adminText = new Paragraph("Author:", font12);
        Paragraph adminFullName = new Paragraph(user.getFirstName() + " " + user.getLastName(), font12);
        Paragraph adminEmail = new Paragraph(user.getEmail(), font12);

        Paragraph title = new Paragraph(reportName, boldFont);

        title.setAlignment(Element.ALIGN_CENTER);
        time.setAlignment(Element.ALIGN_RIGHT);
        adminFullName.setIndentationLeft(30f);
        adminEmail.setIndentationLeft(30f);

        document.add(time);
        document.add(adminText);
        document.add(adminFullName);
        document.add(adminEmail);
        document.add(new Paragraph("\n"));
        document.add(title);
        document.add(new Paragraph("\n\n"));
    }

    private void fillDatabaseData(Document document, List<DatabaseHistoryDTO> history) throws DocumentException {
        PdfPTable table = new PdfPTable(4);

        Stream.of("Date", "Time", "Admin email", "Operation")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(new BaseColor(153, 192, 192));
                    header.setBorderWidth(0.5f);
                    header.setPhrase(new Phrase(columnTitle, font14));
                    var headerCell = table.addCell(header);
                    headerCell.setHorizontalAlignment(1);
                    headerCell.setVerticalAlignment(1);
                    headerCell.setFixedHeight(35f);
                });

        for (DatabaseHistoryDTO historyRecord : history) {
            var dateCell = table.addCell(new PdfPCell(new Phrase(historyRecord.getTime()
                    .format(DateTimeFormatter.ofPattern("dd.MM.yy")), font12)));

            var timeCell = table.addCell(new PdfPCell(new Phrase(historyRecord.getTime()
                    .format(DateTimeFormatter.ofPattern("HH:mm:ss")), font12)));

            var emailCell = table.addCell(new PdfPCell(new Phrase(historyRecord.getAdminEmail(), font12)));

            var operationCell = table.addCell(new PdfPCell(new Phrase(
                    historyRecord.getOperation().toString().toLowerCase(),
                    font12)));

            for (PdfPCell cell : List.of(dateCell, timeCell, emailCell, operationCell)) {
                cell.setHorizontalAlignment(1);
                cell.setVerticalAlignment(1);
                cell.setMinimumHeight(35f);
            }
        }
        table.setWidthPercentage(100f);
        document.add(table);
    }


    private void fillInvoiceData(Document document, InvoiceDTO invoice) throws DocumentException {

        Paragraph line1 = new Paragraph("Creation date: " + invoice.getCreationDate()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH.mm.ss")), font12);
        Paragraph line2 = new Paragraph("Truck number: " + invoice.getTruckNumber(), font12);
        Paragraph line3 = new Paragraph("Truck manager email: " + invoice.getTruckManagerEmail(), font12);
        Paragraph line4 = new Paragraph("Place number: " + invoice.getPlaceNumber(), font12);
        Paragraph line5 = new Paragraph("Parking manager email: " + invoice.getParkingManagerEmail(), font12);
        Paragraph line6 = new Paragraph("Description: " + invoice.getDescription(), font12);
        Paragraph line7 = new Paragraph("Price: " + String.format("%.2f", invoice.getPrice()), font12);


        document.add(line1);
        document.add(new Paragraph("\n"));
        document.add(line2);
        document.add(line3);
        document.add(new Paragraph("\n"));
        document.add(line4);
        document.add(line5);
        document.add(new Paragraph("\n"));
        document.add(line6);
        document.add(line7);

        if (!invoice.getItems().isEmpty()) {
            document.add(new Paragraph("\n"));
            Paragraph line8 = new Paragraph("Items", font12);
            document.add(line8);
            document.add(new Paragraph("\n"));


            PdfPTable table = new PdfPTable(6);

            Stream.of("Name", "Weight", "Price", "Amount", "Total weight", "Total price")
                    .forEach(columnTitle -> {
                        PdfPCell header = new PdfPCell();
                        header.setBackgroundColor(new BaseColor(153, 192, 192));
                        header.setBorderWidth(0.5f);
                        header.setPhrase(new Phrase(columnTitle, font14));
                        var headerCell = table.addCell(header);
                        headerCell.setHorizontalAlignment(1);
                        headerCell.setVerticalAlignment(1);
                        headerCell.setFixedHeight(35f);
                    });

            for (ItemDTO item : invoice.getItems()) {
                var nameCell = table.addCell(new PdfPCell(new Phrase(item.getName(), font12)));

                var weightCell = table.addCell(new PdfPCell(new Phrase(
                        String.format("%.2f", item.getWeight()), font12)));

                var priceCell = table.addCell(new PdfPCell(new Phrase(
                        String.format("%.2f", item.getPrice()), font12)));

                var amountCell = table.addCell(new PdfPCell(new Phrase(item.getAmount().toString(), font12)));

                var totalWeightCell = table.addCell(new PdfPCell(new Phrase(
                        String.format("%.2f", item.getTotalWeight()), font12)));

                var totalPriceCell = table.addCell(new PdfPCell(new Phrase(
                        String.format("%.2f", item.getTotalPrice()), font12)));

                for (PdfPCell cell : List.of(nameCell, weightCell, priceCell, amountCell,
                        totalWeightCell, totalPriceCell)) {
                    cell.setHorizontalAlignment(1);
                    cell.setVerticalAlignment(1);
                    cell.setMinimumHeight(35f);
                }
            }
            table.setWidthPercentage(100f);
            document.add(table);
        }
        document.add(new Paragraph("\n\n"));
        Paragraph line9 = new Paragraph("Signed by parking manager: "
                + invoice.getSignedByParkingManagerDate()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")), font12);
        Paragraph line10 = new Paragraph("Signed by truck manager: "
                + invoice.getSignedByTruckManagerDate()
                .format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")), font12);
        document.add(line9);
        document.add(line10);
    }
}
