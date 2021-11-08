package ru.parohodov.servicearticles.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import ru.parohodov.servicearticles.exception.ArticleAlreadyExistsException;
import ru.parohodov.servicearticles.exception.ArticleNotFoundException;
import ru.parohodov.servicearticles.exception.StorageException;
import ru.parohodov.servicearticles.service.ArticleService;
import ru.parohodov.servicearticles.service.FileProcessService;
import ru.parohodov.servicearticles.service.dto.ArticleDto;

/**
 * @author Parohodov
 *
 * DONE: Controller
 * DONE: Crud repository
 * DONE: small ui
 * TODO: RestController (?)
 * TODO: Uploading files
 * DONE: Exception handling
 * TODO: Normal UI
 * TODO: Deploying
 * TODO: tests
 * TODO: Filters for DB (?)
 * TODO: Logging (?)
 * TODO: Bean Validation (?)
 * TODO: Paging
 * TODO: Entity To DTO Conversion
 * TODO: Authentication
 */
@RequiredArgsConstructor
//@RestController
@Controller
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;
    private final FileProcessService fileProcessService;

    @GetMapping( "/")
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView fetchALl(ModelAndView modelAndView) {
        try {
            modelAndView.addObject("articles", articleService.getAllArticles());
        } catch (RuntimeException e) {
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

        } catch (StorageException se) {
            return makeErrorModelAndView(modelAndView, HttpStatus.NOT_FOUND, se.getMessage());
        } catch (ArticleNotFoundException anfe) {
            return makeErrorModelAndView(modelAndView, HttpStatus.NOT_FOUND, anfe.getMessage());
        }
        modelAndView.setStatus(HttpStatus.OK);
        modelAndView.setViewName("/article");
        return modelAndView;
    }

    @PostMapping( {"", "/"})
    public ModelAndView create(@RequestParam("file") MultipartFile fileName, ModelAndView modelAndView) {
        try {
            ArticleDto articleDto = fileProcessService.processFile(fileName);
            articleService.saveArticle(articleDto);
        } catch (ArticleAlreadyExistsException e) {
            return makeErrorModelAndView(modelAndView, HttpStatus.CONFLICT, e.getMessage());
        }
        modelAndView.setStatus(HttpStatus.CREATED);
        modelAndView.setViewName("redirect:/template/articles");
        return modelAndView;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") long id) {
        articleService.deleteById(id);
        return "redirect:/templates/articles";
    }

    //------------------------------------------------------------------------------------------------------------------

    private ModelAndView makeErrorModelAndView(ModelAndView modelAndView, HttpStatus httpStatus, String errorMessage) {
        modelAndView.setStatus(HttpStatus.NOT_FOUND);
        // FIXME: need to return redirect:
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
