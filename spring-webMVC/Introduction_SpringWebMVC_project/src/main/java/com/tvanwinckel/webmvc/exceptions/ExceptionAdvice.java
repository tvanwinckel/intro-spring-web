package com.tvanwinckel.webmvc.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "com.tvanwinckel.webmvc.controllers")
public class ExceptionAdvice {

    @ExceptionHandler(NotEnoughCurrencyException.class)
    public ResponseEntity<String> handleNotEnoughCurrencyException(final NotEnoughCurrencyException e) {
        // When using a ResponseEntity it is not possible to return a Model, ModelMap or ModelAndView.
        return new ResponseEntity<>("Oops! Not enough currency.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UnknownCurrencyOpperationException.class)
    public ResponseEntity<String> handleUnknownCurrencyOpperation(final UnknownCurrencyOpperationException e) {
        // When using a ResponseEntity it is not possible to return a Model, ModelMap or ModelAndView.
        return new ResponseEntity<>("Oops! Unknown currency opperation.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
