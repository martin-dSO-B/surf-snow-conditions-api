package com.portfolio.surfsnow.shared.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import java.util.Map;
import java.util.TreeMap;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<ProblemDetail> handleResourceNotFound(
            ResourceNotFoundException exception,
            HttpServletRequest request
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
        problemDetail.setTitle("Resource not found");
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ProblemDetail> handleIllegalArgument(
            IllegalArgumentException exception,
            HttpServletRequest request
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
        problemDetail.setTitle("Bad request");
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity.badRequest().body(problemDetail);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<ProblemDetail> handleDataIntegrityViolation(
            DataIntegrityViolationException exception,
            HttpServletRequest request
    ) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                "Request conflicts with existing data or database constraints"
        );
        problemDetail.setTitle("Data conflict");
        problemDetail.setInstance(URI.create(request.getRequestURI()));

        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        Map<String, String> errors = new TreeMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Request validation failed"
        );
        problemDetail.setTitle("Validation failed");
        problemDetail.setProperty("errors", errors);

        if (request instanceof ServletWebRequest servletWebRequest) {
            problemDetail.setInstance(URI.create(servletWebRequest.getRequest().getRequestURI()));
        }

        return ResponseEntity.badRequest().body(problemDetail);
    }
}
