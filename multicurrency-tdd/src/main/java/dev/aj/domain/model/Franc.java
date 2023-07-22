package dev.aj.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Franc extends Money{

    public Franc(double amount) {
        super(amount, Currency.FRANC);
    }

}
