package org.example;

import com.sun.net.httpserver.HttpServer;
import org.example.configuration.HttpServerConfiguration;
import org.example.database.ItemsRepository;

import java.io.IOException;
import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException {
        HttpServerConfiguration config = new HttpServerConfiguration();
        HttpServer httpServer = config.httpServer();
        httpServer.start();

    }
}