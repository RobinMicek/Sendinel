package cz.sendinel.api.controller;

import cz.sendinel.api.dto.exception.ExceptionDto;
import cz.sendinel.api.util.MapperUtil;
import org.apache.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ExceptionDto> handleResponseStatusException(ResponseStatusException exception) {
        return ResponseEntity.status(exception.getStatusCode().value()).body(MapperUtil.toDto(exception, ExceptionDto.class));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDto> handleAllExceptions(Exception exception) {
        int httpStatus = HttpStatus.SC_INTERNAL_SERVER_ERROR;

        return ResponseEntity.status(HttpStatus.SC_INTERNAL_SERVER_ERROR).body(
                new ExceptionDto(httpStatus, exception.getMessage())
        );
    }

}
