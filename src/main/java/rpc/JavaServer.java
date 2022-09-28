package rpc;

import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

import java.sql.*;
import java.util.Vector;

public class JavaServer {
    private static final String driverName = "org.sqlite.JDBC";
    private static final String connectionString = "jdbc:sqlite:goods.db";

    public static Vector DBRequest() {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            System.out.println("Can't get class. No driver found");
            e.printStackTrace();
            return null;
        }
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(connectionString);
        } catch (SQLException e) {
            System.out.println("Can't get connection. Incorrect URL");
            e.printStackTrace();
            return null;
        }

        System.out.println("Connected");

        String sql = "SELECT orderr.id, good.name, status.status " +
                "FROM orderr INNER JOIN good ON orderr.good = good.id " +
                "INNER JOIN status ON orderr.status = status.id;";


        Vector vec = new Vector();

        try (Statement stmt  = connection.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            while (rs.next()) {
                vec.addElement(new Object[] { rs.getString("id"), rs.getString("name"), rs.getString("status") });
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        try {
            connection.close();
        } catch (SQLException e) {
            System.out.println("Can't close connection");
            e.printStackTrace();
        }

        return vec;
    }

    public Vector getGoods(){
        System.out.println("Goods request");

        return DBRequest();
    }

    public static void main (String [] args){

        try {

            System.out.println("Attempting to start XML-RPC Server...");

            WebServer server = new WebServer(8741);
            PropertyHandlerMapping handlerMapping = new PropertyHandlerMapping();
            handlerMapping.addHandler("server", JavaServer.class);
            XmlRpcServer xmlRpcServer = server.getXmlRpcServer();
            xmlRpcServer.setHandlerMapping(handlerMapping);

            XmlRpcServerConfigImpl serverConfig = (XmlRpcServerConfigImpl) xmlRpcServer.getConfig();
            serverConfig.setEnabledForExtensions(true);
            serverConfig.setContentLengthOptional(false);

            server.start();

            System.out.println("Started successfully.");
            System.out.println("Accepting requests. (Halt program to stop.)");

        } catch (Exception exception){
            System.err.println("JavaServer: " + exception);
        }
    }
}