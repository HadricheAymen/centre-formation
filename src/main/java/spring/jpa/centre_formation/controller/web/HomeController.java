package spring.jpa.centre_formation.controller.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Contrôleur pour la page d'accueil
 */
@Controller
public class HomeController {
    
    @GetMapping("/")
    public String home() {
        return "index";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}

