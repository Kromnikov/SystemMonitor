package ui;
import core.agents.SQL.SQLAgent;
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

public class Chart extends ApplicationFrame {

    private static SQLAgent sqlAgent;
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
        ResultSet resultSet = chart.getData(typeOfMetric);
        int j = chart.QuantyOfRows(typeOfMetric);
        for (int i=1;i<j;i++){
            resultSet.next();
            value=Double.parseDouble(resultSet.getString(1));
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
        if(sql.load()) {
            sqlAgent = new SQLAgent(sql.getStatement());
//            resultSet=sqlAgent.getAllValue(id);//1- id для получения значений загруженности СРU.
        }
        return resultSet;
    }
    public int QuantyOfRows (int id1) throws SQLException {//TODO и убери sqlAgent'a из кода, он в SQLBranch будет =)
        int id=id1;
        int j=10;
        SQLConfiguration sql = new SQLConfiguration();
        if(sql.load()) {
            sqlAgent = new SQLAgent(sql.getStatement());
//            j=sqlAgent.getQuantityOfRow(id);
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