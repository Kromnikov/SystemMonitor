package net.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.NoHandlerFoundException;

@Controller
public class ErrorController {
    @RequestMapping("/403")
    public String accessDenied() {
        return "hotfound";
    }
    @RequestMapping("/404")
    public String notFound() {
        return "hotfound";
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handle(Exception ex) {
        return "hotfound";
    }
}
