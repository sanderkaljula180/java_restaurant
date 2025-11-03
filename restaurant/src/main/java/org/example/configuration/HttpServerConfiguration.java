package org.example.configuration;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpServer;
import org.example.database.ItemsRepository;
import org.example.entities.Item;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// SAA ARU SELLEST KOODIST JA KEYWORDIDEST
public class HttpServerConfiguration {

    private final ItemsRepository itemsRepository;
    private static HttpServer httpServer;

    public HttpServerConfiguration() {
        this.itemsRepository = new ItemsRepository();
    }

    public HttpServer httpServer() throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8000),0);

        httpServer.createContext("/api/items", (handler -> {
            if (handler.getRequestMethod().equals("GET")) {
                // Next one should be check for content-type which I can get from getRequestHeaders() map.
                // But first I need to implement json. This will come later.

                try {
                    LocalDateTime time =  LocalDateTime.now();
                    Headers headers = handler.getResponseHeaders();
                    headers.add("Date", String.valueOf(time));
                    headers.add("Content-Type", "text/html");
                    headers.add("Connection", "keep-alive");
                    handler.sendResponseHeaders(200, 0);

                    OutputStream response = handler.getResponseBody();
                    ArrayList<Item> allItems = itemsRepository.getAllItems();
                    String items = allItems.toString();
                    byte[] bytes = items.getBytes();
                    response.write(bytes);
                    response.close();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }));
        // LEARN ABOUT THIS MORE. WHAT IS EXECUTOR?
        httpServer.setExecutor(null);



        return httpServer;
        
    }


}
