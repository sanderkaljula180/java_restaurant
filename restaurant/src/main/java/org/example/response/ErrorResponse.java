package org.example.response;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dto.ErrorResponseDTO;
import org.example.exceptions.ResourceNotAvailable;
import org.example.helpers.JsonResponseConverter;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;

public class ErrorResponse {

    private final JsonResponseConverter jsonResponseConverter;
    private static final Logger log = LogManager.getLogger(ErrorResponse.class);

    public ErrorResponse(JsonResponseConverter jsonResponseConverter) {
        this.jsonResponseConverter = jsonResponseConverter;
    }

    public void errorResponse(HttpExchange httpExchange, Exception e) throws IOException {
        ErrorResponseDTO errorResponseDTO = entityForErrorResponse(e, httpExchange);
        LocalDateTime time =  LocalDateTime.now();
        Headers headers = httpExchange.getResponseHeaders();
        headers.add("Date", String.valueOf(time));
        headers.add("Content-Type", "application/json");
        headers.add("Connection", "keep-alive");
        httpExchange.sendResponseHeaders(errorResponseDTO.getStatus(), 0);
        OutputStream response = httpExchange.getResponseBody();
        response.write(jsonResponseConverter.convertDTOIntoJsonByte(errorResponseDTO));
        response.close();
    }



    /**
     * Error codes that probably will be written here
     * 400 - Bad Request
     * 404 - Resource doesnt exist
     * 500 - Internal
     * 503 - Service unavailable, like Database
     * **/
    public ErrorResponseDTO entityForErrorResponse(Exception e, HttpExchange httpExchange) {
        if (e.toString().contains("ConflictException")) {
            return new ErrorResponseDTO(
                    LocalDateTime.now(),
                    409,
                    "Internal Conflict",
                    e.getMessage(),
                    httpExchange.getRequestURI().getPath()
            );
        }

        if (e.toString().contains("ResourceNotAvailable")) {
            return new ErrorResponseDTO(
                    LocalDateTime.now(),
                    503,
                    "Service Unavailable",
                    e.getMessage(),
                    httpExchange.getRequestURI().getPath()
            );
        }

        if (e.toString().contains("NumberFormatException")) {
            return new ErrorResponseDTO(
                    LocalDateTime.now(),
                    400,
                    "Invalid input",
                    e.getMessage(),
                    httpExchange.getRequestURI().getPath()
            );
        }

        if (e.toString().contains("ResourcesNotFoundException")) {
            return new ErrorResponseDTO(
                    LocalDateTime.now(),
                    404,
                    "The resource does not exist.",
                    e.getMessage(),
                    httpExchange.getRequestURI().getPath()
            );
        }

        return new ErrorResponseDTO(
                LocalDateTime.now(),
                500,
                "Internal Server Error",
                "Unexpected server error",
                httpExchange.getRequestURI().getPath()
        );
    }

}
