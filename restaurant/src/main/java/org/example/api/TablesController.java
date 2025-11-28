package org.example.api;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.example.configuration.StacktraceFromThread;
import org.example.helpers.JsonResponseConverter;
import org.example.response.ErrorResponse;
import org.example.services.TableService;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class TablesController {

    private final TableService tableService;
    private final JsonResponseConverter jsonConverter;
    private final ErrorResponse errorResponse;

    public TablesController(TableService tableService, JsonResponseConverter jsonConverter, ErrorResponse errorResponse) {
        this.tableService = tableService;
        this.jsonConverter = jsonConverter;
        this.errorResponse = errorResponse;
    }

    public void getAllTables(HttpExchange httpExchange) throws IOException {
        if (httpExchange.getRequestMethod().equals("GET")) {
            try {
                LocalDateTime time =  LocalDateTime.now();
                Headers headers = httpExchange.getResponseHeaders();
                headers.add("Date", String.valueOf(time));
                headers.add("Content-Type", "application/json");
                headers.add("Connection", "keep-alive");
                httpExchange.sendResponseHeaders(200, 0);
                OutputStream response = httpExchange.getResponseBody();
                response.write(jsonConverter.convertArrayIntoJsonByte(
                           tableService.getAllTables()
                        ));
                response.close();
            } catch (NullPointerException | SQLException | IOException e) {
//                StacktraceFromThread.logStackTraceFromThread();
                errorResponse.errorResponse(httpExchange, e);
            }
        }
    }

    // I have to check if there is a id behind that also, if not then return exception
    // ALso I have to send back bad request if checkForTableId fails. Create bad request method.
    public void getTableForSetup(HttpExchange httpExchange) {
        if (httpExchange.getRequestMethod().equals("GET")) {
            try {
                int tableId = checkForTableId(httpExchange.getRequestURI().getPath());
                LocalDateTime time =  LocalDateTime.now();
                Headers headers = httpExchange.getResponseHeaders();
                headers.add("Date", String.valueOf(time));
                headers.add("Content-Type", "application/json");
                headers.add("Connection", "keep-alive");
                httpExchange.sendResponseHeaders(200, 0);
                OutputStream response = httpExchange.getResponseBody();
                response.write(jsonConverter
                        .convertDTOIntoJsonByte(
                                tableService.getTableForSetup(tableId)
                        ));
                response.close();
            } catch (Exception e) {
                System.out.println(e);
                throw new RuntimeException(e);
            }

        }
    }

    private static int checkForTableId(String getPath) {
        String[] getTableId = getPath.split("/");
        return Integer.parseInt(getTableId[4]);
    }
}
