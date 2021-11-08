package ru.parohodov.servicearticles.exception;

import java.io.IOException;

/**
 * @author Parohodov
 */
public class OpenFileFailedException extends IOException {
    public OpenFileFailedException(String message) {
        super(message);
    }
}
