package dev.aj.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Dollar extends Money {

    public Dollar(double amount) {
        super(amount, Currency.DOLLAR);
    }
}
