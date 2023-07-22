package dev.aj.domain.model;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Dollar extends Money {

    public Dollar(BigDecimal amount) {
        super(amount, Currency.DOLLAR);
    }
}
