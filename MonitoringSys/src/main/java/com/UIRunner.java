package com;

import com.core.hibernate.services.HostService;
import com.core.interfaces.db.IMetricStorage;
import com.ui.MainForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.sql.SQLException;

@Service("UIRunner")
public class UIRunner {

    private IMetricStorage metricStorage;

    private HostService hosts;

    @Autowired
    public UIRunner(final IMetricStorage metricStorage, final HostService hosts) {
        this.hosts=hosts;
        this.metricStorage=metricStorage;
    }

    public UIRunner() {
    }
    @PostConstruct
    public void run() {
        MainForm frame = null;
        try {
            frame = new MainForm(this.metricStorage,this.hosts);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.createmenu(frame);
    }
}
