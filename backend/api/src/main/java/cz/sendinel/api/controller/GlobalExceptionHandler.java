package cz.sendinel.api.controller;

import cz.sendinel.api.dto.exception.ExceptionDto;
import cz.sendinel.api.util.MapperUtil;
import jakarta.persistence.EntityNotFoundException;
import org.apache.http.HttpStatus;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.sql.SQLException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handles exceptions explicitly thrown with a ResponseStatusException
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ExceptionDto> handleResponseStatusException(ResponseStatusException exception) {
        return ResponseEntity.status(exception.getStatusCode().value())
                .body(MapperUtil.toDto(exception, ExceptionDto.class));
    }

    // Handles Hibernate/JPA and DB constraint exceptions
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionDto> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
        String message = extractConstraintMessage(ex);
        return buildResponse(HttpStatus.SC_BAD_REQUEST, message);
    }

    // Handles not found entities
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleEntityNotFound(EntityNotFoundException ex) {
        return buildResponse(HttpStatus.SC_NOT_FOUND, "The requested resource was not found.");
    }

    // Handles Spring Data errors when deleting a missing record
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<ExceptionDto> handleEmptyResultDataAccess(EmptyResultDataAccessException ex) {
        return buildResponse(HttpStatus.SC_NOT_FOUND, "The record you are trying to delete does not exist.");
    }

    // Handles validation errors from @Valid annotated request DTOs
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDto> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + " " + err.getDefaultMessage())
                .findFirst()
                .orElse("Invalid request parameters.");
        return buildResponse(HttpStatus.SC_BAD_REQUEST, message);
    }

    // Handles SQL or DB connectivity issues
    @ExceptionHandler({SQLException.class, CannotCreateTransactionException.class, DuplicateKeyException.class})
    public ResponseEntity<ExceptionDto> handleSqlAndTransaction(Exception ex) {
        return buildResponse(HttpStatus.SC_SERVICE_UNAVAILABLE, "Database error: " + ex.getMessage());
    }

    // Fallback — catches anything else
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> handleAllExceptions(Exception exception) {
        String message = (exception.getMessage() != null)
                ? exception.getMessage()
                : "Unexpected error occurred.";
        return buildResponse(HttpStatus.SC_INTERNAL_SERVER_ERROR, message);
    }

    private ResponseEntity<ExceptionDto> buildResponse(int httpStatus, String message) {
        return ResponseEntity.status(httpStatus).body(new ExceptionDto(httpStatus, message));
    }

    private String extractConstraintMessage(DataIntegrityViolationException ex) {
        Throwable root = org.springframework.core.NestedExceptionUtils.getMostSpecificCause(ex);
        String msg = root.getMessage();
        if (msg == null) return "Database constraint violation.";

        msg = msg.toLowerCase();
        if (msg.contains("unique") || msg.contains("duplicate"))
            return "Duplicate entry. A record with this value already exists.";
        if (msg.contains("foreign key"))
            return "Operation violates a foreign key constraint.";
        if (msg.contains("not-null"))
            return "Missing required field.";
        return "Database constraint violation.";
    }
}
