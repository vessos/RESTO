package com.area.areaservice.exceptionHandler;

public class AreaNotFoundException extends RuntimeException {

    public AreaNotFoundException(String message){
        super(message);
    }
}
