package ru.parohodov.servicearticles.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.parohodov.servicearticles.service.dto.ArticleDto;
import ru.parohodov.servicearticles.service.ArticleService;
import java.util.List;

/**
 * @author Parohodov
 *
 * TODO Deploying
 * TODO Exceptions
 * TODO RestController
 * TODO Logging
 * TODO Bean Validation
 * TODO Authorization
 * TODO Uploading files
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping({"", "/"})
    public String fetchALl(Model model) {
        List<ArticleDto> list = articleService.getAllArticles();
        model.addAttribute("articles", list);
        return "/articles";
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
}
