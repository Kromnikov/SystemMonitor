package net.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController {
    @RequestMapping("/403")
    public String accessDenied() {
        return "hotfound";
    }
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    @RequestMapping("/404")
//    public String notFound() {
//        return "hotfound";
//    }
//
//    @ExceptionHandler(NoHandlerFoundException.class)
//    public String handle(Exception ex) {
//        return "hotfound";
//    }
//    @ExceptionHandler(ResourceNotFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ModelAndView handleResourceNotFoundException() {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("hotfound");
//        return modelAndView;
//    }
      /*if (!authentication.accessAdmin()) {
            throw new ResourceNotFoundException();
        }*/
}
