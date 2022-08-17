# Exercise 7

We now have a currency object giving us a detailed view of how much gold, silver and copper we have. Our controller allows to add or subract currency from our inventory. Currency can not go below zero and if it does we throw an exception. Catch this exception:

```java
@PostMapping(path = "/currency")
public String addGoldToInventory(@RequestBody final Currency currency,
                                 @RequestParam(name = "action", required = false, defaultValue = "add") final String action,
                                 @RequestHeader(name = "key") final String key, final Model model) throws Exception {
    if(key.equals("secret")) {
        if (action.equals("add")) {
            final Currency newCurrency = this.currency.add(currency);
            model.addAttribute("message", "Amount of currency: " + newCurrency);
        } else if (action.equals("subtract")) {
            final Currency newCurrency = this.currency.subtract(currency);
            model.addAttribute("message", "Amount of currency: " + newCurrency);
        } else {
            throw new UnknownCurrencyOpperationException();
        }
    } else {
        model.addAttribute("message", "Sorry, wallet is locked");
    }

    return "inventoryView";
}
```

* Catch individual `NotEnoughCurrencyException` and `UnknownCurrencyOpperationException` in their respective excptionhandlers.
* All other exceptions are caught by a generic exception handler.
* Return a view of some sort to the user, informing that something went wrong.

Extra:

* When you return a view to the user, send along a http 500 code instead of the default 200. (Might require you to you a ResponseEntity)

```java
@ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAll(final Exception e) {
        // When using a ResponseEntity it is not possible to return a Model, ModelMap or ModelAndView.
        return new ResponseEntity<>("Oops! Something went wrong, try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleSpecific(final Exception e) {
        final ModelAndView view = new ModelAndView("basicView");
        view.addObject("message", "Oops! Something went wrong, an IllegalArgument was given.");
        return view;
    }

```