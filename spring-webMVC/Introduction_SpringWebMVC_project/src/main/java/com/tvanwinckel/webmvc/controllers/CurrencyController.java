package com.tvanwinckel.webmvc.controllers;

import com.tvanwinckel.webmvc.exceptions.NotEnoughCurrencyException;
import com.tvanwinckel.webmvc.exceptions.UnknownCurrencyOpperationException;
import com.tvanwinckel.webmvc.models.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/currency")
public class CurrencyController {

    private final static Logger LOGGER = LoggerFactory.getLogger(CurrencyController.class);

    private final Currency currency = new Currency(10, 23, 67);

    @GetMapping
    public String getTotalCurrency(final Model model) {
        LOGGER.info("Received a GET request on the inventoryMethod.");
        model.addAttribute("message", "Amount of currency: " + currency);
        return "inventoryView";
    }

    @PostMapping
    public String addGoldToInventory(@RequestBody final Currency currency,
                                     @RequestParam(name = "action", required = false, defaultValue = "add") final String action,
                                     @RequestHeader(name = "key") final String key,
                                     final Model model) throws Exception {
        if(key.equals("secret")) {
            if (action.equals("add")) {
                final Currency newCurrency = this.currency.add(currency);
                model.addAttribute("message", "Amount of currency: " + newCurrency);
            } else if (action.equals("subtract")) {
                final Currency newCurrency = this.currency.subtract(currency);
                model.addAttribute("message", "Amount of currency: " + newCurrency);
            } else {
                throw new UnknownCurrencyOpperationException();
            }
        } else {
            model.addAttribute("message", "Sorry, wallet is locked");
        }

        return "inventoryView";
    }

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
