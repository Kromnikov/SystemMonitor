package core.ui;

import core.SpringService;
import core.hibernate.services.HostService;
import core.interfaces.db.IMetricStorage;
import core.models.TemplateMetric;

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
    HostService hosts = SpringService.getHosts();

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
        List<String> hosts;
//        hosts=metricStorage.getListIP(); //список хостов
//        for (String host:hosts) {
//            listModel.addElement(host);
//        }
        final JList list = new JList(listModel);//получаем лист хостов
        list.setSelectedIndex(0);
        list.setFocusable(false);
        //для каждого хоста выводим его метрики
        //
        //
        final DefaultListModel listModelMetric = new DefaultListModel();
        List<TemplateMetric> templateMetrics;
        i = list.getSelectedIndex();
        String host = (String) listModel.get(i);
        int id = metricStorage.getHostIDbyTitle(host);
//        templateMetrics =metricStorage.getMetricsByHostId(id);
//        for (TemplateMetric templateMetric : templateMetrics){
//            listModelMetric.addElement(templateMetric.getTitle());
//        }
        final JList listmetric = new JList(listModelMetric);
        listmetric.setSelectedIndex(0);
        listmetric.setFocusable(false);
        list.setBounds(1,1,125,200);
        listmetric.setBounds(126,1,125,200);
        //список всех метрик на добавление
        //
        //
        final DefaultListModel listModelToAddMetrics = new DefaultListModel();
        List<TemplateMetric> metricsAdd;
        metricsAdd=metricStorage.geAllTemplatMetrics();
        for (TemplateMetric templateMetric : metricsAdd){
            listModelToAddMetrics.addElement(templateMetric.getTitle());
        }
        final JList listAllMetric = new JList(listModelToAddMetrics);
        listAllMetric.setSelectedIndex(0);
        listAllMetric.setFocusable(false);
        listAllMetric.setBounds(250,1,125,200);
        mainPanel.add(list);
        mainPanel.add(listmetric);
        mainPanel.add(listAllMetric);


        final JButton removeButton = new JButton("Delete");
        removeButton.setFocusable(false);
        final JButton removeMetricButton = new JButton("Delete");
        removeMetricButton.setFocusable(false);
        final JButton addButton = new JButton("Add");
        removeButton.setFocusable(false);
        removeButton.setBounds(0,200,125,30);
        removeMetricButton.setBounds(125,200,125,30);
        addButton.setBounds(250,200,125,30);
        mainPanel.add(removeButton);
        mainPanel.add(removeMetricButton);
        mainPanel.add(addButton);
        getContentPane().add(mainPanel);

        setPreferredSize(new Dimension(375, 265));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);



        //Listners
//        removeButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                String host;
//                int i=list.getSelectedIndex();
//                host = (String) listModel.get(i);
//                try {
//                    metricStorage.delHost(host);
//                } catch (SQLException e1) {
//                    e1.printStackTrace();
//                    JOptionPane.showMessageDialog( mainPanel, "This host has not been added");
//                }
//                listModel.remove(list.getSelectedIndex());
//            }
//        });
        removeMetricButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String metric;
                int i=listmetric.getSelectedIndex();
                metric = (String) listModelMetric.get(i);
                try {
                    int id =metricStorage.getTemplatMetricID(metric);
                    metricStorage.delMetricFromHost(id);
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
                    int metricID = metricStorage.getTemplatMetricID(metric);
                    int hostID = metricStorage.getHostIDbyTitle(host);
                    metricStorage.addMetricToHost(hostID,metricID);

                } catch (SQLException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog( mainPanel, "This metric has not been added");
                }
                listModelMetric.addElement(metric);

            }
        });

        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                String host;
                int id;
                int i = list.getSelectedIndex();
                host = (String) listModel.get(i);
                List<TemplateMetric> templateMetrics = null;
                try {
                    id = metricStorage.getHostIDbyTitle(host);
//                    templateMetrics = metricStorage.getMetricsByHostId(id);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
                listModelMetric.removeAllElements();
                for (TemplateMetric templateMetric : templateMetrics){
                    listModelMetric.addElement(templateMetric.getTitle());
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

