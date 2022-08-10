package com.tvanwinckel.webmvc;

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

    public Currency add(final Currency currency) {
        int newAmountOfCopper = 0;
        int newAmountOfSilver = 0;
        int newAmountOfGold = 0;

        if (copper + currency.getCopper() < 100) {
            newAmountOfCopper = copper + currency.getCopper() + newAmountOfCopper;
        } else {
            newAmountOfSilver += 1;
            newAmountOfCopper = copper + currency.getCopper() - 100;
        }

        if (silver + currency.getSilver() + newAmountOfSilver < 100) {
            newAmountOfSilver = newAmountOfSilver + silver + currency.getSilver();
        } else {
            newAmountOfGold += 1;
            newAmountOfSilver = newAmountOfSilver + silver + currency.getSilver() - 100;
        }

        newAmountOfGold = gold + currency.getGold() + newAmountOfGold;

        return new Currency(newAmountOfGold, newAmountOfSilver, newAmountOfCopper);
    }

    public Currency subtract(final Currency currency) throws Exception {
        int newAmountOfCopper = 0;
        int newAmountOfSilver = 0;
        int newAmountOfGold = 0;

        if (gold - currency.getGold() < 0) {
            throw new NotEnoughCurrencyException();
        }
        newAmountOfGold = gold - currency.getGold();

        if (silver - currency.getSilver() >= 0) {
            newAmountOfSilver = silver - currency.getSilver();
        } else {
            newAmountOfGold -= 1;
            newAmountOfSilver = silver - currency.getSilver() + 100;
        }

        if (copper - currency.getCopper() >= 0) {
            newAmountOfCopper = copper - currency.getCopper();
        } else {
            if (newAmountOfSilver > 0) {
                newAmountOfSilver -= 1;
                newAmountOfCopper = copper - currency.getCopper() + 100;
            } else {
                if (newAmountOfGold > 0) {
                    newAmountOfGold -= 1;
                    newAmountOfSilver += 99;
                    newAmountOfCopper = copper - currency.getCopper() + 100;
                } else {
                    throw new NotEnoughCurrencyException();
                }

            }
        }

        return new Currency(newAmountOfGold, newAmountOfSilver, newAmountOfCopper);
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
