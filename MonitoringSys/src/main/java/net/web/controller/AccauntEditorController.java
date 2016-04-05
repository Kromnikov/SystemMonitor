package net.web.controller;

import net.core.AccauntDb;
import net.core.db.IMetricStorage;
import net.core.models.TemplateMetric;
import net.core.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
    @RequestMapping(value = "/accounts")
    public ModelAndView accauntsView() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("getAllUsers", metricStorage.getAllUsers());
        modelAndView.setViewName("accaunts");
        return modelAndView;
    }
    @RequestMapping(value = "/getAccounts", method = RequestMethod.GET)
    @ResponseBody
    public User getAccounts(@RequestParam("username") String username) throws SQLException {
        return metricStorage.getUsers(username);
    }
    @RequestMapping(value = "/saveAccount", method = RequestMethod.GET)
    public void saveAccount(@RequestParam("id") int id,@RequestParam("username") String username,@RequestParam("password") String password,@RequestParam("role") String role) throws SQLException {
        metricStorage.updateUser(id,username,password,role);
    }


//    @RequestMapping(params = {"roleid","username"}, value = "/accaunts")
//    public ModelAndView accauntsChangeRole(int roleid,String username) throws SQLException {
//        ModelAndView modelAndView = new ModelAndView();
//        changeRole(roleid,username);
//        modelAndView.addObject("getAllUsers", metricStorage.getAllUsers());
//        modelAndView.setViewName("accaunts");
//        return modelAndView;
//    }

}