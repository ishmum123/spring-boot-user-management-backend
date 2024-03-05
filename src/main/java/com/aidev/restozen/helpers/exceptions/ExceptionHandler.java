package com.aidev.restozen.helpers.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        MultiValueMap<Object, Object> errorMap = new LinkedMultiValueMap<>();
        ex.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> errorMap.add(fieldError.getField(), fieldError.getDefaultMessage()));
        return new ResponseEntity<>(Map.of("errors", errorMap), HttpStatus.BAD_REQUEST);
    }
}
