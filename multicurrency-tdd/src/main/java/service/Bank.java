package service;

import dev.aj.domain.model.Currency;
import dev.aj.domain.model.Money;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Bank {
    private final double dollarToFrankRate;

    public Money currencyConversion(final Money money, final Currency toCurrency) {
        if (money.getCurrency()
                 .equals(toCurrency)) {
            return money;
        } else {
            double applicableConversionRate;

            if (money.getCurrency().equals(Currency.DOLLAR) && toCurrency.equals(Currency.FRANC)) {
                applicableConversionRate = dollarToFrankRate;
            } else {
                applicableConversionRate = 1 / dollarToFrankRate;
            }
            return new Money(money.getAmount() * applicableConversionRate, toCurrency);
        }

    }
}
