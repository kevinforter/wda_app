package ch.hslu.informatik.swde.wda.rws.util;

import java.net.Socket;
import java.util.Arrays;

public class ServerChecker {
    public static void checkServer(String host, int port) {
        try (Socket socket = new Socket(host, port)) {
            System.out.printf("%-50s PORT: %-13d - Status: ✅%n", "Server Status: " + host, port);
        } catch (Exception e) {
            System.out.printf("%-50s PORT: %-13d - Status: ❌%n", "Server Status: " + host, port);
        }
    }
}