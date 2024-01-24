package dev.com.kupreychik;

import com.sun.net.httpserver.HttpServer;
import dev.com.kupreychik.handlres.DevicesHandler;
import dev.com.kupreychik.handlres.PingHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {
    private static Logger LOGGER = LogManager.getLogger(Main.class);

    private static Integer PORT = System.getenv("PORT") != null
            ? Integer.parseInt(System.getenv("PORT"))
            : 8080;

    public static void main(String[] args) {
        LOGGER.trace("Server is starting...");
        LOGGER.trace("PORT = " + PORT);

        HttpServer server;
        try {
            server = HttpServer.create();
            server.bind(new InetSocketAddress(PORT), 0);
        } catch (IOException ex) {
            LOGGER.error("Server can't start! Reason: {}", ex.getMessage(), ex);
            return;
        }

        server.createContext("/", new PingHandler());
        server.createContext("/devices", new DevicesHandler());

        server.start();

    }

}















