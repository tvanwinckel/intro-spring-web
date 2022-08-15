package com.tvanwinckel.webmvc.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

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

    public int getGold() {
        return gold;
    }

    public int getSilver() {
        return silver;
    }

    public int getCopper() {
        return copper;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "gold=" + gold +
                ", silver=" + silver +
                ", copper=" + copper +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Currency currency = (Currency) o;
        return gold == currency.gold && silver == currency.silver && copper == currency.copper;
    }

    @Override
    public int hashCode() {
        return Objects.hash(gold, silver, copper);
    }
}
