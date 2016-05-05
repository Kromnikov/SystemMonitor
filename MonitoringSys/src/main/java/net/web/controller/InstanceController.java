package net.web.controller;

import net.core.IStorageController;
import net.core.hibernate.services.HostService;
import net.core.models.*;
import net.core.tools.Authorization;
import net.core.tools.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.SQLException;
import java.util.List;


@Controller
public class InstanceController {

    @Autowired
    private HostService hosts;
    @Autowired
    private IStorageController metricStorage;
    @Autowired
    private Authorization authentication;

    public List<InstanceMetric> getMetrics(int hostId) throws SQLException {
        return metricStorage.getInstMetrics(hostId);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleResourceNotFoundException() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("hotfound");
        return modelAndView;
    }
    //TODO: Контроллер для Instance метрик
    @RequestMapping(value = "/instMetric")
    public ModelAndView addInstMetricPage(@RequestParam(required = false, defaultValue = "-1") int instMetricId, @RequestParam(required = false, defaultValue = "-1") int hostId) throws SQLException {

        if (!authentication.accessAdmin()) {
            throw new ResourceNotFoundException();
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("InstMetric");
        modelAndView.addObject("username", SecurityContextHolder.getContext().getAuthentication().getName());
        if (instMetricId > 0) {
            InstanceMetric instanceMetric = metricStorage.getInstMetric(instMetricId);
            TemplateMetric templateMetric = metricStorage.getTemplateMetric(instanceMetric.getTempMetrcId());
            modelAndView.addObject("InstanceMetric", instanceMetric);
            modelAndView.addObject("templateMetric", templateMetric);
            modelAndView.addObject("host", hosts.get(instanceMetric.getHostId()));
//            modelAndView.addObject("instMetricId",instMetricId);
        }
        if (hostId > 0) {
            modelAndView.addObject("host", hosts.get(hostId));
        }
        return modelAndView;
    }

    @RequestMapping(value = "/getInstTempHost")
    @ResponseBody
    public InstTemplHostRow getInstTempHost(@RequestParam(required = false, defaultValue = "-1") int instMetricId) throws SQLException {

        if (!authentication.accessAdmin()) {
            throw new ResourceNotFoundException();
        }
        InstTemplHostRow metrics = new InstTemplHostRow();
        InstanceMetric instanceMetric = metricStorage.getInstMetric(instMetricId);
        metrics.setHost(hosts.get(instanceMetric.getHostId()));
        metrics.setInstanceMetrics(instanceMetric);
        metrics.setTemplateMetrics(metricStorage.getTemplateMetric(instanceMetric.getTempMetrcId()));
        return metrics;
    }

    @RequestMapping(value = "/getHostsTempl", method = RequestMethod.GET)
    @ResponseBody
    public HostsTemplMetricsRow getHostsTempl() throws SQLException {

        if (!authentication.accessAdmin()) {
            throw new ResourceNotFoundException();
        }
        HostsTemplMetricsRow hostsTemplMetricsRow = new HostsTemplMetricsRow();
        hostsTemplMetricsRow.setHosts(hosts.getAll());
        hostsTemplMetricsRow.setTemplateMetrics(metricStorage.getTemplatMetrics());
        return hostsTemplMetricsRow;
    }

    @RequestMapping(value = "/saveNewInstMetric", method = RequestMethod.GET)
    public void addInstMetric(@RequestParam(required = false, defaultValue = "-1") int templId, @RequestParam(required = false, defaultValue = "-1") int hostId, @RequestParam(required = false, defaultValue = "Err") String title, @RequestParam(required = false, defaultValue = "Err") String command, @RequestParam(required = false, defaultValue = "0") double minValue, @RequestParam(required = false, defaultValue = "0") double maxValue) throws SQLException {

        if (!authentication.accessAdmin()) {
            throw new ResourceNotFoundException();
        }
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
    public void editInstMetric(@RequestParam("id") int id, @RequestParam(required = false, defaultValue = "-1") int templId, @RequestParam(required = false, defaultValue = "-1") int hostId, @RequestParam(required = false, defaultValue = "Err") String title, @RequestParam(required = false, defaultValue = "Err") String command, @RequestParam(required = false, defaultValue = "0") double minValue, @RequestParam(required = false, defaultValue = "0") double maxValue) throws SQLException {

        if (!authentication.accessAdmin()) {
            throw new ResourceNotFoundException();
        }
        metricStorage.editInstMetric(id, hostId, templId, title, command, minValue, maxValue);
    }


    @RequestMapping(value = "/moveToInstMetric", method = RequestMethod.GET)
    @ResponseBody
    public MetricsRow addInstMetric(@RequestParam("hostid") int hostid, @RequestParam("templMetricid") int templMetricid) throws SQLException {

        if (!authentication.accessAdmin()) {
            throw new ResourceNotFoundException();
        }
        metricStorage.addInstMetric(hostid, templMetricid);
        MetricsRow metrics = new MetricsRow();
        metrics.setHostId(hostid);
        metrics.setInstanceMetrics(getMetrics(hostid));
        metrics.setTemplateMetrics(metricStorage.getTemplatMetrics());
        return metrics;
    }

    @RequestMapping(value = "/moveFromInstMetric", method = RequestMethod.GET)
    @ResponseBody
    public MetricsRow dellInstMetric(@RequestParam("hostid") int hostid, @RequestParam("instMetricid") int instMetricid) throws SQLException {

        if (!authentication.accessAdmin()) {
            throw new ResourceNotFoundException();
        }
        metricStorage.delMetricFromHost(hostid, instMetricid);
        MetricsRow metrics = new MetricsRow();
        metrics.setHostId(hostid);
        metrics.setInstanceMetrics(getMetrics(hostid));
        metrics.setTemplateMetrics(metricStorage.getTemplatMetrics());
        return metrics;
    }

    @RequestMapping(value = "/getMetricsRow", method = RequestMethod.GET)
    @ResponseBody
    public MetricsRow getInstMetrics(@RequestParam("hostid") int hostid) throws SQLException {

        if (!authentication.accessAdmin()) {
            throw new ResourceNotFoundException();
        }
        MetricsRow metrics = new MetricsRow();
        metrics.setHostId(hostid);
        metrics.setInstanceMetrics(getMetrics(hostid));
        metrics.setTemplateMetrics(metricStorage.getTemplatMetrics());
        return metrics;
    }
}
