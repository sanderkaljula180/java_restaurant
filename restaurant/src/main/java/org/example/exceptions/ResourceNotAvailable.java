package org.example.exceptions;

public class ResourceNotAvailable extends RuntimeException {
    public ResourceNotAvailable(String message, Throwable cause) {
        super(message, cause);
    }

    public ResourceNotAvailable(String message) {
        super(message);
    }
}
