package ru.parohodov.servicearticles.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ru.parohodov.servicearticles.exception.FileConflictException;
import ru.parohodov.servicearticles.exception.FileMissingException;
import ru.parohodov.servicearticles.exception.FileFormatException;
import ru.parohodov.servicearticles.exception.FileCommonException;
import ru.parohodov.servicearticles.service.ArticleService;

/**
 * @author Parohodov
 *
 * DONE: Controller
 * DONE: Crud repository
 * DONE: Uploading, readind, deleting files
 * TODO: PUT (?) - Updating - get upload file date and store it if it's different with original one
 * DONE: Exception handling
 * TODO: No Thymeleaf UI
 * TODO: Normal UI
 * TODO: Deploying
 * TODO: tests
 * TODO: Paging
 * TODO: Article subject
 * TODO: Filters for DB
 * TODO: Rest
 * TODO: Logging
 * TODO: Entity To DTO Conversion
 * TODO: Authentication
 */
@RequiredArgsConstructor
//@RestController
@Controller
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping( {"", "/"})
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView fetchALl(ModelAndView modelAndView) {
        try {
            modelAndView.addObject("articles", articleService.getAllArticles());
        } catch (FileMissingException e) {
            return makeErrorModelAndView(modelAndView, HttpStatus.NOT_FOUND, e.getMessage());
        } catch (FileFormatException e) {
            return makeErrorModelAndView(modelAndView, HttpStatus.UNSUPPORTED_MEDIA_TYPE, e.getMessage());
        } catch (FileCommonException e) {
            return makeErrorModelAndView(modelAndView, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        modelAndView.setStatus(HttpStatus.OK);
        modelAndView.setViewName("/articles");
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView fetchById(@PathVariable("id") long id, ModelAndView modelAndView) {
        try {
            modelAndView.addObject("article", articleService.getArticleById(id));
        } catch (FileMissingException e) {
            return makeErrorModelAndView(modelAndView, HttpStatus.NOT_FOUND, e.getMessage());
        } catch (FileFormatException e) {
            return makeErrorModelAndView(modelAndView, HttpStatus.UNSUPPORTED_MEDIA_TYPE, e.getMessage());
        } catch (FileCommonException e) {
            return makeErrorModelAndView(modelAndView, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        modelAndView.setStatus(HttpStatus.OK);
        modelAndView.setViewName("/article");
        return modelAndView;
    }

    @PostMapping( {"", "/"})
    public ModelAndView create(@RequestParam("file") MultipartFile fileName, ModelAndView modelAndView) {
        try {
            articleService.saveArticle(fileName);
        } catch (FileConflictException e) {
            return makeErrorModelAndView(modelAndView, HttpStatus.CONFLICT, e.getMessage());
        } catch (FileFormatException e) {
            return makeErrorModelAndView(modelAndView, HttpStatus.UNSUPPORTED_MEDIA_TYPE, e.getMessage());
        } catch (FileMissingException e) {
            return makeErrorModelAndView(modelAndView, HttpStatus.NOT_FOUND, e.getMessage());
        } catch (FileCommonException e) {
            return makeErrorModelAndView(modelAndView, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        modelAndView.setStatus(HttpStatus.CREATED);
        modelAndView.setViewName("redirect:/articles");
        return modelAndView;
    }

    @DeleteMapping("/{id}")
    public ModelAndView delete(@PathVariable("id") long id, ModelAndView modelAndView) {
        articleService.deleteById(id);
        modelAndView.setStatus(HttpStatus.OK);
        modelAndView.setViewName("redirect:/articles");
        return modelAndView;
    }

    //------------------------------------------------------------------------------------------------------------------

    private ModelAndView makeErrorModelAndView(ModelAndView modelAndView, HttpStatus httpStatus, String errorMessage) {
        modelAndView.setStatus(httpStatus);
        modelAndView.setViewName("/error");
        modelAndView.addObject("status", new ErrorDto(httpStatus.toString()));
        modelAndView.addObject("message", new ErrorDto(errorMessage));
        return modelAndView;
    }

    @Data
    @AllArgsConstructor
    public class ErrorDto {
        private String message;
    }
}
