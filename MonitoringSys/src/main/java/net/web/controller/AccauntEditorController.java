package net.web.controller;

import net.core.IStorageController;
import net.core.models.User;
import net.core.tools.Authorization;
import net.core.tools.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.List;

@Controller
public class AccauntEditorController {
    @Autowired
    private Authorization authentication;
    @Autowired
    private IStorageController metricStorage;
    public void changeRole(int roleid,String username) throws SQLException {
        long count = metricStorage.getCountRoles();
        if (roleid<count)
            metricStorage.setNewUserRole(username,roleid+1);
        else metricStorage.setNewUserRole(username,1);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleResourceNotFoundException() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("hotfound");
        return modelAndView;
    }
    @RequestMapping(value = "/accounts")
    public ModelAndView accauntsView() {
        if (!authentication.accessAdmin()) {
            throw new ResourceNotFoundException();
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("getAllUsers", metricStorage.getAllUsers());
        modelAndView.setViewName("accaunts");
        return modelAndView;
    }
    @RequestMapping(value = "/getRoles", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getRoles() throws SQLException {
        if (!authentication.accessAdmin()) {
            throw new ResourceNotFoundException();
        }
        return metricStorage.getRoles();
    }
    @RequestMapping(value = "/getAccounts", method = RequestMethod.GET)
    @ResponseBody
    public User getAccounts(@RequestParam("username") String username) throws SQLException {
        if (!authentication.accessAdmin()) {
            throw new ResourceNotFoundException();
        }
        return metricStorage.getUsers(username);
    }
    @RequestMapping(value = "/saveAccount", method = RequestMethod.GET)
    public void saveAccount(@RequestParam("id") int id,@RequestParam("username") String username,@RequestParam("password") String password,@RequestParam("role") String role) throws SQLException {
        if (!authentication.accessAdmin()) {
            throw new ResourceNotFoundException();
        }
        metricStorage.updateUser(id,username,password,role);
    }
    @RequestMapping(value = "/addAccount", method = RequestMethod.GET)
    public void addAccount(@RequestParam("username") String username,@RequestParam("password") String password,@RequestParam("role") String role) throws SQLException {
        if (!authentication.accessAdmin()) {
            throw new ResourceNotFoundException();
        }
        metricStorage.addUser(username,password,role);
    }
    @RequestMapping(value = "/dellAccount", method = RequestMethod.GET)
    public String dellHost(@RequestParam("username") String username) throws SQLException {
        if (!authentication.accessAdmin()) {
            throw new ResourceNotFoundException();
        }
        metricStorage.dellUser(username);
        return "redirect:/accounts";
    }

}