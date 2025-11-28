package org.example.api;

import com.sun.net.httpserver.*;
import org.example.database.ItemsRepository;
import org.example.entities.Item;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class ItemController {

    private final ItemsRepository itemsRepository;

    public ItemController(ItemsRepository itemsRepository) {
        this.itemsRepository = itemsRepository;
    }

    public void getAllItems(HttpExchange httpExchange) throws IOException {

        if (httpExchange.getRequestMethod().equals("GET")) {
            try {
                LocalDateTime time =  LocalDateTime.now();
                Headers headers = httpExchange.getResponseHeaders();
                headers.add("Date", String.valueOf(time));
                headers.add("Content-Type", "text/html");
                headers.add("Connection", "keep-alive");
                httpExchange.sendResponseHeaders(200, 0);
                OutputStream response = httpExchange.getResponseBody();
                List<Item> allItems = itemsRepository.getAllItems();
                String items = allItems.toString();
                byte[] bytes = items.getBytes();
                response.write(bytes);
                response.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}