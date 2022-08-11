package com.tvanwinckel.webmvc.controllers;

import com.tvanwinckel.webmvc.models.Currency;
import com.tvanwinckel.webmvc.models.InventoryItem;
import com.tvanwinckel.webmvc.exceptions.NotEnoughCurrencyException;
import com.tvanwinckel.webmvc.exceptions.UnknownCurrencyOpperationException;
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




}
