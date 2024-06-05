package com.logihub.service;

import com.itextpdf.text.DocumentException;
import com.logihub.exception.InvoiceException;
import com.logihub.exception.UserException;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;

public interface FileService {

    void generateWeekDatabaseReport(String email, Long adminId, HttpServletResponse response)
            throws IOException, DocumentException, UserException;

    void generateMonthDatabaseReport(String email, Long adminId, HttpServletResponse response)
            throws IOException, DocumentException, UserException;

    void generateInvoice(String email, Long userId, Long invoiceId, HttpServletResponse response)
            throws IOException, DocumentException, UserException, InvoiceException;

    void generateDatabaseZipDump(HttpServletResponse response)
            throws SQLException, IOException, ClassNotFoundException;

    void importDatabase(String sql) throws SQLException, ClassNotFoundException;
}
