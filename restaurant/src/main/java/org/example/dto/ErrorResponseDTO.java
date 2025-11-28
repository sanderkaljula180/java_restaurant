package org.example.dto;

import java.time.LocalDateTime;

public class ErrorResponseDTO {
    private final LocalDateTime time;
    private final int status;
    private final String error;
    private final String errorMessage;
    private final String path;


    public ErrorResponseDTO(LocalDateTime time, int status, String error, String errorMessage, String path) {
        this.time = time;
        this.status = status;
        this.error = error;
        this.errorMessage = errorMessage;
        this.path = path;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public int getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "ErrorResponseDTO{" +
                "time=" + time +
                ", status=" + status +
                ", error='" + error + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
