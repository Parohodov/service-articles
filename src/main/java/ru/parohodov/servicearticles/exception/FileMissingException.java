package ru.parohodov.servicearticles.exception;

/**
 * @author Parohodov
 */
public class FileMissingException extends RuntimeException {
    public FileMissingException(String message) {
        super(message);
    }
}
