package ui.form;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import ui.*;

public class MainForm extends JFrame {

    private JPanel MForm;

    public MainForm() throws InterruptedException {
        super("Monitoring");
//        CoreBranch.run();
        createbut();
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

        JMenuItem openItem = new JMenuItem("Open");
        openItem.setFont(font);
        fileMenu.add(openItem);

        JMenuItem closeItem = new JMenuItem("Save");
        closeItem.setFont(font);
        fileMenu.add(closeItem);

        JMenuItem closeAllItem = new JMenuItem("Close");
        closeAllItem.setFont(font);
        fileMenu.add(closeAllItem);

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


    public void createbut() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel();
        panel.setLayout(null);
        JButton button1 = new JButton("CPU");
        button1.setBounds(15, 25, 85, 30);
        panel.add(button1);

       button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    new Chart("CPU").setVisible(true);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        JButton button2 = new JButton("Memory");
        button2.setBounds(15, 60, 85, 30);
        panel.add(button2);

        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    new Chart("Memory").setVisible(true);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });



        JButton button3 = new JButton("Disc");
        button3.setBounds(15, 95, 85, 30);
        panel.add(button3);

        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    new Chart("Disc").setVisible(true);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });

        getContentPane().add(panel);
        setPreferredSize(new Dimension(130, 200));
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
                }
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                createmenu(frame);
            }
;        });
    }
}

