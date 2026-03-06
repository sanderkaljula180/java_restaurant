package org.example.configuration;

import com.sun.net.httpserver.HttpServer;
import org.example.router.ApiRouter;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpServerConfiguration {


    private final ApiRouter apiRouter;

    public HttpServerConfiguration(ApiRouter apiRouter) {
        this.apiRouter = apiRouter;
    }

    public HttpServer httpServer() throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8000),0);
        apiRouter.serverRouter(httpServer);
        httpServer.setExecutor(null);
        return httpServer;

    }


}
