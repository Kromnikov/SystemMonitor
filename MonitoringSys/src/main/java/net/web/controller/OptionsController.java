package net.web.controller;

import net.core.MetricStorage;
import net.core.configurations.SSHConfiguration;
import net.core.db.IMetricStorage;
import net.core.hibernate.dao.HostDaoImpl;
import net.core.hibernate.services.HostService;
import net.core.hibernate.services.HostServiceImpl;
import net.core.models.InstanceMetric;
import net.core.models.TemplateMetric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ANTON on 28.02.2016.
 */
@Controller
public class OptionsController {
    @Autowired
    private IMetricStorage metricStorage;
    @Autowired
    private HostService hosts;

    public int getProblemsCount(int hostId) throws SQLException {
        return (int)metricStorage.getMetricNotResolvedLength(hostId);
    }
    public List<InstanceMetric> getMetrics(int hostId) throws SQLException {
        return metricStorage.getInstMetrics(hostId);
    }
    public int getAllProblemsCount() throws SQLException {
        return ((int)metricStorage.getMetricNotResolvedLength()+(int)metricStorage.getHostNotResolvedLength());
    }

    public InstanceMetric getInstMetricCharacters(int id) throws SQLException {
        InstanceMetric instanceMetrics;
        instanceMetrics = metricStorage.getInstMetricById(id);
        return instanceMetrics;
    }

    public InstanceMetric getTopInstMetricCharacters() throws SQLException {
        InstanceMetric instanceMetrics;
        instanceMetrics = metricStorage.getTopInstMetric();
        return instanceMetrics;
    }
    public void saveHost(int hostid,String ip,String login,String password,String name,int port, String location) throws SQLException {
        //metricStorage.updateHost(hostid,ip,login,password,port,name,location);
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
    //Контроллер для Templat метрик
    @RequestMapping(method = RequestMethod.GET, value = "/options")
    public ModelAndView metric() throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("getTemplatMetrics",metricStorage.getTemplatMetrics());
        modelAndView.addObject("tempMetric",metricStorage.getTopTemplatMetric());
        modelAndView.setViewName("templetMetrics");
        return modelAndView;
    }

    @RequestMapping(params = {"tempid"},method = RequestMethod.GET, value = "/options")
    public ModelAndView metricGet(int tempid) throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("getTemplatMetrics",metricStorage.getTemplatMetrics());
        modelAndView.addObject("tempMetric",metricStorage.getTemplateMetric(tempid));
        modelAndView.setViewName("templetMetrics");
        return modelAndView;
    }
    @RequestMapping(params = {"save"},method = RequestMethod.GET, value = "/options")
    public ModelAndView metricSet(double min_value, double max_value,int save, String title, String command) throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        metricStorage.updateTemplateMetric(min_value,max_value,save,title,command);  //через save передаю id хоста, вот такой вот костыль)
        modelAndView.addObject("getTemplatMetrics",metricStorage.getTemplatMetrics());
        modelAndView.addObject("tempMetric",metricStorage.getTemplateMetric(save));
        modelAndView.setViewName("templetMetrics");
        return modelAndView;
    }
    //Контроллер для Instance метрик
    @RequestMapping(method = RequestMethod.GET, value = "/optionsInstance")
    public ModelAndView standartView() throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        InstanceMetric instanceMetric = getTopInstMetricCharacters();
        modelAndView.addObject("getInstanceMetrics",metricStorage.getInstMetrics(1));
        modelAndView.addObject("instMetric",instanceMetric);
        modelAndView.addObject("getHosts",hosts.getAll());
        modelAndView.addObject("hostid",instanceMetric.getHostId());
        modelAndView.setViewName("instanceMetricsOption");
        return modelAndView;
    }
    @RequestMapping(params = {"hostid"},method = RequestMethod.GET, value = "/optionsInstance")
    public ModelAndView selectHostView(int hostid) throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        InstanceMetric instanceMetric = getTopInstMetricCharacters();
        modelAndView.addObject("getInstanceMetrics",metricStorage.getInstMetrics(hostid));
        modelAndView.addObject("instMetric",instanceMetric);
        modelAndView.addObject("getHosts",hosts.getAll());
        modelAndView.addObject("hostid",hostid);
        modelAndView.setViewName("instanceMetricsOption");
        return modelAndView;
    }
    @RequestMapping(params = {"hostid","instid"},method = RequestMethod.GET, value = "/optionsInstance")
    public ModelAndView selectMetricView(int hostid,int instid) throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        InstanceMetric instanceMetric = getInstMetricCharacters(instid);
        modelAndView.addObject("getInstanceMetrics",metricStorage.getInstMetrics(hostid));
        modelAndView.addObject("instMetric",instanceMetric);
        modelAndView.addObject("getHosts",hosts.getAll());
        modelAndView.addObject("hostid",hostid);
        modelAndView.addObject("instid",instid);
        modelAndView.setViewName("instanceMetricsOption");
        return modelAndView;
    }
    @RequestMapping(params = {"save"},method = RequestMethod.GET, value = "/optionsInstance")
    public ModelAndView saveInstMetic(double min_value, double max_value,int save,String title,String command) throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        metricStorage.updateInstMetric(min_value,max_value,save,title,command);
        InstanceMetric instanceMetric = getTopInstMetricCharacters();
        modelAndView.addObject("getInstanceMetrics",metricStorage.getInstMetrics(1));
        modelAndView.addObject("instMetric",instanceMetric);
        modelAndView.addObject("getHosts",hosts.getAll());
        modelAndView.setViewName("instanceMetricsOption");
        return modelAndView;
    }


    @RequestMapping(value="/editIntsMetrics", method = RequestMethod.GET)
    public ModelAndView instMetricPage() throws SQLException, ParseException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("getHosts",hosts.getAll());
        modelAndView.addObject("getTemplatMetrics", metricStorage.getTemplatMetrics());
        modelAndView.addObject("getMetrics", metricStorage.getInstMetrics(1));
        modelAndView.addObject("hostid", 1);
        modelAndView.setViewName("addIntsMetric");
        return modelAndView;
    }
    @RequestMapping(params={"hostid"},value="/editIntsMetrics", method = RequestMethod.GET)
    public ModelAndView instMetricPageGetHost(int hostid) throws SQLException, ParseException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("getHosts",hosts.getAll());
        modelAndView.addObject("getTemplatMetrics", metricStorage.getTemplatMetrics());
        modelAndView.addObject("getMetrics", metricStorage.getInstMetrics(hostid));
        modelAndView.addObject("hostid", hostid);
        modelAndView.setViewName("addIntsMetric");
        return modelAndView;
    }
    @RequestMapping(params={"hostid","instMetricid"},value="/editIntsMetrics", method = RequestMethod.GET)
    public ModelAndView instMetricPageDelInstMetric(int hostid,int instMetricid) throws SQLException, ParseException {
        ModelAndView modelAndView = new ModelAndView();
        metricStorage.delMetricFromHost(hostid,instMetricid);
        modelAndView.addObject("getHosts",hosts.getAll());
        modelAndView.addObject("getTemplatMetrics", metricStorage.getTemplatMetrics());
        modelAndView.addObject("getMetrics", metricStorage.getInstMetrics(hostid));
        modelAndView.addObject("hostid", hostid);
        modelAndView.setViewName("addIntsMetric");
        return modelAndView;
    }
    @RequestMapping(params={"hostid","templMetricid"},value="/editIntsMetrics", method = RequestMethod.GET)
    public ModelAndView instMetricPageAddInstMetric(int hostid,int templMetricid) throws SQLException, ParseException {
        ModelAndView modelAndView = new ModelAndView();
        metricStorage.addInstMetric(hostid,templMetricid);
        modelAndView.addObject("getHosts",hosts.getAll());
        modelAndView.addObject("getTemplatMetrics", metricStorage.getTemplatMetrics());
        modelAndView.addObject("getMetrics", metricStorage.getInstMetrics(hostid));
        modelAndView.addObject("hostid", hostid);
        modelAndView.setViewName("addIntsMetric");
        return modelAndView;
    }

    //Редактор хостов
    @RequestMapping(value="/hostedit", method = RequestMethod.GET)
    public ModelAndView hostEditPage() throws SQLException, ParseException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("getHosts",hosts.getAll());
        SSHConfiguration host=hosts.get(1);
        modelAndView.addObject("host", host);
        modelAndView.addObject("hostid",1);
        modelAndView.setViewName("hostEditor");
        return modelAndView;
    }
    @RequestMapping(params={"hostid"}, value="/hostedit", method = RequestMethod.GET)
    public ModelAndView hostEditChose(int hostid) throws SQLException, ParseException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("getHosts",hosts.getAll());
        SSHConfiguration host=hosts.get(hostid);
        modelAndView.addObject("hostid",hostid);
        modelAndView.addObject("host", host);
        modelAndView.setViewName("hostEditor");
        return modelAndView;
    }
    @RequestMapping(params={"save"}, value="/hostedit", method = RequestMethod.GET)
    public ModelAndView hostEditSave(int save,String ip,String login,String password,String name,int port,String location) throws SQLException, ParseException {
        saveHost(save,ip,login,password,name,port,location);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("getHosts",hosts.getAll());
        SSHConfiguration host=hosts.get(save);
        modelAndView.addObject("host", host);
        modelAndView.addObject("hostid",save);
        modelAndView.setViewName("hostEditor");
        return modelAndView;
    }

}
