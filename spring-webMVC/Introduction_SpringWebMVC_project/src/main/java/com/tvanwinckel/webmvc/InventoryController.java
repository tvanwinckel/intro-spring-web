package com.tvanwinckel.webmvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InventoryController {

    private final static Logger LOGGER = LoggerFactory.getLogger(InventoryController.class);

    @GetMapping("/inventory")
    public String getInventory(final Model model) {
        LOGGER.info("Received a GET request on the inventoryMethod.");
        model.addAttribute("items", "Empty");
        return "inventoryView";
    }
}
