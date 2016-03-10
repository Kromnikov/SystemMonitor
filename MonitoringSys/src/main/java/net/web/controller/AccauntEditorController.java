package net.web.controller;

import net.core.AccauntDb;
import net.core.db.IMetricStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;

/**
 * Created by ANTON on 24.02.2016.
 */

@Controller
public class AccauntEditorController {
    @Autowired
    private IMetricStorage metricStorage;
    public void changeRole(int roleid,String username) throws SQLException {
        long count = metricStorage.getCountRoles();
        if (roleid<count)
            metricStorage.setNewUserRole(username,roleid+1);
        else metricStorage.setNewUserRole(username,1);
    }
    @RequestMapping(value = "/accaunts")
    public ModelAndView accauntsView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("getAllUsers", metricStorage.getAllUsers());
        modelAndView.setViewName("accaunts");
        return modelAndView;
    }

    @RequestMapping(params = {"roleid","username"}, value = "/accaunts")
    public ModelAndView accauntsChangeRole(int roleid,String username) throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        changeRole(roleid,username);
        modelAndView.addObject("getAllUsers", metricStorage.getAllUsers());
        modelAndView.setViewName("accaunts");
        return modelAndView;
    }

}