package com.resto.countryreactive.exceptionHandler;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(String message){
        super(message);
    }
}
