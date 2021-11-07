package ru.parohodov.servicearticles.exception;

/**
 * @author Parohodov
 */
public class ArticleAlreadyExistsException extends RuntimeException {
    public ArticleAlreadyExistsException(String message) {
        super(message);
    }
}
