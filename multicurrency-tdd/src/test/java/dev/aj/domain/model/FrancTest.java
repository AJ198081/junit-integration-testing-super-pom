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
    void testFrancEquality(int amountOne, int amountTwo) {
        Franc fiveFranc = Money.getFranc(amountOne);
        Franc francFive = Money.getFranc(amountTwo);

        Assertions.assertEquals(fiveFranc, francFive);
    }

    @Test
    void testNonNegativeAmounts() {
        Set<ConstraintViolation<Franc>> violations = validator.validate(new Franc(-1));
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
    void testMultiplication(int amount, int multiplier, int multipliedAmount) {
        Franc fiveFranc = Money.getFranc(amount);
        Franc tenFranc = (Franc) fiveFranc.times(multiplier);
        Assertions.assertEquals(multipliedAmount, tenFranc.getAmount());
    }
}