package dev.aj.domain.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
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
    void testDollarEquality(int amountOne, int amountTwo) {
        Money fiveDollar = Money.getDollar(amountOne);
        Money dollarFive = Money.getDollar(amountTwo);

        Assertions.assertNotEquals(Money.getFranc(5), fiveDollar);

        Assertions.assertEquals(Dollar.class, fiveDollar.getClass());
        Assertions.assertEquals(fiveDollar, dollarFive);
    }

    @Test
    void testNonNegativeAmounts() {
        Set<ConstraintViolation<Dollar>> violations = validator.validate(Money.getDollar(-1));
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
    void testMultiplication(int amount, int multiplier, int multipliedAmount) {
        Dollar fiveDollar = Money.getDollar(amount);
        Dollar tenDollar = (Dollar) fiveDollar.times(multiplier);
        Assertions.assertEquals(multipliedAmount, tenDollar.getAmount());
    }
}