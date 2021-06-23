package org.fan.phonebooking.controller;

import lombok.extern.log4j.Log4j2;
import org.fan.phonebooking.services.exceptions.PhoneNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;
import java.util.Map;

@Log4j2
@RestControllerAdvice
public class ControllerAdvice {
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<Map<String, Object>> illegalArgumentException(Throwable ex) {
        log.debug(ex.getMessage());
        return Mono.just(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(PhoneNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Mono<Map<String, Object>> notFoundException(Throwable ex) {
        log.debug(ex.getMessage());
        return Mono.just(Map.of("error", ex.getMessage()));
    }
}
