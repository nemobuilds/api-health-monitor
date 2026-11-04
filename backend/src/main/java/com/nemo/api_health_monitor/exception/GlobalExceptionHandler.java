package com.nemo.api_health_monitor.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestControllerAdvice
public class GlobalExceptionHandler {


    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Catch-all for any unhandled RuntimeException (500 Internal Server Error).
     * This is a safety net — you should handle specific exceptions where possible.
     */
    @ExceptionHandler(RuntimeException.class)
    public ProblemDetail handleGenericError(RuntimeException ex) {

        log.error("Unhandled runtime exception", ex);

        return createProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred"
        );
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ProblemDetail handleDuplicateResource(DuplicateResourceException ex) {
        return createProblemDetail(
                HttpStatus.CONFLICT, ex.getMessage()
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFound(ResourceNotFoundException ex) {
        return createProblemDetail(
                HttpStatus.NOT_FOUND, ex.getMessage()
        );
    }

    /**
     * Helper method to create a consistent ProblemDetail response.
     * ProblemDetail gives us a standardized JSON structure:
     * {
     *   "type": "about:blank",
     *   "title": "Not Found",
     *   "status": 404,
     *   "detail": "Endpoint not found with id: ...",
     *   "instance": "/api/v1/endpoints/..."
     *   "timestamp": "2026-06-10T23:53:00"
     * }
     */
    private ProblemDetail createProblemDetail(HttpStatus status, String message) {
        var detail = ProblemDetail.forStatusAndDetail(status, message);
        detail.setTitle(status.getReasonPhrase());
        detail.setProperty("timestamp", Instant.now().toString());
        return detail;
    }
}
