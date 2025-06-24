package com.mahmoudbat.pcmonitor;

import java.io.IOException;



public class Main {
    public static void main(String[] args) {
        try {
            // Create and start the web server
            WebServer server = new WebServer();
            server.start();
        } catch (IOException e) {
            System.err.println("Error starting the server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}