package com.myspringprojects.rnd.springboottesting.controllers;

import com.myspringprojects.rnd.springboottesting.exception.NonExistingHeroException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SuperHeroExceptionHandler {

    @ExceptionHandler(NonExistingHeroException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleNonExistingSuperHero() {
    }
}
