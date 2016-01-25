package net.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Kromnikov on 24.01.2016.
 */
@Controller
//@EnableAutoConfiguration
public class IndexController {

//    private IMetricStorage metricStorage;
//
//    private HostService hosts;
//
//    @Autowired
//    public IndexController(IMetricStorage metricStorage,HostService hosts) {
//        this.hosts=hosts;
//        this.metricStorage=metricStorage;
//    }

    @RequestMapping("/")
    String index(String name, Model model) {
        model.addAttribute("name", "afwawf");
        return "index";
    }
}
