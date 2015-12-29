package com.ui;

import com.core.interfaces.db.IMetricStorage;
import com.core.models.InstanceMetric;
import com.core.models.Value;
import com.xeiam.xchart.Chart;
import com.xeiam.xchart.QuickChart;
import com.xeiam.xchart.Series;
import com.xeiam.xchart.SeriesMarker;



import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.util.Rotation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.*;
import java.util.List;

/**
 * Created by ANTON on 06.12.2015.
 */
public class ChartMetric extends JFrame {
    private IMetricStorage metricStorage;
    private String title;
    private int hostId;
    private String drawMedoth = "Last20Rows";

    public ChartMetric(IMetricStorage metricStorage) {
        super("");
        this.metricStorage = metricStorage;
        setDefaultCloseOperation(ApplicationFrame.HIDE_ON_CLOSE);
    }

    public ChartMetric(final String title, int id, IMetricStorage metricStorage) throws SQLException {
        super(title);
        this.title= title;
        this.hostId = id;
        this.metricStorage = metricStorage;
        menu();
        draw();
    }

    private void draw1() throws SQLException {
        final XYSeries series = new XYSeries(title);
        InstanceMetric instanceMetric = metricStorage.getInstMetric(hostId, title);

//        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date dateTime = new Date();
//        try {
//            dateTime = (dateFormat.parse("2015-12-29 15:24:38"));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        List<Value> values;

        switch (drawMedoth) {
            case "Last20Rows":
                values = metricStorage.getValuesLastTwentyRec(hostId, instanceMetric.getId());
                if(values.size()>0) {
                    for (int i = 0; i < values.size(); i++) {
                        series.add(((values.get(i).getDateTime().getMinutes()) + ((double) values.get(i).getDateTime().getSeconds() / 100)), values.get(i).getValue());
                    }
                    break;
                }
            case "LastMinute":
                values = metricStorage.getValuesLastMinets(hostId, instanceMetric.getId(), new Date());
                if(values.size()>0) {
                    for (int i = 0; i < values.size(); i++) {
                        series.add(((values.get(i).getDateTime().getMinutes()) + ((double) values.get(i).getDateTime().getSeconds() / 100)), values.get(i).getValue());
                    }
                    break;
                }
        }



        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);

        final JFreeChart freeChart = ChartFactory.createXYLineChart(
                title,
                "Time",
                "Load",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                true
        );
        final ChartPanel chartPanel = new ChartPanel(freeChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
        pack();
    }

    private void draw() throws SQLException {
        InstanceMetric instanceMetric = metricStorage.getInstMetric(hostId, title);
        List<Double> xData = new ArrayList<Double>();
        List<Double> yData = new ArrayList<Double>();
        List<Value> values;
        switch (drawMedoth) {
            case "Last20Rows":
                values = metricStorage.getValuesLastTwentyRec(hostId, instanceMetric.getId());
                if(values.size()>0) {
                    for (int i = 0; i < values.size(); i++) {
                        //series.add(new Day(values.get(i).getDateTime()), values.get(i).getValue());
                        xData.add((double)i);
                        yData.add(values.get(i).getValue());
                    }
                    break;
                }
            case "LastMinute":
                values = metricStorage.getValuesLastMinets(hostId, instanceMetric.getId(), new Date());
                if(values.size()>0) {
                    for (int i = 0; i < values.size(); i++) {
                    }
                    break;
                }
        }


        //Chart chart = QuickChart.getChart("chart", "x", "y", "null", xData, yData);


//        final ChartPanel chartPanel = new ChartPanel(freeChart);
//        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
//        setContentPane(chartPanel);
//        pack();
    }

    private void menu() {
        Font font = new Font("Times New Roman", Font.PLAIN, 14);
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Draw method");
        fileMenu.setFont(font);

        JMenuItem conItem = new JMenuItem("Last 20 rows");
        conItem.setFont(font);
        fileMenu.add(conItem);
        conItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    drawMedoth = "Last20Rows";
                    draw();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        JMenuItem conItem1 = new JMenuItem("Last minute");
        conItem1.setFont(font);
        fileMenu.add(conItem1);
        conItem1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    drawMedoth = "LastMinute";
                    draw();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        JMenuItem conItem2 = new JMenuItem("Last hour");
        conItem2.setFont(font);
        fileMenu.add(conItem2);
        conItem2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    drawMedoth = "LastHour";
                    draw();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        JMenuItem conItem3 = new JMenuItem("Last day");
        conItem3.setFont(font);
        fileMenu.add(conItem3);
        conItem3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    drawMedoth = "LastDay";
                    draw();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        JMenuItem conItem4 = new JMenuItem("Last week");
        conItem4.setFont(font);
        fileMenu.add(conItem4);
        conItem4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    drawMedoth = "LastWeek";
                    draw();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        JMenuItem conItem5 = new JMenuItem("Last month");
        conItem5.setFont(font);
        fileMenu.add(conItem5);
        conItem5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    drawMedoth = "LastMonth";
                    draw();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        JMenuItem conItem6 = new JMenuItem("Last year");
        conItem6.setFont(font);
        fileMenu.add(conItem6);
        conItem6.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    drawMedoth = "LastYear";
                    draw();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        menuBar.add(fileMenu);
        this.setJMenuBar(menuBar);
    }
}

