package com.tvanwinckel.webmvc;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CurrencyTest {

    @Test
    public void testAddCurrencies() {
        final Currency c1 = new Currency(10, 20, 30);
        final Currency c2 = new Currency(10, 20, 30);

        final Currency result = c1.add(c2);
        Assertions.assertThat(result).isEqualTo(new Currency(20, 40, 60));
    }

    @Test
    void testAddCurrenciesOverflows() {
        final Currency c1 = new Currency(0, 99, 99);
        final Currency c2 = new Currency(0, 0, 1);

        final Currency result = c1.add(c2);
        Assertions.assertThat(result).isEqualTo(new Currency(1, 0, 0));
    }

    @Test
    void subtractCurrencies() throws Exception {
        final Currency c1 = new Currency(10, 20, 30);
        final Currency c2 = new Currency(10, 20, 30);

        final Currency result = c1.subtract(c2);
        Assertions.assertThat(result).isEqualTo(new Currency(0, 0, 0));
    }

    @Test
    void testSubtractingCurrenciesOverflows() throws Exception {
        final Currency c1 = new Currency(1, 24, 0);
        final Currency c2 = new Currency(0, 0, 1);

        final Currency result = c1.subtract(c2);
        Assertions.assertThat(result).isEqualTo(new Currency(0, 99, 99));
    }

    @Test
    void testCurrencyCantBeNegagive() throws Exception {
        final Currency c1 = new Currency(0, 0, 0);
        final Currency c2 = new Currency(1, 0, 0);

        Assertions.assertThatExceptionOfType(Exception.class)
                .isThrownBy(() -> c1.subtract(c2));
    }
}