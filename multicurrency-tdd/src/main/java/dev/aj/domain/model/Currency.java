package dev.aj.domain.model;

import lombok.Getter;

@Getter
public enum Currency {

    FRANC("franc", "chf"), DOLLAR("dollar", "$");


    private final String name;
    private final String symbol;

  /*  Currency(String name) {

        this.name = name.toUpperCase();

        if (name.equalsIgnoreCase("FRANC")) {
            this.symbol = "CHF";
        } else {
            this.symbol = "$";
        }
    }*/

    Currency(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }

    public static Currency getByName(String currencyName) {
        return Currency.valueOf(currencyName.toUpperCase());
    }
}
