package core.configurations;


import core.interfaces.SQLinterface;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class SQLConfiguration implements SQLinterface{
    private Connection connection = null;
    private Statement statement;
    Properties prop = new Properties();
    InputStream input = null;

    public boolean load() {
        try {
            prop.load(new FileInputStream("config.properties"));
            if(loadDriver()& getConnection()){
                statement = connection.createStatement();
                return true;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private boolean loadDriver() {
        try {
            Class.forName(prop.getProperty("MetricDriver"));
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Driver is not load!!!!!");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean getConnection() {
        try {
            String login = prop.getProperty("Metricdbuser");
            String password = prop.getProperty("Metricdbpassword");
            String connectionString = prop.getProperty("MetricUrl");
            this.connection = DriverManager.getConnection(connectionString, login, password);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Not Connection");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void close() {
        try {
            this.connection.close();
            this.statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Statement getStatement() {
        return this.statement;
    }

}
