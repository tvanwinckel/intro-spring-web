# Exercise 4: a solution

```java
@Controller
@RequestMapping(path = "/inventory")
public class InventoryController {

    private final static Logger LOGGER = LoggerFactory.getLogger(InventoryController.class);
    private final List<String> items = new ArrayList<>(Arrays.asList("Sword", "Shield", "Mana Potion"));
    private int gold = 0;

    @GetMapping(path = "/items")
    public String getItemsFromInventory(final Model model) {
        LOGGER.info("Received a GET request on the inventoryMethod.");
        model.addAttribute("message", inventoryItemsToString());
        return "inventoryView";
    }

    @PostMapping(path = "/items")
    public String addItemToInventory(@RequestParam(name = "item") final String item, final Model model) {
        items.add(item);
        model.addAttribute("message", inventoryItemsToString());
        return "inventoryView";
    }

    @PostMapping(path = "/items2", params = "item")
    public String addItemToInventory2(final String item, final Model model) {
        items.add(item);
        model.addAttribute("message", inventoryItemsToString());
        return "inventoryView";
    }

    @GetMapping (path = "/items/{index}")
    public String getItemFromInventory(@PathVariable(name = "index") final int itemIndex, final Model model) {
        model.addAttribute("message", "The item at " + itemIndex + " is " + items.get(itemIndex));
        return "inventoryView";
    }

    @GetMapping(path = "/gold")
    public String getGoldFromInventory(final Model model) {
        LOGGER.info("Received a GET request on the inventoryMethod.");
        model.addAttribute("message", "Amount of gold: " + gold);
        return "inventoryView";
    }

    @PostMapping(path = "/gold")
    public String addGoldToInventory(@RequestParam(name = "amount", required = false, defaultValue = "0") final String amount,
                                     @RequestHeader(name = "key") final String key,
                                     final Model model) {
        if(key.equals("secret")) {
            gold = gold + Integer.parseInt(amount);
            model.addAttribute("message", "Amount of gold: " + gold);
        } else {
            model.addAttribute("message", "Sorry, gold is locked");
        }

        return "inventoryView";
    }

    private String inventoryItemsToString() {
        final StringBuilder inventoryItemList = new StringBuilder();
        for (final String i : items) {
            inventoryItemList.append(" ").append(i);
        }
        return inventoryItemList.toString();
    }
}
```
