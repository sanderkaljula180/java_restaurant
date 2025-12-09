package org.example.api;

import com.sun.net.httpserver.HttpExchange;
import org.example.configuration.StacktraceConfig;
import org.example.helpers.JsonResponseConverter;
import org.example.response.ApiResponse;
import org.example.response.ErrorResponse;
import org.example.services.TableService;

import java.io.IOException;
import java.sql.SQLException;

public class TablesController {

    private final TableService tableService;
    private final JsonResponseConverter jsonConverter;
    private final ErrorResponse errorResponse;
    private final ApiResponse apiResponse;

    public TablesController(TableService tableService, JsonResponseConverter jsonConverter, ErrorResponse errorResponse, ApiResponse apiResponse) {
        this.tableService = tableService;
        this.jsonConverter = jsonConverter;
        this.errorResponse = errorResponse;
        this.apiResponse = apiResponse;
    }

    public void getAllTables(HttpExchange httpExchange) throws IOException {
        if (httpExchange.getRequestMethod().equals("GET")) {
            try {
                byte[] byteResponse = jsonConverter.convertArrayIntoJsonByte(tableService.getAllTables());
                apiResponse.okResponse(byteResponse, httpExchange);
            } catch (NullPointerException | SQLException | IOException e) {
                StacktraceConfig.logStackTraceFromThread(e);
                errorResponse.errorResponse(httpExchange, e);
            }
        }
    }

    public void getTableForSetup(HttpExchange httpExchange) throws IOException {
        if (httpExchange.getRequestMethod().equals("GET")) {
            try {
                int tableId = checkForTableId(httpExchange.getRequestURI().getPath());
                byte[] byteResponse = jsonConverter.convertDTOIntoJsonByte(tableService.getTableForSetup(tableId));
                apiResponse.okResponse(byteResponse, httpExchange);
            } catch (Exception e) {
                StacktraceConfig.logStackTraceFromThread(e);
                errorResponse.errorResponse(httpExchange, e);
            }

        }
    }

    private static int checkForTableId(String getPath) {
        String[] getTableId = getPath.split("/");
        return Integer.parseInt(getTableId[4]);
    }
}
