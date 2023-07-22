package dev.aj.domain.model;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Franc extends Money{

    public Franc(BigDecimal amount) {
        super(amount, Currency.FRANC);
    }

}
