# Exercise 9: A solution

CharacterController:

```java
@Controller
@RequestMapping(path = "/characters")
public class CharacterController {

    private Character character;

    public CharacterController(Character character) {
        this.character = new Character("BigSwordGuy", "Warrior", "Horde", 55);
    }

    @GetMapping
    public String personForm(final Model model) {
        model.addAttribute("character", character);
        return "characterForm";
    }

    @PostMapping
    public String personSubmit(@ModelAttribute final Character character) {
        return "characterView";
    }
}
```

Html file for showing Character details:

```html
<!DOCTYPE HTML>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
</head>
<body>
    <p th:text="'Name: ' + ${character.name}"></p>
    <p th:text="'Type: ' + ${character.type}"></p>
    <p th:text="'Faction: ' + ${character.faction}"></p>
    <p th:text="'Level: ' + ${character.level}"></p>
</body>
</html>
```

Html file for submitting a Character:

```html
<html xmlns:th="http://www.thymeleaf.org">
<body>
<h3>Enter character details:</h3>
<form action="#" th:action="@{/characters}" th:object="${character}" method="post">
    <p>Name: <input type="text" th:field="*{name}"/></p>
    <p>Type: <input type="text" th:field="*{type}"/></p>
    <p>Faction: <input type="text" th:field="*{faction}"/></p>
    <p>Level: <input type="text" th:field="*{level}"/></p>
    <p><input type="submit" value="Submit"/></p>
</form>
</body>
</html>
```
