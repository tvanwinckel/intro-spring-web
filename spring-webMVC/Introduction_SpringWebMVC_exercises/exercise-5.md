# Exercise 5: Consuming and producing media types

Given is the controller from the previous exercise and an object for inventory items.

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

    @GetMapping (path = "/items/{index}")
    public String getItemFromInventory(@PathVariable(name = "index") final int itemIndex, final Model model) {
        model.addAttribute("message", "The item at " + itemIndex + " is " + items.get(itemIndex));
        return "inventoryView";
    }
```

```java
import com.fasterxml.jackson.annotation.JsonProperty;

public class InventoryItem {

    private final String name;
    private final String quality;
    private final int durability;

    public InventoryItem(@JsonProperty(value = "name") final String name,
                         @JsonProperty(value = "quality") final String quality,
                         @JsonProperty(value = "durability") final int durability) {
        this.name = name;
        this.quality = quality;
        this.durability = durability;
    }

    public String getName() {
        return name;
    }

    public String getQuality() {
        return quality;
    }

    public int getDurability() {
        return durability;
    }

    @Override
    public String toString() {
        return "InventoryItem{" +
                "name='" + name + '\'' +
                ", quality='" + quality + '\'' +
                ", durability=" + durability +
                '}';
    }
}
```

Rework the controller to:

* Return a single item at a given index as Json.
* Return a list of items in the inventory.
* Add a new item to the inventoy, added as a Json object.

**Extra**

Rework the field variable 'gold' to become a 'currency' object having gold, silver and copper fields. Allow the controller to return the total amount of currency in the inventory and add/subtract currency to/from the inventory.
