package com.ui;

import com.core.interfaces.db.IMetricStorage;
import com.core.models.Value;
import com.ui.tools.TypeOfMetric;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.ApplicationFrame;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by ANTON on 06.12.2015.
 */
public class Chart extends JFrame {

    private IMetricStorage metricStorage;

    public Chart() {
        super("");
        set();
    }

    public Chart(final String title,int id,IMetricStorage metricStorage) throws SQLException {
        super(title);
        this.metricStorage = metricStorage;
        double value;
        TimeSeries series = new TimeSeries(title, Minute.class);
        Hour hour = new Hour();
        Chart chart = new Chart();
        int typeOfMetric = TypeOfMetric.getTypeOfMetric(title);
        int j = chart.QuantyOfRows(typeOfMetric);
        List<Value> values;
        values = metricStorage.getValues(id,typeOfMetric);
        if(values.size()>0) {
            for (int i = 1; i < j; i++) {
                value = values.get(i).getValue();
                series.add(new Minute(i, hour), value);
            }
        }
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series);

        final JFreeChart freeChart = ChartFactory.createTimeSeriesChart(
                title,
                "Time",
                "Load",
                dataset,
                true,
                true,
                false
        );
        final ChartPanel chartPanel = new ChartPanel(freeChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
        pack();
    }



    public int QuantyOfRows(int id1) throws SQLException {//TODO и убери sqlAgent'a из кода, он в SQLBranch будет =)
        int id = id1;
        int j = 10;

        return j;
    }



    public void set() {
        setDefaultCloseOperation(ApplicationFrame.HIDE_ON_CLOSE);
    }
}

