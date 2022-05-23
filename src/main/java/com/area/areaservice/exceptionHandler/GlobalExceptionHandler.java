package com.area.areaservice.exceptionHandler;

import com.area.areaservice.exceptionHandler.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AreaNotAuthorizedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ApiResponses(value = {
        @ApiResponse(responseCode = "403",description = "NotAuthorized",content = @Content)
    })
    public ResponseEntity<ErrorResponse> handleNotAuthorized(AreaNotAuthorizedException ex){
        log.info(ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.FORBIDDEN.getReasonPhrase(),"Not Authorized."),HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AreaNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "404",description = "Not Found",content = @Content)
    })
    public ResponseEntity<ErrorResponse> handleAreaNotFoundException(AreaNotFoundException ex){
        log.info(ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.NOT_FOUND.getReasonPhrase(),"Not Found"),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "500",description = "Something terrible errors was happening in source xcode of countryService.",content = @Content)
    })
    public ResponseEntity<ErrorResponse> handleGeneralError(Exception ex){
        log.info(ex.getMessage());
        return new ResponseEntity<>(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),"Unknown error."),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
