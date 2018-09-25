package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DatabaseHandler {
    private static DatabaseHandler handler = null;

    private static final String DB_URL = "jdbc:derby:database;create=true";
    private static Connection conn = null;
    private static Statement stmt = null;
    public DatabaseHandler()
    {
        createConnection();
    }
    private static void createConnection() {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
            conn = DriverManager.getConnection(DB_URL);
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cant load database", "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }
    public void setupSongTable(){
        String TABLE_NAME = "SONGS";
        try {
            stmt = conn.createStatement();
            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables =dbm.getTables(null,null,TABLE_NAME.toUpperCase(),null);
            if(tables.next()){
                System.out.println("Table "+ TABLE_NAME +"already exists");
            }
            else{
                stmt.execute("CREATE TABLE "+ TABLE_NAME + "("
                        + " id int NOT NULL ,\n"
                        + " path varchar(300),\n"
                        + " title varchar(200),\n"
                        + " album varchar(200),\n"
                        + " artist varchar(200),\n"
                        + " genre varchar(200)"
                        + ")");

            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {

        }

    }
    public ResultSet execQuery(String query) {
        ResultSet result;
        try {
            stmt = conn.createStatement();
            result = stmt.executeQuery(query);
        }
        catch (SQLException ex) {
            System.out.println("Exception at execQuery:datahanderr" + ex.getLocalizedMessage());
            return null;
        } finally {

        }
        return result;
    }
    public boolean execAction(String qu) {
        try {
            stmt = conn.createStatement();
            stmt.execute(qu);
            return true;
        }
        catch (SQLException ex) {
            System.out.println("Exception at execQuery:datahandler" + ex.getLocalizedMessage());
            return false;
        }
        finally {

        }
    }

}
