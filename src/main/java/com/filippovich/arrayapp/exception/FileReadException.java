package com.filippovich.arrayapp.exception;

public class FileReadException extends Exception {
    public FileReadException(String message) {
        super(message);
    }

    public FileReadException(String message, Throwable cause) {
        super(message, cause);
    }
}
