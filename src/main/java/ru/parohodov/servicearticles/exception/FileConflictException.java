package ru.parohodov.servicearticles.exception;

/**
 * @author Parohodov
 */
public class FileConflictException extends RuntimeException {
    public FileConflictException(String message) {
        super(message);
    }
}
