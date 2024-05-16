package ch.hslu.informatik.swde.wda.rws.util;

import java.net.Socket;

public class ServerChecker {
    public static void checkServer(String host, int port) {
        try (Socket socket = new Socket(host, port)) {
            System.out.println(host + " on PORT: " + port + " ✅");
        } catch (Exception e) {
            System.out.println(host + " on PORT: " + port + " ❌");
        }
    }
}