package org.example.response;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;

public class ApiResponse {

    public void okResponse(byte[] byteResponse, HttpExchange httpExchange) throws IOException {
        LocalDateTime time =  LocalDateTime.now();
        Headers headers = httpExchange.getResponseHeaders();
        headers.add("Date", String.valueOf(time));
        headers.add("Content-Type", "application/json");
        headers.add("Connection", "keep-alive");
        httpExchange.sendResponseHeaders(200, 0);
        OutputStream response = httpExchange.getResponseBody();
        response.write(byteResponse);
        response.close();
    }

    public void createResponse(byte[] byteResponse, HttpExchange httpExchange) {
        // WELL SEE WHAT COMES HERE
    }

}
