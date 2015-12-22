package ui;

import core.MetricStorage;
import core.SpringService;
import core.configurations.SSHConfiguration;
import core.hibernate.services.HostService;
import core.hibernate.services.HostServiceImpl;
import core.interfaces.db.IMetricStorage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Created by ANTON on 06.12.2015.
 */
public class ConForm extends JFrame {
    private int i = 0;
    @Autowired
    MetricStorage metricStorage;
    public ConForm() {
        super("Connection");
        final HostServiceImpl hosts = new HostServiceImpl();
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        JPanel panel = new JPanel();
        JLabel jLabel1 = new JLabel("Login");
        jLabel1.setBounds(10,10,100,20);
        panel.add(jLabel1);

        final JTextField jTextlogin = new JTextField();
        jTextlogin.setBounds(80,10,140,20);
        panel.add(jTextlogin);

        JLabel jLabel2 = new JLabel("Password");
        jLabel2.setBounds(10,30,100,20);
        panel.add(jLabel2);

        final JTextField jTextPas = new JTextField();
        jTextPas.setBounds(80,30,140,20);
        panel.add(jTextPas);

        JLabel jLabel3 = new JLabel("IP");
        jLabel3.setBounds(10,50,100,20);
        panel.add(jLabel3);

        final JTextField jTextIP = new JTextField("192.168.1.42");
        jTextIP.setBounds(80,50,140,20);
        panel.add(jTextIP);

        JLabel jLabel4 = new JLabel("Port");
        jLabel4.setBounds(10,70,100,20);
        panel.add(jLabel4);

        final JTextField jTextPort = new JTextField("22");
        jTextPort.setBounds(80,70,140,20);
        panel.add(jTextPort);

        panel.setLayout(null);
        JButton button1 = new JButton("Connect");
        button1.setBounds(120, 93, 100, 30);
        panel.add(button1);


        JCheckBox checkBox = new JCheckBox("Add standart metrics",true);
        checkBox.setBounds(10,125,150,15);
        panel.add(checkBox);

        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String user = jTextlogin.getText();
                String password = jTextPas.getText();
                String IP =jTextIP.getText();
                int port = Integer.parseInt(jTextPort.getText());
                SSHConfiguration sshconfig = new SSHConfiguration(IP,port,user,password);

                try {
                    hosts.save(sshconfig);
                    int id =  metricStorage.getHostIDbyTitle(IP);
                    metricStorage.addStandartMetrics(id);
                } catch (SQLException e1) {
                    JOptionPane.showMessageDialog( null, "User has not been connected!");
                    e1.printStackTrace();
                }
                JOptionPane.showMessageDialog( null,  "User has been connected!", "Message", JOptionPane.DEFAULT_OPTION );
                jTextlogin.setText("");
                jTextPas.setText("");


            }
        });

        getContentPane().add(panel);




        setPreferredSize(new Dimension(260, 180));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame.setDefaultLookAndFeelDecorated(true);
                new ConForm();

            }
        });
    }}


