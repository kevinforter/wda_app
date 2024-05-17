package ch.hslu.informatik.swde.wda.rws.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.net.URI;
import java.net.URISyntaxException;

public class SQLServerChecker {

    public static void checkServer(String url, String username, String password) {
        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String serverName = getServerNameFromUrl(url);
            String databaseName = getDatabaseNameFromUrl(url);
            System.out.printf("%-50s DB: %-15s - Status: ✅%n", "DB Connection: " + serverName, databaseName);
        } catch (SQLException e) {
            String serverName = getServerNameFromUrl(url);
            String databaseName = getDatabaseNameFromUrl(url);
            System.out.printf("%-50s DB: %-15s - Status: ❌%n", "DB Connection: " + serverName, databaseName);
        }
    }

    private static String getDatabaseNameFromUrl(String url) {
        String databaseName = url.replaceAll(".*/([^/?]+).*", "$1");
        if (databaseName.isEmpty()) {
            throw new RuntimeException("Database name not found in JDBC URL: " + url);
        }
        return databaseName;
    }

    private static String getServerNameFromUrl(String url) {
        String serverName = url.replaceAll(".*//([^/:]+).*", "$1");
        if (serverName.isEmpty()) {
            throw new RuntimeException("Server name not found in JDBC URL: " + url);
        }
        return serverName;
    }
}
