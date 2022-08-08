# Exercise 2: Intercepting requests

For this exercise we are given a new controller returning a basic view based on a Model (we'll touch this so called Model later on).

```java
@Controller
@RequestMapping(path = "/inventory")
public class InventoryController {

    private final static Logger LOGGER = LoggerFactory.getLogger(InventoryController.class);

    @GetMapping("/inventory")
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
<p th:text="'items: ' + ${items}" ></p>
</body>
</html>
```

We would like catch the requests going to and coming from the controller:

* Log a message for preHandling the request.
* Log a message for postHandling the request.
* Log a message for afterCompletion of the request.

Extra:

* Log a message containing the request path going to the controller.
* What happens if we create a second controller? Can we focus interceptions for specific controllers?

## Hints

You will need an interceptor class. (look at the HandlerInterceptor interface) and do not forget to configure your interceptor.