package dev.aj;

import java.util.stream.IntStream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MainTest {

    public static final String FIZZ = "Fizz";
    public static final String BUZZ = "Buzz";
    private static Main main;

    @BeforeAll
    static void beforeAll() {
        //Can be used to instantiate 'pure' function type behaviour, can be faster but be careful
        main = new Main();
    }

    @AfterAll
    static void afterAll() {
        main = null;
    }

    @BeforeEach
    void setUp() {
//        main = new Main();
    }

    @AfterEach
    void tearDown() {
//        main = null;
    }

    @Order(1)
    @Test
    void TEST_FOR_DIVISIBLE_BY_THREE() {
        Assertions.assertEquals(FIZZ, main.compute(3), () -> "Should have returned Fizz");
    }

    @Order(2)
    @Test
    void TEST_FOR_DIVISIBLE_BY_FIVE() {
        Assertions.assertEquals(BUZZ, main.compute(5), () -> "Should have returned Buzz");
    }


    @Order(3)
    @Test
    void TEST_FOR_DIVISIBLE_BY_THREE_AND_FIVE() {
        Assertions.assertEquals(FIZZ + BUZZ, main.compute(15), () -> "Should have returned FizzBuzz");
    }

    @Order(4)
    @Test
    void TEST_FOR_DIVISIBLE_BY_ZERO() {
        Assertions.assertEquals("0", main.compute(0), () -> "Should have returned '0'");
    }

    @Order(5)
    @Test
    void TEST_FOR_DIVISIBLE_BY_NEITHER_THREE_NOR_FIVE() {
        Assertions.assertEquals("2", main.compute(2), () -> "Should have returned '2'");
    }

    @Order(6)
    @Test
    @Disabled("Only for debugging purposes, no value add")
    void PRINT_FIZZ_OR_BUZZ_FOR_NUMBERS_ZERO_TO_HUNDRED() {
        IntStream.rangeClosed(0, 100)
                .forEach(main::computeAndPrint);
        // No assertions, not preferred, but can be used to just test some code for debugging purposes, disable it
    }

    @Order(7)
    @ParameterizedTest(name = "Value = {0}, Expected = {1}")
    // Just include the label, and used placeholders {} to fetch the csv values
    @CsvSource({
            "0,0",
            "3,Fizz",
            "5,Buzz",
            "9,Fizz",
            "15,FizzBuzz",
            "43,43",
            "60,FizzBuzz",
            "81, Fizz",
            "82,82"
    })
    void TEST_FIZZ_BUZZ_USING_PARAMETERIZED_TEST(String input, String output) {
        Assertions.assertEquals(output, main.compute(Integer.parseInt(input)));
    }

    @Order(8)
    @ParameterizedTest(name = "Value = {0}, Expected = {1}")
    @CsvFileSource(resources = "/fizzbuzztestsource.csv")
    @DisplayName("Test with CSV file source")
    void testFizzBuzzUsingCSVFile(String input, String output) {
        Assertions.assertEquals(output, main.compute(Integer.parseInt(input)));
    }
}