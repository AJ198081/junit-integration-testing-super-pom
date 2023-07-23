package service;

import dev.aj.domain.model.Currency;
import dev.aj.domain.model.Money;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

@Tag(value = "unit")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class BankTest {

    private static final double CONVERSION_RATE = 2;
    private static final double DYNAMIC_CONVERSION_RATE = 5.343434;
    private Bank bank;
    private Bank dynamicBank;

    static Stream<Arguments> getCurrencyConversionTestingParameters() {

        return Stream.generate(() -> getCurrencyConversionArguments())
                     .limit(50);
    }

    private static Arguments getCurrencyConversionArguments() {
        Currency[] values = Currency.values();

        double doubleValue = new Random().nextDouble(0, 5000);

        BigDecimal amount = new BigDecimal(String.valueOf(doubleValue)).setScale(10, RoundingMode.CEILING);

        Currency fromCurrency = values[new Random().nextInt(0, values.length)];

        Currency toCurrency = values[new Random().nextInt(0, values.length)];

        return Arguments.arguments(amount, fromCurrency, toCurrency,
                getConvertedAmount(fromCurrency, toCurrency, amount));
    }

    private static BigDecimal getConvertedAmount(Currency fromCurrency, Currency toCurrency, BigDecimal amount) {
        BigDecimal conversionFactor = new BigDecimal(String.valueOf(DYNAMIC_CONVERSION_RATE)).setScale(10,
                RoundingMode.CEILING);
        BigDecimal convertedAmount;

        if (fromCurrency.equals(toCurrency)) {
            convertedAmount = amount;
        } else {
            if (toCurrency.equals(Currency.FRANC)) {
                convertedAmount = amount.multiply(conversionFactor)
                                        .setScale(10, RoundingMode.CEILING);
            } else {
                convertedAmount = amount.divide(conversionFactor, 10, RoundingMode.CEILING)
                                        .setScale(10, RoundingMode.CEILING);
            }
        }

        return convertedAmount;
    }

    @BeforeEach
    void setUp() {
        bank = new Bank(CONVERSION_RATE);
        dynamicBank = new Bank(DYNAMIC_CONVERSION_RATE);
    }

    @AfterEach
    void tearDown() {
        bank = null;
    }

    @ParameterizedTest(name = "Given conversion rate " + CONVERSION_RATE + ", Converting {0} {1} to {3} will fetch us {2} {3}")
    @CsvSource(value = {
            "23, dollar, 46, franc",
            "46, franc, 23, dollar",
            "46, dollar, 46, dollar",
            "23, franc, 23, franc",
            "23, franc, 11.5, dollar",
            "0, franc, 0, dollar",
            "0, franc, 0, franc",
            "0, dollar, 0, dollar",
            "0, dollar, 0, dollar",
    })
    void Test_Currency_Conversion(BigDecimal amount, String currencyName, BigDecimal convertedAmount, String convertedCurrencyName) {

        Money money = new Money(amount.setScale(10, RoundingMode.CEILING), Currency.getByName(currencyName));

        Currency convertedCurrency = Currency.getByName(convertedCurrencyName);

        Money convertedMoney = new Money(convertedAmount.setScale(10, RoundingMode.CEILING), convertedCurrency);

        Assertions.assertEquals(convertedMoney, bank.currencyConversion(money, convertedCurrency));
    }

    @ParameterizedTest(name = "Given $ to Franc rate of " + DYNAMIC_CONVERSION_RATE + ", Converting {0} {1} to {2} will fetch us {3} {2}")
    @MethodSource(value = "getCurrencyConversionTestingParameters")
    void Test_Customisable_Currency_Conversion(BigDecimal amount, Currency fromCurrency, Currency toCurrency, BigDecimal expectedAmount) {

        Money money = new Money(amount, fromCurrency);

        Money convertedMoney = new Money(expectedAmount, toCurrency);

        Assertions.assertEquals(convertedMoney, dynamicBank.currencyConversion(money, toCurrency));
    }
}