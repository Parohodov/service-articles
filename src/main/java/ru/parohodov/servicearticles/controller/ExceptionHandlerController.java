package ru.parohodov.servicearticles.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.parohodov.servicearticles.exception.*;

/**
 * @author Parohodov
 */
@ControllerAdvice
public class ExceptionHandlerController {
    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleRuntimeException(RuntimeException e, ModelAndView modelAndView) {
        modelAndView.setViewName("/error");
        setErrorMessage(modelAndView, e.getMessage());
        modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return modelAndView;
    }

    @ExceptionHandler(OpenFileFailedException.class)
    public ModelAndView handleOpenFileFailedException(OpenFileFailedException e, ModelAndView modelAndView) {
        modelAndView.setViewName("/error");
        setErrorMessage(modelAndView, e.getMessage());
        modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        return modelAndView;
    }

//    @ExceptionHandler(ArticleNotFoundException.class)
//    public ModelAndView handleArticleNotFoundException(ArticleNotFoundException e, ModelAndView modelAndView) {
//        modelAndView.setViewName("/error");
//        setErrorMessage(modelAndView, e.getMessage());
//        modelAndView.setStatus(HttpStatus.NOT_FOUND);
//        return modelAndView;
//    }

    @ExceptionHandler(ArticleNotFoundException.class)
    public String handleArticleNotFoundException(ArticleNotFoundException e) {
        return e.getMessage();
    }

    @ExceptionHandler(ArticleAlreadyExistsException.class)
    public ModelAndView handleArticleAlreadyExistsException(ArticleAlreadyExistsException e, ModelAndView modelAndView) {
        modelAndView.setViewName("/error");
        setErrorMessage(modelAndView, e.getMessage());
        modelAndView.setStatus(HttpStatus.CONFLICT);
        return modelAndView;
    }

    @ExceptionHandler(ArticleFormatException.class)
    public ModelAndView handleArticleFormatException(ArticleFormatException e, ModelAndView modelAndView) {
        modelAndView.setViewName("/error");
        setErrorMessage(modelAndView, e.getMessage());
        modelAndView.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        return modelAndView;
    }

    @ExceptionHandler(FileFormatException.class)
    public ModelAndView handleFileFormatException(FileFormatException e, ModelAndView modelAndView) {
        modelAndView.setViewName("/error");
        setErrorMessage(modelAndView, e.getMessage());
        modelAndView.setStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        return modelAndView;
    }

    private void setErrorMessage(ModelAndView modelAndView, String message) {
        ErrorDto errorDto = new ErrorDto(message);
        modelAndView.addObject("errormessage", errorDto);

    }

    @Data
    @AllArgsConstructor
    public class ErrorDto {

        private String message;
    }
}
