package dev.aj;

import dev.aj.domain.model.Currency;
import dev.aj.domain.model.Franc;
import dev.aj.domain.model.Money;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;

@Tag(value = "unit")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MoneyTest {

    // TODO:  Demo advantages of using the 'auto' generated test classes by -
    // 1. Click 'Ctrl + Shift + T'
    // 2. Make the 'getAmount' protected rather than ''public', which it is at the moment

    //TODO: Just because I didn't keep 'MoneyTest' in the same package as 'Money' -
    // I now need to have everything as 'public', can't have 'private' or 'protected'
    private Validator validator;

    @BeforeEach
    void setUp() {
        validator = buildDefaultValidatorFactory()
                .getValidator();
    }

    @AfterEach
    void tearDown() {
    }

    @ParameterizedTest
    @CsvSource({
            "0, 0",
            "5, 5",
            "69, 69"
    })
    void Test_Money_Equality(BigDecimal amountOne, BigDecimal amountTwo) {
        Money fiveMoney = new Money(amountOne, Currency.DOLLAR);
        Money moneyFive = new Money(amountTwo, Currency.DOLLAR);

        Assertions.assertEquals(fiveMoney, moneyFive);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 1",
    })
    void Test_Non_Equality_Of_Dollar_And_Franc(BigDecimal dollarAmount, BigDecimal francAmount) {
        Assertions.assertNotEquals(Money.getDollar(dollarAmount).getCurrency(), Money.getFranc(francAmount).getCurrency());
        Assertions.assertEquals(Money.getDollar(dollarAmount)
                                     .getAmount(), new Franc(francAmount).getAmount());
    }

    @Test
    void testNonNegativeAmounts() {
        Set<ConstraintViolation<Money>> violations = validator.validate(new Money(new BigDecimal(-1), Currency.DOLLAR));
        violations.forEach(System.out::println);
        // We aren't using any framework to bring the beans into context,
        // Remember the annotations themselves don't do anything, it is the framework that will process and execute them
        Assertions.assertEquals(1, violations.size());
    }

    @ParameterizedTest(name = "When ${0} is multiplied by {1}, we get ${2}")
    @CsvSource({
            "5, 2, 10",
            "15, 2, 30",
            "45, 10, 450",
            "1, 1, 1",
    })
    void testMultiplication(BigDecimal amount, BigDecimal multiplier, BigDecimal multipliedAmount) {
        Money initialMoney = new Money(amount, Currency.FRANC);
        Money convertedMoney = initialMoney.times(multiplier);
        Assertions.assertEquals(multipliedAmount, convertedMoney.getAmount());
    }

    @ParameterizedTest(name = "When passed amount: {0} and currency: {2}, we get {1}{0}")
    @CsvSource({
            "5, $, dollar",
            "5, chf, franc",
            "15, $, dollar",
            "15, chf, franc",
    })
    @Order(1)
    void Test_Currency_Symbols_And_Amounts(BigDecimal amount, String currency, String currencyName) {

        Money money = new Money(amount, Currency.getByName(currencyName));

        Assertions.assertEquals(currency, money.getCurrency().getSymbol());
        Assertions.assertEquals(amount, money.getAmount());
    }
}