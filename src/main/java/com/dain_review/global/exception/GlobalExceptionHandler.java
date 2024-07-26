package com.dain_review.global.exception;


import com.dain_review.global.api.API;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j(topic = "exceptionHandler")
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity globalError(GlobalException ex) {
        ex.exceptionHandling();
        return API.ERROR(ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity validationError(MethodArgumentNotValidException ex) {
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            log.error(fieldError.getDefaultMessage());
        }
        return API.ERROR(ex);
    }

}
