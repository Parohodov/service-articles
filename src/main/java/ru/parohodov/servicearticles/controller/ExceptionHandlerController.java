package ru.parohodov.servicearticles.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import ru.parohodov.servicearticles.exception.*;

/**
 * @author Parohodov
 */
//@ControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDto handleRuntimeException(RuntimeException e) {
        return new ErrorDto(e.getMessage());
    }

    @ExceptionHandler(OpenFileFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ErrorDto handleOpenFileFailedException(OpenFileFailedException e) {
        return new ErrorDto(e.getMessage());
    }

    @ExceptionHandler(ArticleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorDto handleArticleNotFoundException(ArticleNotFoundException e) {
        return new ErrorDto(e.getMessage());
    }

    @ExceptionHandler(ArticleAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ErrorDto handleArticleAlreadyExistsException(ArticleAlreadyExistsException e, ModelAndView modelAndView) {
        return new ErrorDto(e.getMessage());
    }

    @ExceptionHandler(ArticleFormatException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ResponseBody
    public ErrorDto handleArticleFormatException(ArticleFormatException e, ModelAndView modelAndView) {
        return new ErrorDto(e.getMessage());
    }

    @ExceptionHandler(FileFormatException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ResponseBody
    public ErrorDto handleFileFormatException(FileFormatException e, ModelAndView modelAndView) {
        return new ErrorDto(e.getMessage());
    }

    @Data
    @AllArgsConstructor
    public class ErrorDto {
        private String message;
    }
}
