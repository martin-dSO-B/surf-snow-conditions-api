package com.portfolio.surfsnow.shared.exception;

public class ExternalForecastServiceException extends RuntimeException {

    public ExternalForecastServiceException(String message) {
        super(message);
    }

    public ExternalForecastServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
