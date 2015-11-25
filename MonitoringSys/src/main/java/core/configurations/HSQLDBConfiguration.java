package core.configurations;

import core.interfaces.DBConnectInterface;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class HSQLDBConfiguration implements DBConnectInterface {
    private Connection connection = null;
    private Statement statement;
    Properties prop = new Properties();
    InputStream input = null;


    public boolean load() {
        try {
            prop.load(new FileInputStream("config.properties"));
            if(loadDriver()& getConnection())return true;
        } catch (IOException ex) {
            ex.printStackTrace();
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
            Class.forName(prop.getProperty("ConfigDriver"));
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Driver is not load!!!!!");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private boolean getConnection() {
        try {
            String connectionString = prop.getProperty("ConfigUrl");
            String login = prop.getProperty("Configdbuser");
            String password = prop.getProperty("Configdbpassword");
            connection = DriverManager.getConnection(connectionString, login, password);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Not Connection");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void close()
    {
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

    public void createTable() {
        try {
            statement = connection.createStatement();
            String sql = "CREATE TABLE `SSHConfiguration` (" +
                    "   id INT NOT NULL AUTO_INCREMENT," +
                    "   host VARCHAR(20) default NULL," +
                    "   port INT default NULL," +
                    "   login VARCHAR(20) default NULL," +
                    "   password VARCHAR(20) default NULL," +
                    "   PRIMARY KEY (id)\n" +
                    ");";
            statement.executeUpdate(sql);
        } catch (SQLException e) {

        }
    }

    public void fillTable() {
        try {
            statement = connection.createStatement();
            String sql = "INSERT INTO SSHConfiguration (login) VALUES('FIST')," +
                    "('POUTS')," +
                    "('FBTO')," +
                    "('TROD')," +
                    "('UITS')";
            statement.executeUpdate(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void printTable() {
        try {
            statement = connection.createStatement();
            String sql = "SELECT * FROM SSHConfiguration";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
//                JOptionPane.showMessageDialog(null, resultSet.getInt(1));
                System.out.println(resultSet.getInt(1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
