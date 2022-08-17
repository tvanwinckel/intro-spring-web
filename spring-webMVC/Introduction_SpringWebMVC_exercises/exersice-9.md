# Exercise 9: Creating a form with Thymeleaf

We are again given a *Character* object. And would like to make a simple editor for this Character object.

* Create a controller allowing us to ask a detailed view of this Character.
* Extend the controller allowing us to edit the Character details.
* When you submit your changes, show a summary of the Character details again.

```java
public class Character {

    private final String name;
    private final String type;
    private final String faction;
    private final int level;

    public Character(@JsonProperty(value = "name") final String name,
                     @JsonProperty(value = "type") final String type,
                     @JsonProperty(value = "faction") final String faction,
                     @JsonProperty(value = "level") final int level) {
        this.name = name;
        this.type = type;
        this.faction = faction;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getFaction() {
        return faction;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "Character{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", faction='" + faction + '\'' +
                ", level=" + level +
                '}';
    }
}
```

