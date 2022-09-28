package rpc;

import java.net.URL;
import java.util.*;

import org.apache.xmlrpc.XmlRpcConfigImpl;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.server.*;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.webserver.WebServer;

import javax.swing.*;

public class JavaClient {
    public static void main (String [] args) {
        MainForm mainForm = new MainForm();
        try {
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL("http://localhost:8741"));

            XmlRpcClient client = new XmlRpcClient();
            client.setConfig(config);

            Object[] arr = (Object[])client.execute("server.getGoods", new Object[] {});

            mainForm.setRows(arr);

        } catch (Exception exception) {
            System.err.println("JavaClient: " + exception);
            JOptionPane.showMessageDialog(null,
                    "Ошибка соединения с сервером",
                    "Внимание",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}