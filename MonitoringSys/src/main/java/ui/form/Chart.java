package ui.form;
import core.models.Value;
import core.branches.SQLBranch;
import core.configurations.SQLConfiguration;
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

public class Chart extends ApplicationFrame {

    public Chart(){
        super("");
        setDefaultCloseOperation(ApplicationFrame.HIDE_ON_CLOSE);
    }

    public Chart(final String title) throws SQLException {
        super(title);
        setDefaultCloseOperation(ApplicationFrame.HIDE_ON_CLOSE);
        double value;
        TimeSeries series = new TimeSeries(title, Minute.class);
        Hour hour = new Hour();
        Chart chart = new Chart();
        int typeOfMetric = chart.getTypeOfMetric(title);

        List<Value> values = SQLBranch.getValues(1, typeOfMetric);
        for (Value val : values) {
            series.add(new Minute(val.getId(), hour), val.getValue());
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

    public List<Double> getData(int id) throws SQLException {
        return SQLBranch.getAllValueMetricOnHost(id);
    }
    public int QuantyOfRows (int id1) throws SQLException {
        int id=id1;
        int j=10;
        SQLConfiguration sql = new SQLConfiguration();
        if(sql.load()) {
            j= SQLBranch.getQuantityOfRow(id);
        }
        return j;
    }
    public int getTypeOfMetric(String title){
        int k=0;
        switch (title){
            case "CPU":k=1;break;
            case "Disc":k=2;break;
            case "Memory":k=5;break;
        }
        return k;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ApplicationFrame.setDefaultLookAndFeelDecorated(true);
                new Chart();

            }
        });
    }
}