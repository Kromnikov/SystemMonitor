package com.ui;


import com.core.interfaces.db.IMetricStorage;
import com.core.models.TableModel;
import org.jfree.ui.ApplicationFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HostsTables extends JFrame {

    private IMetricStorage metricStorage;

    private JTable table;
    private JPanel mainPanel ;
    private JPanel headPanel ;
    private JPanel studentsPanel;
    private JPanel tablePanel ;


    public HostsTables(){
        super("Hosts states");
        setDefaultCloseOperation(ApplicationFrame.HIDE_ON_CLOSE);
        init();
    }
    public HostsTables(IMetricStorage metricStorage){
        super("Hosts states");
        setDefaultCloseOperation(ApplicationFrame.HIDE_ON_CLOSE);
        this.metricStorage = metricStorage;
        Toolkit kit = Toolkit.getDefaultToolkit();
        final int screenHeight = kit.getDefaultToolkit().getScreenSize().height;
        int screenWidth =  kit.getDefaultToolkit().getScreenSize().width;
        setSize(screenWidth / 2, screenHeight / 2);
        setLocation(screenWidth / 4, screenHeight / 4);
        init();
    }

    private void init() {
        initializationPanel();
        TableModel tableModel = metricStorage.getHostTableModel();
        table = new JTable(tableModel);
        final JScrollPane scrollTable = new JScrollPane(table);//Скрол для таблицы
        tablePanel.add(scrollTable);
        final JButton addButton = new JButton("Resolve");
        addButton.addActionListener(addButtonAction());
        headPanel.add(addButton);
        setContent();
    }
    private void setContent() {
        mainPanel.add(headPanel);
        mainPanel.add(studentsPanel);
        mainPanel.add(tablePanel);
        setContentPane(mainPanel);
    }
    private void initializationPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));
        headPanel = new JPanel();
        headPanel.setLayout(new GridLayout());
        studentsPanel = new JPanel();
        studentsPanel.setLayout(new GridLayout());
        tablePanel = new JPanel();
        tablePanel.setLayout(new GridLayout());
    }
    private ActionListener addButtonAction() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    int reply = JOptionPane.showConfirmDialog(null, "Resolve?", "Resolve", JOptionPane.YES_NO_OPTION);
                    if (reply == JOptionPane.YES_OPTION) {
                        if (table.getValueAt(table.getSelectedRow(), 3) != " ") {
                            int id = Integer.parseInt(table.getValueAt(table.getSelectedRow(), 0).toString());
                            metricStorage.setResolvedHost(id);
                            JOptionPane.showMessageDialog(null, "Succeed =)");
                            TableModel tableModel = metricStorage.getHostTableModel();
                            table.setModel(tableModel);
                        } else {
                            JOptionPane.showMessageDialog(null, "Not succeed =( \r\nend_datetime unknown");
                        }
                    }
                } catch (NullPointerException e) {

                }
            }
        };
    }//Добавление элемента
}
