# Exercise 3: Request Mapping

We are given the controller from our previous exersice:

```java
@Controller
@RequestMapping(path = "/inventory")
public class InventoryController {

    private final static Logger LOGGER = LoggerFactory.getLogger(InventoryController.class);

    @GetMapping
    public String getInventory(final Model model) {
        LOGGER.info("Received a GET request on the inventoryMethod.");
        model.addAttribute("items", "Empty");
        return "inventoryView";
    }
}
```

```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <title>Inventory</title>
</head>
<body>
<h1>My inventory contains:</h1>
<p th:text="'message: ' + ${message}" ></p>
</body>
</html>
```

Add different request mappings (GET, POST, PUT, DELETE, ...) to the controller and map them individually to `/inventory`. Verify that your mappings are working as intended, this can be done by logging a specific message for each mapping, by running your application in debug and using breakpoints, or any other method you see fit.

**Extra:**

* What happens if two mappings of the same type direct to the same endpoint?
  * In the same controller class
  * In a different controller class
* What happens if two mappings of a different type direct to the same endpoint?
