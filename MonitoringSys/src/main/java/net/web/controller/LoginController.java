package net.web.controller;

import net.core.models.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

/**
 * Created by ANTON on 15.02.2016.
 */
@Controller
public class LoginController {
    @RequestMapping(value = "/login")
    public ModelAndView loginUser() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
    }
}
