package cz.promtply.backend.controller;

import cz.promtply.backend.dto.exception.ExceptionDto;
import cz.promtply.backend.util.MapperUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ExceptionDto> handleResponseStatusException(ResponseStatusException exception) {
        System.out.println("Exception caught: " + exception.getStatusCode() + " - " + exception.getReason());
        return ResponseEntity.status(exception.getStatusCode().value()).body(MapperUtil.toDto(exception, ExceptionDto.class));
    }
}
