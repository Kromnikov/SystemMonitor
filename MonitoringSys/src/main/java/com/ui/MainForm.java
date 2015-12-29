package com.ui;

import com.core.hibernate.services.HostService;
import com.core.interfaces.db.IMetricStorage;
import com.core.models.InstanceMetric;
import com.core.models.Value;
import core.configurations.SSHConfiguration;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;


public class MainForm extends JFrame {


    private JPanel MForm;

    private IMetricStorage metricStorage;

    private HostService hosts;


    public MainForm(IMetricStorage metricStorage,HostService hosts) throws SQLException {
        super("Monitoring");
        this.hosts = hosts;
        this.metricStorage = metricStorage;
        final JPanel panel = new JPanel();
        createDesign(panel);
    }
    public void createmenu(MainForm frame) {

        Font font = new Font("Times New Roman", Font.PLAIN, 14);
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(font);

        JMenuItem conItem = new JMenuItem("Connect");
        conItem.setFont(font);
        fileMenu.add(conItem);
        conItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ConForm(metricStorage,hosts).setVisible(true);
            }
        });

        JMenuItem editItem = new JMenuItem("Edit Config");
        editItem.setFont(font);
        fileMenu.add(editItem);
        editItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    new HostRedactor(metricStorage,hosts).setVisible(true);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        final JMenuItem metricItem = new JMenuItem("Edit Metric");
        metricItem.setFont(font);
        fileMenu.add(metricItem);
        metricItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    new MetricRedactor(metricStorage).setVisible(true);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });


        fileMenu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setFont(font);
        fileMenu.add(exitItem);

        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        JMenu refMenu = new JMenu("Reference");
        refMenu.setFont(font);
        JMenuItem helpItem = new JMenuItem("Help");
        helpItem.setFont(font);
        refMenu.add(helpItem);

        JMenuItem abpItem = new JMenuItem("About Program");
        abpItem.setFont(font);
        refMenu.add(abpItem);

        menuBar.add(fileMenu);
        menuBar.add(refMenu);
        frame.setJMenuBar(menuBar);
    }


    public void createDesign(final JPanel panel) throws SQLException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel.setLayout(null);

        JButton button4= new JButton("View chart");
        button4.setBounds(0, 320, 180, 30);
        panel.add(button4);

        JLabel label1 = new JLabel("Hosts");
        JLabel label2 =new JLabel("Metrics");
        label1.setBounds(0,0,100,20);
        label2.setBounds(0,160,100,20);
        panel.add(label1);
        panel.add(label2);

        int i;
        panel.setLayout(null);
        final DefaultListModel listModel = new DefaultListModel();

        for (SSHConfiguration host:hosts.getAll()) {
            listModel.addElement(host.getHost());
        }
        final JList list = new JList(listModel);//получаем лист хостов
        list.setSelectedIndex(0);
        list.setFocusable(false);
        //для каждого хоста выводим его метрики
        //
        //
        final DefaultListModel listModelMetric = new DefaultListModel();
        final List<InstanceMetric> metrics;
        i = list.getSelectedIndex();
        String host = (String) listModel.get(i);
        int id = metricStorage.getHostIDbyTitle(host);
        metrics=metricStorage.getInstMetrics(id);
        for (InstanceMetric metric: metrics){
            listModelMetric.addElement(metric.getTitle());
        }
        final JList listmetric = new JList(listModelMetric);
        listmetric.setSelectedIndex(0);
        listmetric.setFocusable(false);
        list.setBounds(0,20,180,140);
        listmetric.setBounds(0,180,180,140);

        panel.add(list);
        panel.add(listmetric);

        JLabel labelInfo = new JLabel("INFORMATION");
        labelInfo.setBounds(300,0,100,40);
        panel.add(labelInfo);
        panel.add(MainForm.drawChar(metricStorage,1));
//-----------------------раздел Information
        Icon iconOK = UIManager.getIcon("OptionPane.informationIcon");
        Icon iconWrong = UIManager.getIcon("OptionPane.errorIcon");
        Icon iconCheck = UIManager.getIcon("OptionPane.questionIcon");
        JLabel label3 = new JLabel();
        label3.setForeground(Color.GREEN);
        label3.setText("Host's status information");
        label3.setIcon(iconOK);

        JLabel label4 = new JLabel();
        label4.setForeground(Color.GREEN);
        label4.setText("Metric's status information");
        label4.setIcon(iconCheck);

        JLabel label5 = new JLabel();
        label5.setForeground(Color.ORANGE);
        label5.setText("Metric's state information");
        label5.setIcon(iconWrong);
        label3.setBounds(220,40,200,30);
        label4.setBounds(220,70,200,30);
        label5.setBounds(220,100,200,30);
        panel.add(label3);
        panel.add(label4);
        panel.add(label5);

//-----------------------Listners
        getContentPane().add(panel);
        setPreferredSize(new Dimension(500, 405));

        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                String host;
                int id;
                int i = list.getSelectedIndex();
                host = (String) listModel.get(i);
                List<InstanceMetric> metrics = null;
                try {
                    id = metricStorage.getHostIDbyTitle(host);
                    metrics=metricStorage.getInstMetrics(id);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                listModelMetric.removeAllElements();
                for (InstanceMetric metric: metrics){
                    listModelMetric.addElement(metric.getTitle());
                }
                listmetric.setSelectedIndex(0);
            }
        });



        button4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String title;
                    int idhost;
                    String host;
                    int indexmetric=listmetric.getSelectedIndex();
                    title = (String)listModelMetric.get(indexmetric);
                    int indexhost=list.getSelectedIndex();
                    host = (String) listModel.get(indexhost);
                    idhost = metricStorage.getHostIDbyTitle(host);
                    new ChartMetric(title,idhost,metricStorage).setVisible(true);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog( panel, "Chart is not available");
                }
            }
        });

    }

    public static ChartPanel drawChar(IMetricStorage metricStorage, int metricID) throws SQLException {
        double value;
        TimeSeries series = new TimeSeries("Metric", Minute.class);
        Hour hour = new Hour();
        ChartMetric chart = new ChartMetric(metricStorage);
        List<Value> values;
        values = metricStorage.getValues(1,metricID);
        if(values.size() >0) {
            for (int i = 1; i < 10; i++) {
                value = values.get(i).getValue();
                series.add(new Minute(i, hour), value);
            }
        }
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(series);

        final JFreeChart freeChart = ChartFactory.createTimeSeriesChart(
                "Metric",
                "Time",
                "Load",
                dataset,
                true,
                true,
                false
        );
        final ChartPanel chartPanel = new ChartPanel(freeChart);
        chartPanel.setBounds(180,160,310,215);
        return chartPanel;

    }

}

