package com.tvanwinckel.webmvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String addItemToInventory(@RequestBody final InventoryItem item, final Model model) {
        items.add(item);
        model.addAttribute("message", "Added: " + item.toString());
        return "inventoryView";
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
