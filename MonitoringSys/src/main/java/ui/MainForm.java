package ui;

import core.MetricStorage;
import core.SpringService;
import core.branches.CoreBranch;
import core.configurations.SSHConfiguration;
import core.hibernate.services.HostService;
import core.hibernate.services.HostServiceImpl;
import core.interfaces.db.IMetricStorage;
import core.models.InstanceMetric;
import core.models.Value;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.Hour;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.activation.DataSource;
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
public class MainForm extends JFrame {

    private JPanel MForm;
    @Autowired
    MetricStorage metricStorage;
    public MainForm() throws InterruptedException, SQLException {
        super("Monitoring");
        CoreBranch.run();
        final JPanel panel = new JPanel();
        createDesign(panel);

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

        JMenuItem metricItem = new JMenuItem("Edit Metric");
        metricItem.setFont(font);
        fileMenu.add(metricItem);
        metricItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    new MetricRedactor().setVisible(true);
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
        //HostService hostsser = SpringService.getHosts();
        HostServiceImpl hostService = new HostServiceImpl();
        List<SSHConfiguration> hosts;
        hosts=hostService.getAll(); //список хостов

        for (SSHConfiguration host:hosts) {
            listModel.addElement(host);
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

       /* listmetric.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                String metric;
                int id;
                int i = listmetric.getSelectedIndex();
                metric = (String) listModelMetric.get(i);
                int metricID = TypeOfMetric.getTypeOfMetric(metric);
                try {
                    panel.add(MainForm.drawChar(metricStorage,metricID));
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }


            }
        });*/


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
                    new Chart(title,idhost).setVisible(true);
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
        Chart chart = new Chart();
        List<Value> values;
        values = metricStorage.getValues(1,metricID);
        for (int i = 1; i < 10; i++) {
            value = values.get(i).getValue();
            series.add(new Minute(i, hour), value);
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

