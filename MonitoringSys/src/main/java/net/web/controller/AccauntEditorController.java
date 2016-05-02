package net.web.controller;

import net.core.IRouteStorage;
import net.core.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by ANTON on 24.02.2016.
 */

@Controller
public class AccauntEditorController {
    @Autowired
    private IRouteStorage metricStorage;
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
    @RequestMapping(value = "/getRoles", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getRoles() throws SQLException {
        return metricStorage.getRoles();
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
    @RequestMapping(value = "/addAccount", method = RequestMethod.GET)
    public void addAccount(@RequestParam("username") String username,@RequestParam("password") String password,@RequestParam("role") String role) throws SQLException {
        metricStorage.addUser(username,password,role);
    }
    @RequestMapping(value = "/dellAccount", method = RequestMethod.GET)
    public String dellHost(@RequestParam("username") String username) throws SQLException {
        metricStorage.dellUser(username);
        return "redirect:/accounts";
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