package ui.form;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.*;

import core.branches.CoreBranch;
import core.branches.SQLBranch;
import core.models.Metric;
import org.jfree.ui.ApplicationFrame;

public class MainForm extends JFrame {

    private JPanel MForm;

    public MainForm() throws InterruptedException, SQLException {
        super("Monitoring");
        CoreBranch.run();
        createDesign();
    }





    public static void createmenu(MainForm frame) {

        Font font = new Font("Times New Roman", Font.PLAIN, 14);
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        fileMenu.setFont(font);

        JMenuItem conItem = new JMenuItem("Connect");
        conItem.setFont(font);
        fileMenu.add(conItem);
        conItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new ConForm().setVisible(true);
            }
        });

        JMenuItem editItem = new JMenuItem("Edit Config");
        editItem.setFont(font);
        fileMenu.add(editItem);
        editItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    new HostRedactor().setVisible(true);
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


    public void createDesign() throws SQLException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(null);

        JButton button4= new JButton("View chart");
        button4.setBounds(0, 150, 250, 30);
        panel.add(button4);

        int i;
        panel.setLayout(null);
        final DefaultListModel listModel = new DefaultListModel();
        java.util.List<String> hosts;
        hosts= SQLBranch.getListIP(); //список хостов
        for (String host:hosts) {
            listModel.addElement(host);
        }
        final JList list = new JList(listModel);//получаем лист хостов
        list.setSelectedIndex(0);
        list.setFocusable(false);
        //для каждого хоста выводим его метрики
        //
        //
        final DefaultListModel listModelMetric = new DefaultListModel();
        java.util.List<Metric> metrics;
        i = list.getSelectedIndex();
        String host = (String) listModel.get(i);
        int id = SQLBranch.getHostIDbyTitle(host);
        metrics=SQLBranch.getMetricsByHostId(id);
        for (Metric metric: metrics){
            listModelMetric.addElement(metric.getTitle());
        }
        final JList listmetric = new JList(listModelMetric);
        listmetric.setSelectedIndex(0);
        listmetric.setFocusable(false);
        list.setBounds(0,0,125,150);
        listmetric.setBounds(125,0,125,150);

        panel.add(list);
        panel.add(listmetric);

        getContentPane().add(panel);
        setPreferredSize(new Dimension(250, 235));

        list.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                String host;
                int id;
                int i = list.getSelectedIndex();
                host = (String) listModel.get(i);
                java.util.List<Metric> metrics = null;
                try {
                    id = SQLBranch.getHostIDbyTitle(host);
                    metrics=SQLBranch.getMetricsByHostId(id);
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
                    idhost = SQLBranch.getHostIDbyTitle(host);
                    new Chart(title,idhost).setVisible(true);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame.setDefaultLookAndFeelDecorated(true);
                MainForm frame = null;
                try {
                    frame = new MainForm();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                createmenu(frame);
            }
            ;        });
    }
}

