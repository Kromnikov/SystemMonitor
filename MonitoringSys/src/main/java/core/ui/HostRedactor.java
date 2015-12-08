package core.ui;

import core.SpringService;
import core.configurations.SSHConfiguration;
import core.hibernate.services.HostService;
import core.interfaces.db.IMetricStorage;
import core.models.Metric;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by ANTON on 06.12.2015.
 */
public class HostRedactor extends JFrame {
    Object[] res = new Object[5];
    IMetricStorage metricStorage = SpringService.getMetricStorage();
    HostService hostsser = SpringService.getHosts();

    public HostRedactor() throws SQLException {
        super("Host Redactor");
        createGUI();
    }


    public void createGUI() throws SQLException {
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        final JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        int i;
        final DefaultListModel listModel = new DefaultListModel();
        List<SSHConfiguration> hosts;
        hosts=hostsser.getAll(); //список хостов
        for (SSHConfiguration host:hosts) {
            listModel.addElement(host.getHost());
        }
        final JList list = new JList(listModel);//получаем лист хостов
        list.setSelectedIndex(0);
        list.setFocusable(false);
        //для каждого хоста выводим его метрики
        //
        //
        final DefaultListModel listModelMetric = new DefaultListModel();
        List<Metric> metrics;
        i = list.getSelectedIndex();
        String host = (String) listModel.get(i);
        int id = metricStorage.getHostIDbyTitle(host);
        metrics=metricStorage.getMetricsByHostId(id);
        for (Metric metric: metrics){
            listModelMetric.addElement(metric.getTitle());
        }
        final JList listmetric = new JList(listModelMetric);
        listmetric.setSelectedIndex(0);
        listmetric.setFocusable(false);
        //список всех метрик на добавление
        //
        //
        final DefaultListModel listModelToAddMetrics = new DefaultListModel();
        List<Metric> metricsAdd;
        metricsAdd=metricStorage.geAllMetrics();
        for (Metric metric: metricsAdd){
            listModelToAddMetrics.addElement(metric.getTitle());
        }
        final JList listAllMetric = new JList(listModelToAddMetrics);
        listAllMetric.setSelectedIndex(0);
        listAllMetric.setFocusable(false);




        final JButton removeButton = new JButton("-");
        removeButton.setFocusable(false);
        final JButton addHostButton = new JButton("+");
        removeButton.setFocusable(false);
        final JButton updateButton = new JButton("Update");
        final JButton removeMetricButton = new JButton(">");
        removeMetricButton.setFocusable(false);
        final JButton addButton = new JButton("<");
        removeButton.setFocusable(false);
        JLabel labelListOfHosts = new JLabel("Hosts");
        JLabel labelMetricHosts = new JLabel("Host's Metrics");
        JLabel labelMetricsToAdd = new JLabel("Metrics to add");

// Расположение всех элементов и наполнение ими формы
        labelListOfHosts.setBounds(5,1,60,20);
        list.setBounds(1,20,125,200);
        labelMetricHosts.setBounds(130,1,120,20);
        listmetric.setBounds(130,20,125,200);
        labelMetricsToAdd.setBounds(305,1,100,20);
        listAllMetric.setBounds(305,20,150,200);
        removeButton.setBounds(0,220,60,30);
        addHostButton.setBounds(70,220,60,30);
        updateButton.setBounds(135,220,100,30);
        removeMetricButton.setBounds(255,90,50,25);
        addButton.setBounds(255,130,50,25);
        mainPanel.add(list);
        mainPanel.add(listmetric);
        mainPanel.add(listAllMetric);
        mainPanel.add(removeButton);
        mainPanel.add(removeMetricButton);
        mainPanel.add(addButton);
        mainPanel.add(addHostButton);
        mainPanel.add(labelListOfHosts);
        mainPanel.add(labelMetricHosts);
        mainPanel.add(labelMetricsToAdd);
        //mainPanel.add(updateButton);
        getContentPane().add(mainPanel);

        setPreferredSize(new Dimension(470, 285));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);



        //Listners
        addHostButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ConForm().setVisible(true);
            }
        });
        removeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String host;
                int i=list.getSelectedIndex();
                host = (String) listModel.get(i);
                try {
                    metricStorage.delHost(host);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog( mainPanel, "This host has not been added");
                }
                listModel.remove(list.getSelectedIndex());
            }
        });
        removeMetricButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String metric;
                int i=listmetric.getSelectedIndex();
                metric = (String) listModelMetric.get(i);
                try {
                    int id =metricStorage.getMetricID(metric);
                    String hostname = (String) listModel.get(i);
                    int host = metricStorage.getHostIDbyTitle(hostname);
                    metricStorage.delMetricFromHost(host,id);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog( mainPanel, "This metric has not been deleted");
                }
                listModelMetric.remove(listmetric.getSelectedIndex());
            }
        });
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String metric;
                String host;
                int i=listAllMetric.getSelectedIndex();
                int j = list.getSelectedIndex();
                metric = (String) listModelToAddMetrics.get(i);
                host = (String) listModel.get(j);
                try {
                    int metricID = metricStorage.getMetricID(metric);
                    int hostID = metricStorage.getHostIDbyTitle(host);
                    metricStorage.addMetricToHost(hostID,metricID);
                    JOptionPane.showMessageDialog( mainPanel, "This metric has been added!");

                } catch (SQLException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog( mainPanel, "This metric has not been added!");
                }
                listModelMetric.addElement(metric);

            }
        });

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {


            }
        });

        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                String host;
                int id;
                int i = list.getSelectedIndex();
                host = (String) listModel.get(i);
                List<Metric> metrics = null;
                try {
                    id = metricStorage.getHostIDbyTitle(host);
                    metrics= metricStorage.getMetricsByHostId(id);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                listModelMetric.removeAllElements();
                for (Metric metric: metrics){
                    listModelMetric.addElement(metric.getTitle());
                }
                listmetric.setSelectedIndex(0);
            }
        });

        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (list.getSelectedIndex() >= 0) {
                    removeButton.setEnabled(true);
                } else {
                    removeButton.setEnabled(false);
                }
                if (listmetric.getSelectedIndex() >= 0) {
                    removeMetricButton.setEnabled(true);
                } else {
                    removeMetricButton.setEnabled(false);
                }
            }
        });
        listmetric.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (listmetric.getSelectedIndex() >= 0) {
                    removeMetricButton.setEnabled(true);
                } else {
                    removeMetricButton.setEnabled(false);
                }
            }
        });
    }

}

