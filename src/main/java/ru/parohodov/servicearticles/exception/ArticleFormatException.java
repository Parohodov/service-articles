package ru.parohodov.servicearticles.exception;

/**
 * @author Parohodov
 */
public class ArticleFormatException extends RuntimeException {
    public ArticleFormatException(String message) {
        super(message);
    }
}
