package com.ui;

import com.core.MetricStorage;
import com.core.models.TemplateMetric;
import org.springframework.beans.factory.annotation.Autowired;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by ANTON on 06.12.2015.
 */
public class MetricRedactor extends JFrame {
    private int i = 0;
    @Autowired
    MetricStorage metricStorage;
    public MetricRedactor() throws SQLException {
        super("Metric's redactor");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        createGUI();
    }

    //--------Кнопки и текстбоксы для ввода информации
    public void createGUI() throws SQLException {
        final JPanel panel = new JPanel();
        JLabel jLabel1 = new JLabel("Command");
        jLabel1.setBounds(10, 10, 100, 20);
        panel.add(jLabel1);
        final JTextField jTextMetricCommand = new JTextField();
        jTextMetricCommand.setBounds(80, 10, 140, 20);
        panel.add(jTextMetricCommand);
        panel.setLayout(null);


        JLabel jLabel2 = new JLabel("Title");
        jLabel2.setBounds(10, 35, 100, 20);
        panel.add(jLabel2);
        final JTextField jTextMetricTitle = new JTextField();
        jTextMetricTitle.setBounds(80, 35, 140, 20);
        panel.add(jTextMetricTitle);
        panel.setLayout(null);

        JButton button1 = new JButton("Add");
        button1.setBounds(10, 60, 100, 30);
        panel.add(button1);
        JButton button2 = new JButton("Del");
        button2.setBounds(120, 60, 100, 30);
        panel.add(button2);

        JLabel label1 = new JLabel("Title");
        JLabel label2 = new JLabel("Command");
        label1.setBounds(10, 100, 100, 20);
        label2.setBounds(120, 100, 100, 20);
        panel.add(label1);
        panel.add(label2);
//--------Список названий метрик
        final DefaultListModel listModel = new DefaultListModel();
        List<TemplateMetric> metricList;
        metricList = metricStorage.getTemplatMetrics();
        for (TemplateMetric m : metricList) {
            listModel.addElement(m.getTitle());
        }
        final JList list = new JList(listModel);
        list.setSelectedIndex(0);
        list.setFocusable(false);
        list.setBounds(10, 120, 100, 200);
        panel.add(list);


        final DefaultListModel listModel1 = new DefaultListModel();
        List<TemplateMetric> metricList1;
        metricList = metricStorage.getTemplatMetrics();
        for (TemplateMetric m : metricList) {
            listModel1.addElement(m.getCommand());
        }
        final JList list1 = new JList(listModel1);
        list.setSelectedIndex(0);
        list.setFocusable(false);
        list1.setBounds(120, 120, 100, 200);
        panel.add(list1);
        getContentPane().add(panel);

        getContentPane().add(panel);
        setPreferredSize(new Dimension(270, 300));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);


//---------------Listners
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String comTitle;
                String comCommand;
                try {
                    comTitle = jTextMetricTitle.getText();
                    comCommand = jTextMetricCommand.getText();
                    metricStorage.addTemplateMetric(comTitle, comCommand);
                    listModel.addElement(comTitle);
                    listModel1.addElement(comCommand);

                } catch (SQLException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(panel, "This metric command has not been added");
                }

            }
        });

        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String title;
                int i = list.getSelectedIndex();
                title = (String) listModel.get(i);
                try {
                    int id = metricStorage.getTemplatMetricID(title);
                    metricStorage.delMetricFromHost(id);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                    JOptionPane.showMessageDialog(panel, "This host has not been added");
                }
                int j = list.getSelectedIndex();
                listModel.remove(j);
                listModel1.remove(j);
            }
        });
    }




    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame.setDefaultLookAndFeelDecorated(true);
                try {
                    new MetricRedactor();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });
    }}


