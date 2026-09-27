package com.hdfclife.ledger.exception;

public class DeskException extends RuntimeException {
    public DeskException(String message) {
        super(message);
    }

    public DeskException(String message, Throwable cause) {
        super(message, cause);
    }
}
