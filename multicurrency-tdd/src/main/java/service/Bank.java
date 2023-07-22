package service;

import dev.aj.domain.model.Currency;
import dev.aj.domain.model.Money;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Bank {
    private final double dollarToFrankRate;

    public Money currencyConversion(final Money money, final Currency toCurrency) {

        BigDecimal convertedAmount;

        if (money.getCurrency().equals(toCurrency)) {
            return money;
        } else {

            BigDecimal conversionRate = new BigDecimal(String.valueOf(dollarToFrankRate));

            if (money.getCurrency().equals(Currency.DOLLAR) && toCurrency.equals(Currency.FRANC)) {
                convertedAmount = money.getAmount()
                                      .multiply(conversionRate);
            } else {
                convertedAmount = money.getAmount().divide(
                        conversionRate, 10, RoundingMode.CEILING);
            }
            return new Money(convertedAmount.setScale(10, RoundingMode.CEILING), toCurrency);
        }

    }
}
