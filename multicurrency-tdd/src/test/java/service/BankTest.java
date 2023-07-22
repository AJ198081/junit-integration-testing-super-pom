package service;

import dev.aj.domain.model.Currency;
import dev.aj.domain.model.Money;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
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
    private static final double DYNAMIC_CONVERSION_RATE = 2;
    private Bank bank;
    private Bank dynamicBank;

    @BeforeEach
    void setUp() {
        bank = new Bank(CONVERSION_RATE);
        dynamicBank = new Bank(DYNAMIC_CONVERSION_RATE);
    }

    @AfterEach
    void tearDown() {
        bank = null;
    }

    @ParameterizedTest(name = "Converting {0} {1} to {3} will fetch us {2} {3}")
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
    void Test_Currency_Conversion(double amount, String currencyName, double convertedAmount, String convertedCurrencyName) {

        Money money = new Money(amount, Currency.getByName(currencyName));

        Currency convertedCurrency = Currency.getByName(convertedCurrencyName);
        Money convertedMoney = new Money(convertedAmount, convertedCurrency);

        Assertions.assertEquals(convertedMoney, bank.currencyConversion(money, convertedCurrency));
    }

    @ParameterizedTest
    @MethodSource(value = "getCurrencyConversionTestingParameters")
    void Test_Customisable_Currency_Conversion(double amount, Currency fromCurrency, Currency toCurrency, double expectedAmount) {

        Money money = new Money(amount, fromCurrency);

        Money convertedMoney = new Money(expectedAmount, toCurrency);

        Assertions.assertEquals(convertedMoney, dynamicBank.currencyConversion(money, toCurrency));
    }

   static Stream<Arguments> getCurrencyConversionTestingParameters() {

       return Stream.generate(() -> getCurrencyConversionArguments())
             .limit(5);
    }

    private static Arguments getCurrencyConversionArguments() {
        Currency[] values = Currency.values();

        double amount = new BigDecimal(new Random().nextDouble(0, 5000)).setScale(2, RoundingMode.CEILING).doubleValue();

        Currency fromCurrency = values[new Random().nextInt(0, values.length)];

        Currency toCurrency = values[new Random().nextInt(0, values.length)];

        return Arguments.arguments(amount, fromCurrency, toCurrency, getConvertedAmount(fromCurrency, toCurrency, amount));
    }

    private static double getConvertedAmount(Currency fromCurrency, Currency toCurrency, double amount) {
        double convertedAmount = 0.0;

        if (fromCurrency.equals(toCurrency)) {
            convertedAmount = amount;
        } else if (toCurrency.equals(Currency.FRANC)) {
            convertedAmount = new BigDecimal(amount * DYNAMIC_CONVERSION_RATE).setScale(2, RoundingMode.CEILING).doubleValue();
        } else {
            convertedAmount = new BigDecimal(amount / DYNAMIC_CONVERSION_RATE).setScale(2, RoundingMode.CEILING).doubleValue();
        }
        return convertedAmount;
    }
}