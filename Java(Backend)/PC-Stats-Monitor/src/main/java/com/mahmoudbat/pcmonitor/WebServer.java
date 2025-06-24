package com.mahmoudbat.pcmonitor;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;



/**
 * A lightweight HTTP server that exposes system statistics via a JSON API.
 *
 * The server listens on port 8080 and responds to requests at `/OSHI-status`
 * by returning memory usage, CPU usage, and CPU temperature using the OSHI library.
 */

public class WebServer {

    private final OshiManager oshiManager;

    public  WebServer(){
        this.oshiManager = new OshiManager();
    }

    /**
     * Starts the HTTP server on port 8080.
     * Registers the /OSHI-status endpoint with a custom handler.
     */

   public void start() throws IOException{

       // Create an HTTP server bound to port 8080 with no backlog limit (0)
       HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

       // Register the /OSHI-status endpoint and assign a handler for it
       server.createContext("/OSHI-status", new OshiHandler(this.oshiManager));

       // Use the default executor (null)
       server.setExecutor(null);

       //Start the server
       server.start();
       System.out.println("Server started on port 8080 successfully");
       System.out.println("go to \"127.0.0.1:8080/OSHI-status\" ");

   }



    /**
     * A custom HTTP handler that responds with system stats in JSON format.
     * This inner class handles all GET requests made to /OSHI-status.
     */

    private static class OshiHandler implements HttpHandler {

            // Create a new instance of the hardware monitor
            private final OshiManager oshi;

            public OshiHandler(OshiManager oshiManager){
                this.oshi = oshiManager;
            }

        /**
         * Handles incoming HTTP requests to the /OSHI-status endpoint.
         * Responds with memory usage, CPU usage, and CPU temperature in JSON format.
         *
         * @param exchange The HTTP exchange object containing request and response data.
         * @throws IOException if an I/O error occurs while writing the response.
         */

        @Override
        public void handle(HttpExchange exchange) throws IOException {


            // Delay for 1 second to allow CPU usage reading to update
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}

            // Get data from hardware monitor
            double memory = oshi.getRamUsage();
            double  cpuUsage = oshi.getCpuUsage();
            double cpuTemp = oshi.getCpuTemp();

            // Format data as JSON
            String Message = String.format("{\n" +
                    "\"memoryUsage\" : %.2f, \n" +
                    "\"cpuUsage\" : %.2f, \n" +
                    "\"cpuTemp\" : %.2f\n" +
                    "}", memory, cpuUsage, cpuTemp);

            // Set content type to application/json
            exchange.getResponseHeaders().set("Content-Type", "application/json");

            // Send HTTP 200 OK response with the content length
            exchange.sendResponseHeaders(200, Message.getBytes(StandardCharsets.UTF_8).length);

            // Write the response body
            try (OutputStream os = exchange.getResponseBody()){
                os.write(Message.getBytes(StandardCharsets.UTF_8));
            }

        }
    }
}

