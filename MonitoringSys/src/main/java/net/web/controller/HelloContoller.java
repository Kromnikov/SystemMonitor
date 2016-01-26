package net.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by ANTON on 26.01.2016.
 */
@Controller
public class HelloContoller {
    @RequestMapping("/test")
    public String test(ModelAndView modelAndView) {
        return "welcome";
    }
}
