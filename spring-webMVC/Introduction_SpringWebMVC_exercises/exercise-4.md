# Exercise 4: Accepting variables to the controller

Given following controller:

```java
@Controller
@RequestMapping(path = "/inventory")
public class InventoryController {

    private final static Logger LOGGER = LoggerFactory.getLogger(InventoryController.class);
    private final List<String> items = List.of("Sword", "Shield", "Mana Potion");
    private int gold = 0;

    @GetMapping(path = "/items")
    public String getInventory(final Model model) {
        LOGGER.info("Received a GET request on the inventoryMethod.");
        final StringBuilder inventoryItemList = new StringBuilder();
        for (final String item : items) {
            inventoryItemList.append(" ").append(item);
        }

        model.addAttribute("message", inventoryItemList.toString());
        return "inventoryView";
    }

    @GetMapping(path = "/gold")
    public String getGoldFromInventory(final Model model) {
        LOGGER.info("Received a GET request on the inventoryMethod.");
        model.addAttribute("message", "Amount of gold: " + gold);
        return "inventoryView";
    }
}
```

Using path variables, request parameters or headers, add the following methods:

* A method that allows you to get an item at a specific index from the inventory.
* A method to add or take gold from the inventory.

**Extra:**

* When adding gold, when no amount of gold, is given default to zero.
