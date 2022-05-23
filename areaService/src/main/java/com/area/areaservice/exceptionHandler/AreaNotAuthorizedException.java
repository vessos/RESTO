package com.area.areaservice.exceptionHandler;

public class AreaNotAuthorizedException extends RuntimeException {

    public AreaNotAuthorizedException(String message){
        super(message);
    }
}
