package ru.parohodov.servicearticles.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.parohodov.servicearticles.service.dto.ArticleDto;
import ru.parohodov.servicearticles.service.ArticleService;

import java.net.http.HttpResponse;
import java.util.List;

/**
 * @author Parohodov
 *
 * DONE: Controller
 * DONE: Crud repository
 * DONE: small ui
 * TODO: RestController
 * TODO: Uploading files
 * TODO: Exception handling
 * TODO: Deploying
 * TODO: Filters for DB (?)
 * TODO: Logging (?)
 * TODO: Bean Validation (?)
 * TODO: Paging
 * TODO: tests
 * TODO: Authentication
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;

//    @GetMapping({"", "/"})
//    public String fetchALl(Model model) {
//        List<ArticleDto> list = articleService.getAllArticles();
//        model.addAttribute("articles", list);
//        return "/articles";
//    }

    @GetMapping({"", "/"})
    public ModelAndView fetchAll(ModelAndView modelAndView) {
        modelAndView.setViewName("/articles");
        modelAndView.addObject("articles", articleService.getAllArticles());
        modelAndView.setStatus(HttpStatus.OK);
        return modelAndView;
    }

    @GetMapping("/{id}")
    public String fetchById(@PathVariable("id") long id, Model model) {
        model.addAttribute("article", articleService.getArticleById(id));
        return "/article";
    }

    @PostMapping({"", "/"})
    public String create() {
        return "redirect:/templates/articles";
    }

    @DeleteMapping("/{id}")
    public String delete() {
        return "redirect:/templates/articles";
    }
}
