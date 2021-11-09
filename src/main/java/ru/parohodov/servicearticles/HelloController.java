package ru.parohodov.servicearticles;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Parohodov
 */
@Controller
public class HelloController {
    @GetMapping("/")
    public String hello() {
        return "/hello";
    }
}
