package com.ocr.mediscreen.exceptions;

public class PatientNoCreateException extends RuntimeException {
    public PatientNoCreateException(String message) {
        super(message);
    }

    @Override
    public Throwable fillInStackTrace() {
        return null;
    }
}

