package net.web.controller;

import net.core.IStorageController;
import net.core.alarms.dao.AlarmsLogDao;
import net.core.configurations.SSHConfiguration;
import net.core.hibernate.services.HostService;
import net.core.models.HostEditRow;
import net.core.tools.Authorization;
import net.core.tools.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;


@Controller
public class EditHostsController {

    @Autowired
    private Authorization authentication;
    @Autowired
    private AlarmsLogDao alarmsLogDao;
    @Autowired
    private IStorageController metricStorage;
    @Autowired
    private HostService hosts;
    public List<HostEditRow> getHostEditRow() throws SQLException {
        return metricStorage.getHostEditRow();
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleResourceNotFoundException() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("hotfound");
        return modelAndView;
    }
    //TODO: Редактор хостов
    @RequestMapping(value="/hostedit", method = RequestMethod.GET)
    public ModelAndView hostEditPage() throws SQLException, ParseException {
        if (!authentication.accessAdmin()) {
            throw new ResourceNotFoundException();
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("getHosts", getHostEditRow());
        modelAndView.setViewName("hostEditor");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName(); //get logged in username
        modelAndView.addObject("username", name);
        return modelAndView;
    }
    public void saveHost(int hostid,String ip,String login,String password,String name,int port, String location) throws SQLException {
        //metricStorage.updateHost(hostid,ip,login,password,port,name,location);
        if (!authentication.accessAdmin()) {
            throw new ResourceNotFoundException();
        }
        SSHConfiguration host = new SSHConfiguration();
        host.setId(hostid);
        host.setName(name);
        host.setHost(ip);
        host.setLocation(location);
        host.setLogin(login);
        host.setPassword(password);
        host.setPort(port);
        hosts.update(host);
    }
    @RequestMapping(value = "/gethost", method = RequestMethod.GET)
    @ResponseBody
    public SSHConfiguration gethost(@RequestParam("hostid") int hostid) {
        if (!authentication.accessAdmin()) {
            throw new ResourceNotFoundException();
        }
        return hosts.get(hostid);
    }
    @RequestMapping(value = "/saveHost", method = RequestMethod.GET)
    public void saveHost(@RequestParam("host") String host,@RequestParam("name") String name,@RequestParam("port") int port,@RequestParam("login") String login,@RequestParam("password") String password,@RequestParam("location") String location,@RequestParam("id") int id) throws SQLException {
        if (!authentication.accessAdmin()) {
            throw new ResourceNotFoundException();
        }
        saveHost(id,host,login,password,name,port,location);
    }
    @RequestMapping(value = "/addHost", method = RequestMethod.GET)
    public void addHost(@RequestParam("host") String host,@RequestParam("name") String name,@RequestParam("port") int port,@RequestParam("login") String login,@RequestParam("password") String password,@RequestParam("location") String location) throws SQLException {
        if (!authentication.accessAdmin()) {
            throw new ResourceNotFoundException();
        }
        SSHConfiguration sshConfiguration = new SSHConfiguration();
        sshConfiguration.setName(name);
        sshConfiguration.setHost(host);
        sshConfiguration.setLocation(location);
        sshConfiguration.setLogin(login);
        sshConfiguration.setPassword(password);
        sshConfiguration.setPort(port);
        hosts.save(sshConfiguration);
    }

}
