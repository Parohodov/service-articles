package ru.parohodov.servicearticles.exception;

/**
 * @author Parohodov
 */
public class FileFormatException extends RuntimeException {
    public FileFormatException(String message) {
        super(message);
    }
}