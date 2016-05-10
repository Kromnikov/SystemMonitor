package net.web.controller;

import net.core.IStorageController;
import net.core.hibernate.services.HostService;
import net.core.models.HostsTemplMetricsRow;
import net.core.models.InstTemplHostRow;
import net.core.models.InstanceMetric;
import net.core.models.MetricsRow;
import net.core.tools.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.List;


@Controller
@RequestMapping(value = "/admin")
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

//    //TODO: Контроллер для Instance метрик
//    @RequestMapping(value = "/instMetric")
//    public ModelAndView addInstMetricPage(@RequestParam(required = false, defaultValue = "-1") int instMetricId, @RequestParam(required = false, defaultValue = "-1") int hostId) throws SQLException {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("InstMetric");
//        modelAndView.addObject("username", SecurityContextHolder.getContext().getAuthentication().getName());
//        if (instMetricId > 0) {
//            InstanceMetric instanceMetric = metricStorage.getInstMetric(instMetricId);
//            TemplateMetric templateMetric = metricStorage.getTemplateMetric(instanceMetric.getTempMetrcId());
//            modelAndView.addObject("InstanceMetric", instanceMetric);
//            modelAndView.addObject("templateMetric", templateMetric);
//            modelAndView.addObject("host", hosts.get(instanceMetric.getHostId()));
//        }
//        if (hostId > 0) {
//            modelAndView.addObject("host", hosts.get(hostId));
//        }
//        return modelAndView;
//    }

    @RequestMapping(value = "/getInstTempHost")
    @ResponseBody
    public InstTemplHostRow getInstTempHost(@RequestParam(required = false, defaultValue = "-1") int instMetricId) throws SQLException {
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
        HostsTemplMetricsRow hostsTemplMetricsRow = new HostsTemplMetricsRow();
        hostsTemplMetricsRow.setHosts(hosts.getAll());
        hostsTemplMetricsRow.setTemplateMetrics(metricStorage.getTemplatMetrics());
        return hostsTemplMetricsRow;
    }

    @RequestMapping(value = "/saveNewInstMetric", method = RequestMethod.GET)
    public void addInstMetric(@RequestParam(required = false, defaultValue = "-1") int templId, @RequestParam(required = false, defaultValue = "-1") int hostId, @RequestParam(required = false, defaultValue = "Err") String title, @RequestParam(required = false, defaultValue = "Err") String command, @RequestParam(required = false, defaultValue = "0") double minValue, @RequestParam(required = false, defaultValue = "0") double maxValue) throws SQLException {
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
        metricStorage.editInstMetric(id, hostId, templId, title, command, minValue, maxValue);
    }


    @RequestMapping(value = "/moveToInstMetric", method = RequestMethod.GET)
    @ResponseBody
    public MetricsRow addInstMetric(@RequestParam("hostid") int hostid, @RequestParam("templMetricid") int templMetricid) throws SQLException {
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
        MetricsRow metrics = new MetricsRow();
        metrics.setHostId(hostid);
        metrics.setInstanceMetrics(getMetrics(hostid));
        metrics.setTemplateMetrics(metricStorage.getTemplatMetrics());
        return metrics;
    }
}
