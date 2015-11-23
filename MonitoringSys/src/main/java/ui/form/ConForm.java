package ui.form;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConForm extends JFrame {

    private int i = 0;

    public ConForm() {
        super("Connection");
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

        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String user = jTextlogin.getText();
                String password = jTextPas.getText();
                String IP =jTextIP.getText();
                int port = Integer.parseInt(jTextPort.getText());
//                SSHAgentOLD ssh = new SSHAgentOLD(new SSHConfiguration(IP, port, user, password));
//                if (ssh.connect()){
//                    Download d = new Download();
////                    d.saveMetrics(ssh);
//                   // JOptionPane.showConfirmDialog(null, "Connection was successful!", "Message", JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE);
//                    new ConForm().setVisible(false);
//                    JOptionPane.showMessageDialog( null,  "Connection was successful!", "Message", JOptionPane.DEFAULT_OPTION );
//
//                }
//                else JOptionPane.showMessageDialog( null,  "Connection was failed!", "Message", JOptionPane.DEFAULT_OPTION );
            }
        });

        getContentPane().add(panel);




        setPreferredSize(new Dimension(260, 160));
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
    }
}