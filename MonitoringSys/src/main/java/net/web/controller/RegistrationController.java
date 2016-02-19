package net.web.controller;

import org.aspectj.apache.bcel.classfile.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.sql.DataSource;

/**
 * Created by ANTON on 17.02.2016.
 */
@Controller
public class RegistrationController {
    private JdbcTemplate jdbcTemplateObject;
    @Autowired
    public RegistrationController(DataSource dataSource)
    {
        this.jdbcTemplateObject = new JdbcTemplate(dataSource);
    }
    /*@RequestMapping(value = "/registration")
    public ModelAndView Registration(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("registration");
        return modelAndView;
    }*/
   /*@RequestMapping(value = "/registration", method = RequestMethod.POST)
    public ModelAndView RegistrationAdd1(@RequestParam (value = "username") String login,
                                        @RequestParam (value = "password") String password){
        if ((login!=null)&(password!=null)){
            String sqlUser = "INSERT INTO \"Users\" VALUES ("+login+","+password+",true)";
            String sqlRole = "INSERT INTO \"Roles\"(role,username) VALUES (USER,"+login+")";
           jdbcTemplateObject.update(sqlUser);
           jdbcTemplateObject.update(sqlRole);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("registration");
        return modelAndView;
    }*/
    @RequestMapping(method = RequestMethod.GET, value = "/registration")
    public ModelAndView Registration() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("registration");
        return modelAndView;
    }
 @RequestMapping(params = {"addUser"},method = RequestMethod.POST, value = "/registration")
 public ModelAndView RegistrationAdd(String username, String password){
     if ((username!=null)&(password!=null)){
         String sqlUser = "INSERT INTO \"Users\" VALUES ('"+username+"','"+password+"','true')";
         String sqlRole = "INSERT INTO \"Roles\"(role,username) VALUES ('USER','"+username+"')";
         jdbcTemplateObject.update(sqlUser);
         jdbcTemplateObject.update(sqlRole);
     }
     ModelAndView modelAndView = new ModelAndView();
     modelAndView.setViewName("registration");
     return modelAndView;
 }
   /* @RequestMapping(method = RequestMethod.POST, value = "/registration")
    public ModelAndView RegistrationAdd1(@RequestParam("username") String username, @RequestParam("password") String password){
        if ((username!=null)&(password!=null)){
            System.out.print("I'm here");
            String sqlUser = "INSERT INTO \"Users\" VALUES ("+username+","+password+",'true')";
            String sqlRole = "INSERT INTO \"Roles\"(role,username) VALUES (USER,"+username+")";
            jdbcTemplateObject.update(sqlUser);
            jdbcTemplateObject.update(sqlRole);
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("registration");
        return modelAndView;
    }*/

}
