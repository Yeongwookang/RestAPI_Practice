package com.culflab.jwtauthsb.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
@RestController
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

    @ExceptionHandler(NoSuchElementException.class)
    public final ResponseEntity<ExceptionResponse> noSuchElementHandler(NoSuchElementException ex){
        ExceptionResponse response = new ExceptionResponse("Not Found Exception","조건에 맞는 것을 찾을 수 없습니다.");
        log.warn("error"+ex.getMessage()+"[NOT FOUND]");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
