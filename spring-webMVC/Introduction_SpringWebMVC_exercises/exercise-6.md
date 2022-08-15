# Exercise 6: Models

For this exercise we continue using the controller from the previous exercise:

```java
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

    ...
}
```

Currently the `addItemToInventory` method returns a model. Expand the method to:

1. Return a view to work with a map of attributes instead of a single attribute.
2. Return a view with a ModelMap object.
3. Return a view based on a ModelAndView object.

Extra:

* Introduce a general model attribute that you can set with the annotation `@ModelAttribute`.
