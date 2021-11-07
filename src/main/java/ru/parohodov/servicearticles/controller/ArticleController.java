package ru.parohodov.servicearticles.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.parohodov.servicearticles.service.ArticleService;

/**
 * @author Parohodov
 *
 * DONE: Controller
 * DONE: Crud repository
 * DONE: small ui
 * TODO: RestController
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
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView fetchAll(ModelAndView modelAndView) {
        modelAndView.setViewName("/articles");
        modelAndView.addObject("articles", articleService.getAllArticles());
        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView fetchById(@PathVariable("id") long id, ModelAndView modelAndView) {
        modelAndView.setViewName("/article");
        modelAndView.addObject("article", articleService.getArticleById(id));
        return modelAndView;
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
