package com.anna.lukasiewicz.interview_task.exception;

public class NoCarsAvailableException extends RuntimeException {
    public NoCarsAvailableException(String message) {
        super(message);
    }
}
