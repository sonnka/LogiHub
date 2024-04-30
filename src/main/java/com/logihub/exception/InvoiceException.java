package com.logihub.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

public class InvoiceException extends Exception {

    private final InvoiceExceptionProfile invoiceExceptionProfile;

    public InvoiceException(InvoiceExceptionProfile invoiceExceptionProfile) {
        super(invoiceExceptionProfile.exceptionMessage);
        this.invoiceExceptionProfile = invoiceExceptionProfile;
    }

    public String getName() {
        return invoiceExceptionProfile.exceptionName;
    }

    public HttpStatus getResponseStatus() {
        return invoiceExceptionProfile.responseStatus;
    }

    @AllArgsConstructor
    public enum InvoiceExceptionProfile {

        INVOICE_NOT_FOUND("invoice_not_found",
                "Invoice with such id is not found.", HttpStatus.BAD_REQUEST);

        private final String exceptionName;
        private final String exceptionMessage;
        private final HttpStatus responseStatus;
    }
}