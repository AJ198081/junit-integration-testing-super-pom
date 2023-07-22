package dev.aj.domain.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static jakarta.validation.Validation.buildDefaultValidatorFactory;

@Tag(value = "unit")
class FrancTest {

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
    void testFrancEquality(BigDecimal amountOne, BigDecimal amountTwo) {
        Franc fiveFranc = Money.getFranc(amountOne);
        Franc francFive = Money.getFranc(amountTwo);

        Assertions.assertEquals(fiveFranc, francFive);
    }

    @Test
    void testNonNegativeAmounts() {
        Set<ConstraintViolation<Franc>> violations = validator.validate(new Franc(new BigDecimal(-1)));
        violations.forEach(System.out::println);
        // We aren't using any framework to bring the beans into context,
        // Remember the annotations themselves don't do anything, it is the framework that will process and execute them
        Assertions.assertEquals(1, violations.size());
    }

    @ParameterizedTest(name = "When CHF{0} is multiplied by {1}, we get CHF{2}")
    @CsvSource({
            "5, 2, 10",
            "15, 2, 30",
            "45, 10, 450",
            "1, 1, 1",
    })
    void testMultiplication(BigDecimal amount, BigDecimal multiplier, BigDecimal multipliedAmount) {
        Money initialFrancs = Money.getFranc(amount);
        Money multipliedFrancs = initialFrancs.times(multiplier);
        Assertions.assertAll("Multiply francs and check amount and currency",
                () -> Assertions.assertEquals(multipliedAmount, multipliedFrancs.getAmount()),
                () -> Assertions.assertEquals(multipliedFrancs.getCurrency(), initialFrancs.getCurrency())
        );
    }
}