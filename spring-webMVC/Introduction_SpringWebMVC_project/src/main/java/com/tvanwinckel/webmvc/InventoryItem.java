package com.tvanwinckel.webmvc;

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
