package io.github.eschoe.hexagonal.common.web;

import io.github.eschoe.hexagonal.common.exception.DomainException;
import io.github.eschoe.hexagonal.common.exception.ErrorCode;
import io.github.eschoe.hexagonal.common.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ProblemDetail> handleNotFound(NotFoundException e) {
        return build(HttpStatus.NOT_FOUND, ErrorCode.NOT_FOUND, e.getMessage(), null);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ProblemDetail> handleDomain(DomainException e) {
        return build(HttpStatus.BAD_REQUEST, ErrorCode.DOMAIN_RULE_VIOLATED, e.getMessage(), null);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleIllegalArg(IllegalArgumentException e) {
        return build(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_FAILED, e.getMessage(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleBeanValidation(MethodArgumentNotValidException e) {
        List<String> violations = e.getBindingResult().getFieldErrors().stream()
            .map(f -> f.getField() + ": " + f.getDefaultMessage())
            .collect(Collectors.toList());
        return build(HttpStatus.BAD_REQUEST, ErrorCode.VALIDATION_FAILED, "Request validation failed", violations);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleFallback(Exception e) {
        log.error("Unhandled exception", e);
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_ERROR, "Unexpected error", null);
    }

    private ResponseEntity<ProblemDetail> build(HttpStatus status, ErrorCode code, String detail, List<String> violations) {
        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setTitle(code.name());
        problem.setDetail(detail);
        problem.setProperty("code", code.name());
        problem.setProperty("timestamp", Instant.now());
        String correlationId = MDC.get(CorrelationIdFilter.MDC_KEY);
        if (correlationId != null) {
            problem.setProperty("correlationId", correlationId);
        }
        if (violations != null && !violations.isEmpty()) {
            problem.setProperty("violations", violations);
        }
        return ResponseEntity.status(status).body(problem);
    }
}
