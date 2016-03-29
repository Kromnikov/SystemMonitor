package other;

import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Kromnikov on 08.11.2015.
 */
public class TestDB {
    private Connection connection = null;
    private Statement statement;
    public boolean loadDriver() {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Driver is not load!!!!!");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean getConnection() {
        try {
            String path = "mypath/";
            String dbname = "mydb";
            String connectionString = "jdbc:hsqldb:file:"+path+dbname;
            String login = "joe";
            String password = "password";
            connection = DriverManager.getConnection(connectionString, login, password);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Not Connection");
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
