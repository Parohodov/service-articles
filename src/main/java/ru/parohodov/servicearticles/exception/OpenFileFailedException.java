package ru.parohodov.servicearticles.exception;

/**
 * @author Parohodov
 */
public class OpenFileFailedException extends RuntimeException {
    public OpenFileFailedException(String message) {
        super(message);
    }
}
