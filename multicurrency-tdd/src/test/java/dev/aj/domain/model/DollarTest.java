package dev.aj.domain.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;

@Tag(value = "unit")
class DollarTest {
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
    void testDollarEquality(BigDecimal amountOne, BigDecimal amountTwo) {
        Money fiveDollar = Money.getDollar(amountOne);
        Money dollarFive = Money.getDollar(amountTwo);

        Assertions.assertNotEquals(Money.getFranc(amountOne), fiveDollar);

        Assertions.assertEquals(Dollar.class, fiveDollar.getClass());
        Assertions.assertEquals(fiveDollar, dollarFive);
    }

    @Test
    void testNonNegativeAmounts(TestInfo testInfo) {
        Set<ConstraintViolation<Dollar>> violations = validator.validate(Money.getDollar(new BigDecimal(-1)));
        violations.forEach(System.out::println);
        System.out.println("TEST Info: -> " + testInfo);
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
        Money initialDollars = Money.getDollar(amount);
        Money multipliedDollar = initialDollars.times(multiplier);

        Assertions.assertAll("Multiply and check multiplied amount and currency",
                () -> Assertions.assertEquals(multipliedAmount, multipliedDollar.getAmount()),
                () -> Assertions.assertEquals(initialDollars.getCurrency(), multipliedDollar.getCurrency())
        );
    }
}