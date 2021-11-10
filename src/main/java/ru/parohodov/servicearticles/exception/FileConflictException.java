package ru.parohodov.servicearticles.exception;

/**
 * @author Pavel Popov
 */
public class FileConflictException extends RuntimeException {
    public FileConflictException(String message) {
        super(message);
    }
}
