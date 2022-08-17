```java
@PostMapping(path = "/items", consumes = MediaType.APPLICATION_JSON_VALUE)
public String addItemToInventory(@RequestBody final InventoryItem item, final Model model) {
    items.add(item);
    
    final HashMap<String, String> map = new HashMap<>();
    map.put("message", "Added: " + item.toString());
    model.mergeAttributes(map);
    return "inventoryView";
}
```

```java
@PostMapping(path = "/items", consumes = MediaType.APPLICATION_JSON_VALUE)
public String addItemToInventory(@RequestBody final InventoryItem item, final ModelMap modelMap) {
    items.add(item);

    modelMap.addAttribute("message", "Added: " + item.toString());
    return "inventoryView";
}
```

```java
@PostMapping(path = "/items", consumes = MediaType.APPLICATION_JSON_VALUE)
public ModelAndView addItemToInventory(@RequestBody final InventoryItem item) {
    items.add(item);

    final ModelAndView view = new ModelAndView("inventoryView");
    view.addObject("message", "Added: " + item.toString());
    return view;
}
```

Example of a 'general' attribute added to the model that sets the total number of items in the inventory.

```java
@ModelAttribute
public void addTotalItemsInInventoryAttribute(final Model model) {
    model.addAttribute("numberOfTotalItems", items.size());
}
```
