package ui.form;
import core.agents.sql.SQLAgent;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Chart extends JFrame {

    private static SQLAgent sqlAgent;

    public Chart() {
        super("");
        set();
    }

    public Chart(final String title,int id) throws SQLException {
        super(title);
        double value;
        TimeSeries series = new TimeSeries(title, Minute.class);
        Hour hour = new Hour();
        Chart chart = new Chart();
        int typeOfMetric = chart.getTypeOfMetric(title);
        ResultSet resultSet = chart.getData(typeOfMetric);
        int j = chart.QuantyOfRows(typeOfMetric);
        for (int i = 1; i < j; i++) {
            resultSet.next();
            value = Double.parseDouble(resultSet.getString(id));
            series.add(new Minute(i, hour), value);
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

    public ResultSet getData(int id) throws SQLException {//TODO Допиши методы в sqlAgent и потом в SQLBranch и юзай из SQLBranch их
        ResultSet resultSet = null;
        SQLConfiguration sql = new SQLConfiguration();
        if (sql.load()) {
            sqlAgent = new SQLAgent(sql.getStatement());
            resultSet = sqlAgent.getAllValue(id);//1- id для получения значений загруженности СРU.
        }
        return resultSet;
    }

    public int QuantyOfRows(int id1) throws SQLException {//TODO и убери sqlAgent'a из кода, он в SQLBranch будет =)
        int id = id1;
        int j = 10;
        SQLConfiguration sql = new SQLConfiguration();
        if (sql.load()) {
            sqlAgent = new SQLAgent(sql.getStatement());
//            j=sqlAgent.getQuantityOfRow(id);
        }
        return j;
    }

    public int getTypeOfMetric(String title) {
        int k = 0;
        switch (title) {
            case "getCPU":
                k = 1;
                break;
            case "getFreeDiskMb":
                k = 2;
                break;
            case "getUsedDiskMb":
                k = 3;
                break;
            case "getTotalDiskMb":
                k = 4;
                break;
            case "getFreeRAM":
                k = 5;
                break;
            case "getUsedRAM":
                k = 6;
                break;
            case "getTotalRAM":
                k = 7;
                break;

        }
        return k;
    }

    public void set() {
        setDefaultCloseOperation(ApplicationFrame.HIDE_ON_CLOSE);
    }
}
