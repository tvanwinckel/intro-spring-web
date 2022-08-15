# Exercise 5: A solution

A solution for the CurrencyController. Note! It is in general bad practice to introduce additional logic like checking the secret and action into the controller. You want to "hide" this in your Service/Usecase. This is just for demonstration purpose.

```java
@Controller
@RequestMapping(path = "/currency")
public class CurrencyController {

    private final static Logger LOGGER = LoggerFactory.getLogger(CurrencyController.class);

    private final CurrencyService currencyService;
    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyController(final CurrencyService currencyService, final CurrencyRepository currencyRepository) {
        this.currencyService = currencyService;
        this.currencyRepository = currencyRepository;
    }

    @GetMapping
    public String getTotalCurrency(final Model model) {
        LOGGER.info("GET total currency.");
        model.addAttribute("message", "Total amount of currency: " + currencyRepository.getAll());
        return "inventoryView";
    }

    @PostMapping
    public String addOrSubtractGoldToInventory(@RequestBody final Currency currency,
                                               @RequestParam(name = "action", required = false, defaultValue = "add") final String action,
                                               @RequestHeader(name = "key") final String key,
                                               final Model model) {
        if (key.equals("secret")) {
            if (action.equals("add")) {
                final Currency current_currency = currencyRepository.getAll();
                final Currency newCurrency = currencyService.add(current_currency, currency);
                model.addAttribute("message", "Amount of currency: " + newCurrency);
            } else if (action.equals("subtract")) {
                final Currency current_currency = currencyRepository.getAll();
                final Currency newCurrency = currencyService.subtract(current_currency, currency);
                model.addAttribute("message", "Amount of currency: " + newCurrency);
            } else {
                model.addAttribute("message", "Sorry, no action taken");
            }
        } else {
            model.addAttribute("message", "Sorry, wallet is locked");
        }

        return "inventoryView";
    }
```

```java
public class Currency {

    private final int gold;
    private final int silver;
    private final int copper;

    public Currency(@JsonProperty(value = "gold") int gold,
                    @JsonProperty(value = "silver") int silver,
                    @JsonProperty(value = "copper") int copper) {
        this.gold = gold;
        this.silver = silver;
        this.copper = copper;
    }

    // ... Getters, toString, ...
```
