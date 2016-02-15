package net.web.controller;

import net.core.models.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;


@Controller
public class IndexController {

    @RequestMapping(value = "/")
    public ModelAndView checkUser(@ModelAttribute("Values") ArrayList<Value> values) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        return modelAndView;
    }
}
