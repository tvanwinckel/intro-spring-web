package com.tvanwinckel.webmvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping(path = "/inventory")
public class InventoryController {

    private final static Logger LOGGER = LoggerFactory.getLogger(InventoryController.class);
    private final List<InventoryItem> items = new ArrayList<InventoryItem>(
            Arrays.asList(new InventoryItem("Sword", "epic", 100),
                    new InventoryItem("Shield", "common", 35)));
    private final Currency currency = new Currency(10, 23, 67);


    @GetMapping(path = "/items", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<InventoryItem> getItemsFromInventory() {
        LOGGER.info("Received a GET request on the inventoryMethod.");
        return items;
    }

    @PostMapping(path = "/items", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ModelAndView addItemToInventory(@RequestBody final InventoryItem item) {
        items.add(item);

        final ModelAndView view = new ModelAndView("inventoryView");
        view.addObject("message", "Added: " + item.toString());
        return view;
    }

    @GetMapping (path = "/items/{index}")
    public InventoryItem getItemFromInventory(@PathVariable(name = "index") final int itemIndex) {
        return items.get(itemIndex);
    }

//    @GetMapping(path = "/gold")
//    public String getGoldFromInventory(final Model model) {
//        LOGGER.info("Received a GET request on the inventoryMethod.");
//        model.addAttribute("message", "Amount of gold: " + gold);
//        return "inventoryView";
//    }
//
//    @PostMapping(path = "/gold")
//    public String addGoldToInventory(@RequestParam(name = "amount", required = false, defaultValue = "0") final String amount,
//                                     @RequestHeader(name = "key") final String key,
//                                     final Model model) {
//        if(key.equals("secret")) {
//            gold = gold + Integer.parseInt(amount);
//            model.addAttribute("message", "Amount of gold: " + gold);
//        } else {
//            model.addAttribute("message", "Sorry, gold is locked");
//        }
//
//        return "inventoryView";
//    }

    @GetMapping(path = "/currency")
    public String getTotalCurrency(final Model model) {
        LOGGER.info("Received a GET request on the inventoryMethod.");
        model.addAttribute("message", "Amount of currency: " + currency);
        return "inventoryView";
    }

    @PostMapping(path = "/currency")
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
