package com.tvanwinckel.webmvc.services;

import com.tvanwinckel.webmvc.exceptions.NotEnoughCurrencyException;
import com.tvanwinckel.webmvc.models.Currency;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService {

    public Currency add(final Currency c1, final Currency c2) {
        int newAmountOfCopper = 0;
        int newAmountOfSilver = 0;
        int newAmountOfGold = 0;

        if (c1.getCopper() + c2.getCopper() < 100) {
            newAmountOfCopper = c1.getCopper() + c2.getCopper() + newAmountOfCopper;
        } else {
            newAmountOfSilver += 1;
            newAmountOfCopper = c1.getCopper() + c2.getCopper() - 100;
        }

        if (c1.getSilver() + c2.getSilver() + newAmountOfSilver < 100) {
            newAmountOfSilver = newAmountOfSilver + c1.getSilver() + c2.getSilver();
        } else {
            newAmountOfGold += 1;
            newAmountOfSilver = newAmountOfSilver + c1.getSilver() + c2.getSilver() - 100;
        }

        newAmountOfGold = c1.getGold() + c2.getGold() + newAmountOfGold;

        return new Currency(newAmountOfGold, newAmountOfSilver, newAmountOfCopper);
    }

    public Currency subtract(final Currency c1, final Currency c2) throws NotEnoughCurrencyException {
        int newAmountOfCopper = 0;
        int newAmountOfSilver = 0;
        int newAmountOfGold = 0;

        if (c1.getGold() - c2.getGold() < 0) {
            throw new NotEnoughCurrencyException();
        }
        newAmountOfGold = c1.getGold() - c2.getGold();

        if (c1.getSilver() - c2.getSilver() >= 0) {
            newAmountOfSilver = c1.getSilver() - c2.getSilver();
        } else {
            newAmountOfGold -= 1;
            newAmountOfSilver = c1.getSilver() - c2.getSilver() + 100;
        }

        if (c1.getCopper() - c2.getCopper() >= 0) {
            newAmountOfCopper = c1.getCopper() - c2.getCopper();
        } else {
            if (newAmountOfSilver > 0) {
                newAmountOfSilver -= 1;
                newAmountOfCopper = c1.getCopper() - c2.getCopper() + 100;
            } else {
                if (newAmountOfGold > 0) {
                    newAmountOfGold -= 1;
                    newAmountOfSilver += 99;
                    newAmountOfCopper = c1.getCopper() - c2.getCopper() + 100;
                } else {
                    throw new NotEnoughCurrencyException();
                }

            }
        }

        return new Currency(newAmountOfGold, newAmountOfSilver, newAmountOfCopper);
    }
}
