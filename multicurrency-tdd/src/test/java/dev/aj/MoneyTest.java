package dev.aj;

import dev.aj.domain.model.Dollar;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MoneyTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testDollarEquality() {
        Dollar fiveDollar = new Dollar(5);
        Dollar dollarFive = new Dollar(5);

        Assertions.assertEquals(fiveDollar, dollarFive);
    }

    @ParameterizedTest(name = "When ${0} is multiplied by {1}, we get ${2}")
    @CsvSource({
            "5, 2, 10",
            "15, 2, 30",
            "45, 10, 450",
            "1, 1, 1",
    })
    void testMultiplication(int amount, int multiplier, int multipliedAmount) {
        Dollar fiveDollar = new Dollar(amount);
        Dollar tenDollar = fiveDollar.times(multiplier);
        Assertions.assertEquals(multipliedAmount, tenDollar.getAmount());
    }
}