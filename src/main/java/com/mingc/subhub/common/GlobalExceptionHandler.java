package com.mingc.subhub.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException e) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiError.of(e.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException e) {
    var msg = e.getBindingResult().getAllErrors().isEmpty()
        ? "Validation error"
        : e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiError.of(msg));
  }
}
