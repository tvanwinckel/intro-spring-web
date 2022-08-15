# Exercise 3: Solution

We are given the controller from our previous exersice:

```java
@Controller
@RequestMapping(path = "/inventory")
public class InventoryController {

    private final static Logger LOGGER = LoggerFactory.getLogger(InventoryController.class);

    @PostMapping
    public String postMapping(final Model model) {
        model.addAttribute("message", "POST mapping");
        return "inventoryView";
    }

    @RequestMapping(path = "/old-post", method = RequestMethod.POST)
    public String oldPostMapping(final Model model) {
        model.addAttribute("message", "old POST mapping");
        return "inventoryView";
    }

    @GetMapping
    public String getInventory(final Model model) {
        LOGGER.info("Received a GET request on the inventoryMethod.");
        model.addAttribute("message", "GET mapping");
        return "inventoryView";
    }

    @PutMapping
    public String putMapping(final Model model) {
        model.addAttribute("message", "PUT mapping");
        return "inventoryView";
    }

    @DeleteMapping
    public String deleteMapping(final Model model) {
        model.addAttribute("message", "DELETE mapping");
        return "inventoryView";
    }

    @PatchMapping
    public String patchMapping(final Model model) {
        model.addAttribute("message", "PATCH mapping");
        return "inventoryView";
    }

}
```

Add different request mappings (GET, POST, PUT, DELETE, ...) to the controller and map them individually to `/inventory`. Verify that your mappings are working as intended, this can be done by logging a specific message for each mapping, by running your application in debug and using breakpoints, or any other method you see fit.

