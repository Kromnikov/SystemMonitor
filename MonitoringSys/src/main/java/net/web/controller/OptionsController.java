package net.web.controller;

import net.core.configurations.SSHConfiguration;
import net.core.db.IMetricStorage;
import net.core.hibernate.services.HostService;
import net.core.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

@Controller
public class OptionsController {
    @Autowired
    private IMetricStorage metricStorage;
    @Autowired
    private HostService hosts;

    public List<InstanceMetric> getMetrics(int hostId) throws SQLException {
        return metricStorage.getInstMetrics(hostId);
    }
    public List<HostEditRow> getHostEditRow() throws SQLException {
        return metricStorage.getHostEditRow();
    }

    //TODO:  Контроллер для alarms
    @RequestMapping(value="/alarms")
    public ModelAndView alarms() throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        modelAndView.addObject("getAlarms", metricStorage.getAlarms(name));
        modelAndView.setViewName("alarms");
        modelAndView.addObject("username", name);
        return modelAndView;
    }
    @RequestMapping(value = "/getAlarm", method = RequestMethod.GET)
    @ResponseBody
    public AlarmRow getAlarm(@RequestParam("id") int id) throws SQLException {
        return metricStorage.getAlarm(id);
    }
    @RequestMapping(value = "/getNewAlarm", method = RequestMethod.GET)
    @ResponseBody
    public AlarmRow getNewAlarm() throws SQLException {
        return metricStorage.getNewAlarm();
    }
    @RequestMapping(value = "/getInstanceMetrics", method = RequestMethod.GET)
    @ResponseBody
    public List<InstanceMetric> getInstanceMetrics(@RequestParam("hostId") int id) throws SQLException {
        return metricStorage.getInstMetrics(id);
    }
    @RequestMapping(value = "/saveAlarm", method = RequestMethod.GET)
    @ResponseBody
    public void saveAlarm(@RequestParam("id") int id,@RequestParam("toEmail") String toEmail,@RequestParam("toUser") String toUser,@RequestParam("metricId") int metricId,@RequestParam("hostId") int hostId) throws SQLException {
        metricStorage.updateAlarm(id,metricId,hostId,toEmail,toUser);
    }
    @RequestMapping(value = "/addAlarm", method = RequestMethod.GET)
    @ResponseBody
    public void addAlarm(@RequestParam("toEmail") String toEmail,@RequestParam("toUser") String toUser,@RequestParam("metricId") int metricId,@RequestParam("hostId") int hostId,@RequestParam("user") String user) throws SQLException {
        metricStorage.addAlarm(metricId, hostId, toEmail, toUser, user);
    }
    @RequestMapping(value = "/dellAlarms", method = RequestMethod.GET)
    @ResponseBody
    public void dellAlarm(@RequestParam("id") int id) throws SQLException {
        metricStorage.dellAlarm(id);
    }












    //TODO: Контроллер для Templat метрик
    @RequestMapping(value="/templMetrics")
    public ModelAndView templMetrics(@RequestParam(required = false , defaultValue = "-1") int id) throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("getTemplatMetrics",metricStorage.getTemplatMetrics());
        if(id>0)
        modelAndView.addObject("templMetric",metricStorage.getTemplateMetric(id));
        modelAndView.setViewName("tempMetrics");
        modelAndView.addObject("username", SecurityContextHolder.getContext().getAuthentication().getName());
        return modelAndView;
    }

    @RequestMapping(value = "/getTemplMetric", method = RequestMethod.GET)
    @ResponseBody
    public TemplateMetric getTemplMetric(@RequestParam("metricId") int metricId) throws SQLException {
        return metricStorage.getTemplateMetric(metricId);
    }

    @RequestMapping(value = "/saveTemplMetric", method = RequestMethod.GET)
    public void saveTemplMetric(@RequestParam("id") int id,@RequestParam("title") String title,@RequestParam("command") String command,@RequestParam("minValue") double minValue,@RequestParam("maxValue") double maxValue) throws SQLException {
        metricStorage.updateTemplMetric(id, title, command, minValue, maxValue);
    }

    @RequestMapping(value = "/addTemplMetric", method = RequestMethod.GET)
    public void addTemplMetric(@RequestParam("title") String title,@RequestParam("command") String command,@RequestParam("minValue") double minValue,@RequestParam("maxValue") double maxValue) throws SQLException {
        metricStorage.addTemplMetric(title, command, minValue, maxValue);
    }
    @RequestMapping(value = "/dellTemplMetric", method = RequestMethod.GET)
    public void dellTemplMetric(@RequestParam("metricId") int id) throws SQLException {
        metricStorage.dellTemplMetric(id);
    }




    //TODO: Контроллер для Instance метрик
    @RequestMapping(value= "/instMetric")
    public ModelAndView addInstMetricPage(@RequestParam(required = false , defaultValue = "-1") int instMetricId,@RequestParam(required = false , defaultValue = "-1") int hostId) throws SQLException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("InstMetric");
        modelAndView.addObject("username", SecurityContextHolder.getContext().getAuthentication().getName());
        if (instMetricId > 0) {
            InstanceMetric instanceMetric =  metricStorage.getInstMetric(instMetricId);
            TemplateMetric templateMetric = metricStorage.getTemplateMetric(instanceMetric.getTempMetrcId());
            modelAndView.addObject("InstanceMetric",instanceMetric);
            modelAndView.addObject("templateMetric",templateMetric);
            modelAndView.addObject("host",hosts.get(instanceMetric.getHostId()) );
//            modelAndView.addObject("instMetricId",instMetricId);
        }
        if (hostId > 0) {
            modelAndView.addObject("host",hosts.get(hostId) );
        }
        return modelAndView;
    }
    @RequestMapping(value = "/getHostsTempl", method = RequestMethod.GET)
    @ResponseBody
    public HostsTemplMetricsRow getHostsTempl() throws SQLException {
        HostsTemplMetricsRow hostsTemplMetricsRow = new HostsTemplMetricsRow();
        hostsTemplMetricsRow.setHosts(hosts.getAll());
        hostsTemplMetricsRow.setTemplateMetrics(metricStorage.getTemplatMetrics());
        return hostsTemplMetricsRow;
    }
    @RequestMapping(value = "/saveNewInstMetric", method = RequestMethod.GET)
    public void addInstMetric(@RequestParam(required = false , defaultValue = "-1") int templId,@RequestParam(required = false , defaultValue = "-1") int hostId,@RequestParam(required = false , defaultValue = "Err") String title,@RequestParam(required = false , defaultValue = "Err") String command,@RequestParam(required = false , defaultValue = "0") double minValue,@RequestParam(required = false , defaultValue = "0") double maxValue) throws SQLException {
        InstanceMetric instanceMetric = new InstanceMetric();
        instanceMetric.setMinValue(minValue);
        instanceMetric.setMaxValue(maxValue);
        instanceMetric.setCommand(command);
        instanceMetric.setTitle(title);
        instanceMetric.setTempMetrcId(templId);
        instanceMetric.setHostId(hostId);
        metricStorage.addInstMetric(instanceMetric);
    }
    @RequestMapping(value = "/editInstMetric", method = RequestMethod.GET)
    public void editInstMetric(@RequestParam("id") int id, @RequestParam(required = false , defaultValue = "-1") int templId,@RequestParam(required = false , defaultValue = "-1") int hostId,@RequestParam(required = false , defaultValue = "Err") String title,@RequestParam(required = false , defaultValue = "Err") String command,@RequestParam(required = false , defaultValue = "0") double minValue,@RequestParam(required = false , defaultValue = "0") double maxValue) throws SQLException {
        metricStorage.editInstMetric(id, hostId, templId, title, command, minValue, maxValue);
    }



    @RequestMapping(value = "/moveToInstMetric", method = RequestMethod.GET)
    @ResponseBody
    public MetricsRow addInstMetric(@RequestParam("hostid") int hostid,@RequestParam("templMetricid") int templMetricid) throws SQLException {
        metricStorage.addInstMetric(hostid,templMetricid);
        MetricsRow metrics = new MetricsRow();
        metrics.setHostId(hostid);
        metrics.setInstanceMetrics(getMetrics(hostid));
        metrics.setTemplateMetrics(metricStorage.getTemplatMetrics());
        return metrics;
    }

    @RequestMapping(value = "/moveFromInstMetric", method = RequestMethod.GET)
    @ResponseBody
    public MetricsRow dellInstMetric(@RequestParam("hostid") int hostid,@RequestParam("instMetricid") int instMetricid) throws SQLException {
        metricStorage.delMetricFromHost(hostid,instMetricid);
        MetricsRow metrics = new MetricsRow();
        metrics.setHostId(hostid);
        metrics.setInstanceMetrics(getMetrics(hostid));
        metrics.setTemplateMetrics(metricStorage.getTemplatMetrics());
        return metrics;
    }

    @RequestMapping(value = "/getMetricsRow", method = RequestMethod.GET)
    @ResponseBody
    public MetricsRow getInstMetrics(@RequestParam("hostid") int hostid) throws SQLException {
        MetricsRow metrics = new MetricsRow();
        metrics.setHostId(hostid);
        metrics.setInstanceMetrics(getMetrics(hostid));
        metrics.setTemplateMetrics(metricStorage.getTemplatMetrics());
        return metrics;
    }







//    @RequestMapping(method = RequestMethod.GET, value = "/optionsInstance")
//    public ModelAndView standartView() throws SQLException {
//        ModelAndView modelAndView = new ModelAndView();
//        int instid=1;
//        modelAndView.addObject("getInstanceMetrics",metricStorage.getInstMetrics(1));
//        modelAndView.addObject("getHosts",hosts.getAll());
//        modelAndView.addObject("hostid",1);
//        double min= metricStorage.getMinValueInstanceMetric(1);
//        double max= metricStorage.getMaxValueInstanceMetric(1);
//        modelAndView.addObject("min",min);
//        modelAndView.addObject("max",max);
//        modelAndView.addObject("instid",instid);
//        modelAndView.setViewName("instanceMetricsOption");
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        modelAndView.addObject("username", name);
//        return modelAndView;
//    }
//    @RequestMapping(params = {"hostid"},method = RequestMethod.GET, value = "/optionsInstance")
//    public ModelAndView selectHostView(int hostid) throws SQLException {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("getInstanceMetrics",metricStorage.getInstMetrics(hostid));
//        modelAndView.addObject("getHosts",hosts.getAll());
//        modelAndView.addObject("hostid",hostid);
//        double min= metricStorage.getMinValueInstanceMetric(1);
//        double max= metricStorage.getMaxValueInstanceMetric(1);
//        modelAndView.addObject("min",min);
//        modelAndView.addObject("max",max);
//        modelAndView.addObject("instid",1);
//        modelAndView.setViewName("instanceMetricsOption");
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        modelAndView.addObject("username", name);
//        return modelAndView;
//    }
//    @RequestMapping(params = {"hostid","instid"},method = RequestMethod.GET, value = "/optionsInstance")
//    public ModelAndView selectMetricView(int hostid,int instid) throws SQLException {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("getInstanceMetrics",metricStorage.getInstMetrics(hostid));
//        modelAndView.addObject("getHosts",hosts.getAll());
//        modelAndView.addObject("hostid",hostid);
//        double min= metricStorage.getMinValueInstanceMetric(instid);
//        double max= metricStorage.getMaxValueInstanceMetric(instid);
//        modelAndView.addObject("min",min);
//        modelAndView.addObject("max",max);
//        modelAndView.addObject("instid",instid);
//        modelAndView.setViewName("instanceMetricsOption");
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        modelAndView.addObject("username", name);
//        return modelAndView;
//    }
//    @RequestMapping(params = {"save"},method = RequestMethod.GET, value = "/optionsInstance")
//    public ModelAndView saveInstMetic(double min_value, double max_value,int save) throws SQLException {
//        ModelAndView modelAndView = new ModelAndView();
//        metricStorage.updateMinMaxValueInstanceMetric(min_value,max_value,save);
//        modelAndView.addObject("min",min_value);
//        modelAndView.addObject("max",max_value);
//        modelAndView.addObject("getInstanceMetrics",metricStorage.getInstMetrics(1));
//        modelAndView.addObject("getHosts",hosts.getAll());
//        modelAndView.addObject("getTemplatMetrics",metricStorage.getTemplatMetrics());
//        modelAndView.setViewName("instanceMetricsOption");
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        modelAndView.addObject("username", name);
//        return modelAndView;
//    }
//
//
//
//
//
//
//
//    //TODO:editIntsMetrics
//
//
//    @RequestMapping(value="/editIntsMetrics", method = RequestMethod.GET)
//    public ModelAndView instMetricPage() throws SQLException, ParseException {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("getHosts",hosts.getAll());
//        modelAndView.addObject("getTemplatMetrics", metricStorage.getTemplatMetrics());
//        modelAndView.addObject("getMetrics", metricStorage.getInstMetrics(1));
//        modelAndView.addObject("hostid", 1);
//        //modelAndView.addObject("templMetricid", 0);
//       // modelAndView.addObject("instMetricid",0);
//        modelAndView.setViewName("addIntsMetric");
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        modelAndView.addObject("username", name);
//        return modelAndView;
//    }
//    @RequestMapping(params={"hostid"},value="/editIntsMetrics", method = RequestMethod.GET)
//    public ModelAndView instMetricPageGetHost(int hostid) throws SQLException, ParseException {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.addObject("getHosts",hosts.getAll());
//        modelAndView.addObject("getTemplatMetrics", metricStorage.getTemplatMetrics());
//        modelAndView.addObject("getMetrics", metricStorage.getInstMetrics(hostid));
//        modelAndView.addObject("hostid", hostid);
//       // modelAndView.addObject("templMetricid", 0);
//       // modelAndView.addObject("instMetricid",0);
//        modelAndView.setViewName("addIntsMetric");
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        modelAndView.addObject("username", name);
//        return modelAndView;
//    }
//    @RequestMapping(params={"hostid","instMetricid"},value="/editIntsMetrics", method = RequestMethod.GET)
//    public ModelAndView instMetricPageDelInstMetric(int hostid,int instMetricid) throws SQLException, ParseException {
//        ModelAndView modelAndView = new ModelAndView();
//        metricStorage.delMetricFromHost(hostid,instMetricid);
//        modelAndView.addObject("getHosts",hosts.getAll());
//        modelAndView.addObject("getTemplatMetrics", metricStorage.getTemplatMetrics());
//        modelAndView.addObject("getMetrics", metricStorage.getInstMetrics(hostid));
//        modelAndView.addObject("hostid", hostid);
//        modelAndView.setViewName("addIntsMetric");
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        modelAndView.addObject("username", name);
//        return modelAndView;
//    }
//    @RequestMapping(params={"hostid","templMetricid"},value="/editIntsMetrics", method = RequestMethod.GET)
//    public ModelAndView instMetricPageAddInstMetric(int hostid,int templMetricid) throws SQLException, ParseException {
//        ModelAndView modelAndView = new ModelAndView();
//        metricStorage.addInstMetric(hostid,templMetricid);
//        modelAndView.addObject("getHosts",hosts.getAll());
//        modelAndView.addObject("getTemplatMetrics", metricStorage.getTemplatMetrics());
//        modelAndView.addObject("getMetrics", metricStorage.getInstMetrics(hostid));
//        modelAndView.addObject("hostid", hostid);
//        modelAndView.setViewName("addIntsMetric");
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName(); //get logged in username
//        modelAndView.addObject("username", name);
//        return modelAndView;
//    }
//





    //TODO: Редактор хостов
    @RequestMapping(value="/hostedit", method = RequestMethod.GET)
    public ModelAndView hostEditPage() throws SQLException, ParseException {
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
        return hosts.get(hostid);
    }

    @RequestMapping(value = "/saveHost", method = RequestMethod.GET)
    public void saveHost(@RequestParam("host") String host,@RequestParam("name") String name,@RequestParam("port") int port,@RequestParam("login") String login,@RequestParam("password") String password,@RequestParam("location") String location,@RequestParam("id") int id) throws SQLException {
        saveHost(id,host,login,password,name,port,location);
    }

}
