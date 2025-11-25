package org.example.response;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;

public class ErrorResponseImpl implements ErrorResponse{

    @Override
    public void errorResponse(HttpExchange httpExchange, int errorCode) throws IOException {
//        LocalDateTime time =  LocalDateTime.now();
//        Headers headers = httpExchange.getResponseHeaders();
//        headers.add("Date", String.valueOf(time));
//        headers.add("Content-Type", "application/json");
//        headers.add("Connection", "keep-alive");
//        httpExchange.sendResponseHeaders(errorCode, 0);
//        OutputStream response = httpExchange.getResponseBody();
//        response.write();
//        response.close();
    }

}
