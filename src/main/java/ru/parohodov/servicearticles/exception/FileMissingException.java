package ru.parohodov.servicearticles.exception;

/**
 * @author Pavel Popov
 */
public class FileMissingException extends RuntimeException {
    public FileMissingException(String message) {
        super(message);
    }
}