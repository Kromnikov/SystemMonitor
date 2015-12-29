package com.ui;

import com.core.interfaces.db.IMetricStorage;
import com.core.models.InstanceMetric;
import com.core.models.Value;
import com.xeiam.xchart.*;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.*;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StackedXYBarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.data.time.*;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.VerticalAlignment;
import org.jfree.util.Rotation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class ChartMetric extends JFrame {
    private IMetricStorage metricStorage;
    private String title;
    private int hostId;
    private String drawMedoth = "Last20Rows";
    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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

    private void draw() throws SQLException {
        InstanceMetric instanceMetric = metricStorage.getInstMetric(hostId, title);
        List<Value> values;
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String appeningTitle="";
        CategoryLabelPositions cp = CategoryLabelPositions.STANDARD;
        switch (drawMedoth) {
            case "Last20Rows":
                appeningTitle=" (Last20Rows)";
                cp = CategoryLabelPositions.UP_90;
                values = metricStorage.getValuesLastTwentyRec(hostId, instanceMetric.getId());
                if(values.size()>0) {
                    for (int i = 0; i < values.size(); i++) {
                        dataset.addValue(values.get(i).getValue(), "Last20Rows", dateFormat.format(values.get(i).getDateTime()));
                    }
                    break;
                }
            case "LastMinute":
                appeningTitle=" (LastMinute)";
                values = metricStorage.getValuesLastMinets(hostId, instanceMetric.getId(), new Date());
                if(values.size()>0) {
                    for (int i = 0; i < values.size(); i++) {
                        dataset.addValue(values.get(i).getValue(), "LastMinute", values.get(i).getDateTime().getMinutes()+":"+values.get(i).getDateTime().getSeconds());
                    }
                    break;
                }
            case "LastHour":
                appeningTitle=" (LastHour)";
                values = metricStorage.getValuesLastHour(hostId, instanceMetric.getId(), new Date());
                if(values.size()>10)
                    cp = CategoryLabelPositions.UP_90;
                if(values.size()>0) {
                    for (int i = 0; i < values.size(); i++) {
                        dataset.addValue(values.get(i).getValue(), "LastHour", values.get(i).getDateTime().getHours()+":"+values.get(i).getDateTime().getMinutes());
                    }
                    break;
                }
            case "LastDay":
                appeningTitle=" (LastDay)";
                values = metricStorage.getValuesLastDay(hostId, instanceMetric.getId(), new Date());
                if(values.size()>10)
                    cp = CategoryLabelPositions.UP_90;
                if(values.size()>0) {
                    for (int i = 0; i < values.size(); i++) {
                        dataset.addValue(values.get(i).getValue(), "LastDay", values.get(i).getDateTime().getDay()+" "+values.get(i).getDateTime().getHours()+":"+values.get(i).getDateTime().getMinutes());
                    }
                    break;
                }
            case "LastWeek":
                appeningTitle=" (LastWeek)";
                values = metricStorage.getValuesLastWeek(hostId, instanceMetric.getId(), new Date());
                if(values.size()>10)
                    cp = CategoryLabelPositions.UP_90;
                if(values.size()>0) {
                    for (int i = 0; i < values.size(); i++) {
                        dataset.addValue(values.get(i).getValue(), "LastWeek", dateFormat.format(values.get(i).getDateTime()));
                    }
                    break;
                }
            case "LastMonth":
                appeningTitle=" (LastMonth)";
                values = metricStorage.getValuesLastMonth(hostId, instanceMetric.getId(), new Date());
                if(values.size()>10)
                    cp = CategoryLabelPositions.UP_90;
                if(values.size()>0) {
                    for (int i = 0; i < values.size(); i++) {
                        dataset.addValue(values.get(i).getValue(), "LastMonth", values.get(i).getDateTime().getMonth()+"."+values.get(i).getDateTime().getDay());
                    }
                    break;
                }
            case "LastYear":
                appeningTitle=" (LastYear)";
                values = metricStorage.getValuesLastYear(hostId, instanceMetric.getId(), new Date());
                if(values.size()>10)
                    cp = CategoryLabelPositions.UP_90;
                if(values.size()>0) {
                    for (int i = 0; i < values.size(); i++) {
                        dataset.addValue(values.get(i).getValue(), "LastYear", values.get(i).getDateTime().getYear()+"."+values.get(i).getDateTime().getMonth());
                    }
                    break;
                }
        }
        JFreeChart freeChart = ChartFactory.createLineChart(
                title+appeningTitle,
                "Date", "Values",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false);
        appeningTitle="";
        CategoryAxis axis = freeChart.getCategoryPlot().getDomainAxis();
        axis.setCategoryLabelPositions(cp);

        final ChartPanel chartPanel = new ChartPanel(freeChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
        pack();
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

