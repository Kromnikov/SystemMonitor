package net.web.controller;

import net.core.models.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;


@Controller
public class IndexController {
//    @RequestMapping("/")
//    String index(String name, Model model) {
//        model.addAttribute("name", "192.168.0.1");
//        return "index";
//    }

    @RequestMapping(value = "/")
    public ModelAndView checkUser(@ModelAttribute("Values") ArrayList<Value> values) {
        values = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            values.add(i,new Value());
            values.get(i).setValue(i);
        }
        ModelAndView modelAndView = new ModelAndView();

        //имя представления, куда нужно будет перейти
        modelAndView.setViewName("index");

        //записываем в атрибут userJSP (используется на странице *.jsp объект user
        modelAndView.addObject("Values", values);

        return modelAndView; //после уйдем на представление, указанное чуть выше, если оно будет найдено.
    }
}
