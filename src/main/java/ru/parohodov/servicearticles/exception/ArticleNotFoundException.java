package ru.parohodov.servicearticles.exception;

/**
 * @author Parohodov
 */
public class ArticleNotFoundException extends RuntimeException {
    public ArticleNotFoundException(String message) {
        super(message);
    }
}
