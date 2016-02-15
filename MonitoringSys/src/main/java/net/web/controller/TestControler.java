package net.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Kromnikov on 14.02.2016.
 */

@Controller
public class TestControler {

    @RequestMapping(value = "/test1/test", method = RequestMethod.GET)
    public String getPersons(Model model) {

        return "tests/test1";
    }
}
