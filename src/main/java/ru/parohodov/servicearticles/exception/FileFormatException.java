package ru.parohodov.servicearticles.exception;

/**
 * @author Pavel Popov
 */
public class FileFormatException extends RuntimeException {
    public FileFormatException(String message) {
        super(message);
    }
}