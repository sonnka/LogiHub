package com.logihub.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

public class AuthException extends Exception {

    private final AuthExceptionProfile authExceptionProfile;

    public AuthException(AuthExceptionProfile authExceptionProfile) {
        super(authExceptionProfile.exceptionMessage);
        this.authExceptionProfile = authExceptionProfile;
    }

    public String getName() {
        return authExceptionProfile.exceptionName;
    }

    public HttpStatus getResponseStatus() {
        return authExceptionProfile.responseStatus;
    }

    @AllArgsConstructor
    public enum AuthExceptionProfile {

        EMAIL_OCCUPIED("email_occupied",
                "User with such email already exists.", HttpStatus.UNAUTHORIZED),

        REGISTRATION_FAILED("registration_failed",
                "Registration is failed. Try again later.", HttpStatus.UNAUTHORIZED),

        WRONG_AUTHENTICATION_DATA("wrong_authentication_data",
                "Wrong authentication data.", HttpStatus.UNAUTHORIZED),

        DELETING_FAILED("deleting_failed",
                "Something went wrong during deleting.", HttpStatus.BAD_REQUEST);


        private final String exceptionName;
        private final String exceptionMessage;
        private final HttpStatus responseStatus;
    }
}
