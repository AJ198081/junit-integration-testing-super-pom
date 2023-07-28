package dev.aj.junit;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@DisplayName(value = "Test Calculator")
class CalculatorTest {

    private Calculator calculator;

    @BeforeAll
    static void beforeAll() {

    }

    @AfterAll
    static void afterAll() {

    }

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @AfterEach
    void tearDown() {
        calculator = null;
    }

    private static Stream<Arguments> integerAdditionParameters() {
        return Stream.of(
                Arguments.of(33, 4, 37),
                Arguments.of(133, 400, 533),
                Arguments.of(33, 33, 66),
                Arguments.of(87, -87, 0),
                Arguments.of(0, 0, 0),
                Arguments.of(450, 1, 451)
        );
    }

    @DisplayName(value = "33 / 0 => !Arithmetic Error!")
    @Test
    void Test_Integer_Division_When_Divided_By_Zero_Should_Throw_Arithmetic_Exception() {

        int dividend = 33;
        int divisor = 0;

        Assertions.assertThrows(ArithmeticException.class, () -> calculator.integerDivision(dividend, divisor));
    }

    @ParameterizedTest(name = "Subtracting {1} from {0} gives {2}")
    @CsvSource(value = {
            "28, 5, 23",
            "5000, 5000, 0",
            "230, 250, -20",
            "45, 45, 0",
            "23, 5, 18",
            "0, 0, 0"
    })
    void Integer_Subtraction(int minuend, int subtrahend, int expectedResult) {
        int result = calculator.integerSubtraction(minuend, subtrahend);

        org.assertj.core.api.Assertions.assertThat(result)
                .isEqualTo(expectedResult);
    }

    @ParameterizedTest
    @MethodSource(value = "integerAdditionParameters")
    @Order(1)
    void integerAddition(int addend1, int addend2, int expectedResult) {
        int result = calculator.integerAddition(addend1, addend2);

        org.assertj.core.api.Assertions.assertThat(result)
                .isEqualTo(expectedResult);

        Assertions.assertEquals(expectedResult, result);
    }
}