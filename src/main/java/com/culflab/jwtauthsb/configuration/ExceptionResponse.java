package com.culflab.jwtauthsb.configuration;

import lombok.Getter;

import java.util.Date;

@Getter
public class ExceptionResponse {
    private Date timestamp = new Date();
    private String message;
    private String details;

    public ExceptionResponse(String message, String details){
        this.message = message;
        this.details = details;
    }

}
