package org.example.response;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;

public interface ErrorResponse {

    public void errorResponse(HttpExchange httpExchange, int errorCode) throws IOException;

}
