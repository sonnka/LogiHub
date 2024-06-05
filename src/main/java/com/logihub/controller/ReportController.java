package com.logihub.controller;

import com.itextpdf.text.DocumentException;
import com.logihub.exception.InvoiceException;
import com.logihub.exception.UserException;
import com.logihub.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@AllArgsConstructor
@CrossOrigin("http://localhost:4200")
public class ReportController {

    private FileService fileService;

    @GetMapping("/api/truck-managers/{user-id}/invoices/{invoice-id}/export")
    public void exportInvoiceByTruckManager(Authentication auth,
                                            HttpServletResponse response,
                                            @PathVariable("user-id") Long userId,
                                            @PathVariable("invoice-id") Long invoiceId)
            throws DocumentException, IOException, UserException, InvoiceException {
        fileService.generateInvoice(auth.getName(), userId, invoiceId, response);
    }

    @GetMapping("/api/parking-managers/{user-id}/invoices/{invoice-id}/export")
    public void exportInvoiceByParkingManager(Authentication auth,
                                              HttpServletResponse response,
                                              @PathVariable("user-id") Long userId,
                                              @PathVariable("invoice-id") Long invoiceId)
            throws DocumentException, IOException, UserException, InvoiceException {
        fileService.generateInvoice(auth.getName(), userId, invoiceId, response);
    }

    @GetMapping("/api/admins/{admin-id}/db/week-report")
    public void exportWeekDatabaseReport(Authentication auth,
                                         HttpServletResponse response,
                                         @PathVariable("admin-id") Long adminId)
            throws DocumentException, IOException, UserException {
        fileService.generateWeekDatabaseReport(auth.getName(), adminId, response);
    }

    @GetMapping("/api/admins/{admin-id}/db/month-report")
    public void exportMonthDatabaseReport(Authentication auth,
                                          HttpServletResponse response,
                                          @PathVariable("admin-id") Long adminId)
            throws DocumentException, IOException, UserException {
        fileService.generateMonthDatabaseReport(auth.getName(), adminId, response);
    }
}