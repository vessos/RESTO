package com.resto.countryreactive.exceptionHandler;

public class NotAuthorizedException extends RuntimeException{

    public NotAuthorizedException(String message){
        super(message);
    }
}
