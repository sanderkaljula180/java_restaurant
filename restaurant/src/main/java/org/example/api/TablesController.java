package org.example.api;

import com.sun.net.httpserver.HttpExchange;
import org.example.configuration.ResourcesNotFoundException;
import org.example.configuration.StacktraceConfig;
import org.example.dto.OccupyTableRequestDTO;
import org.example.dto.OccupyTableResponseDTO;
import org.example.helpers.JsonResponseConverter;
import org.example.helpers.Mapper;
import org.example.response.ApiResponse;
import org.example.response.ErrorResponse;
import org.example.services.TableService;
import org.json.JSONObject;

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
            } catch (IOException | ResourcesNotFoundException | SQLException | NumberFormatException e) {
                StacktraceConfig.logStackTraceFromThread(e);
                errorResponse.errorResponse(httpExchange, e);
            }

        }
    }

    public void occupyTable(HttpExchange httpExchange) throws IOException {
        if (httpExchange.getRequestMethod().equals("POST")) {
            try {
                JSONObject jsonObject = jsonConverter.convertRequestBodyJsonByteIntoJsonObject(httpExchange);
                OccupyTableRequestDTO occupyTableRequestDTO = Mapper.toOccupyTableRequestDto(
                        jsonObject.getInt("tableId"),
                        jsonObject.getInt("numberOfQuests"),
                        jsonObject.getInt("waitressId")
                );
                OccupyTableResponseDTO occupyTableResponseDTO = tableService.occupyTable(occupyTableRequestDTO);
//                LocalDateTime time =  LocalDateTime.now();
//                Headers headers = httpExchange.getResponseHeaders();
//                headers.add("Date", String.valueOf(time));
//                headers.add("Content-Type", "application/json");
//                headers.add("Connection", "keep-alive");
//                httpExchange.sendResponseHeaders(200, 0);
//                OutputStream response = httpExchange.getResponseBody();
//                response.write();
//                response.close();
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
