package dev.aj.junit;

import java.util.List;
import java.util.NoSuchElementException;
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
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.env.Environment;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

//Always "Run 'testClass' with Coverage" to see what you haven't covered.
// User -DskipTests=true to skip all unit tests
// User intelliJ test report export feature, or mvn surefire-report:report to generate site report

@Tag("unit")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class) //Removes the trailing '()' from test names and replaces '_' with spaces
class UtilsTest {

    public static final String STRING_THAT_IS_NOT_NULL = "Test String that is not null";
    public static final String STRING_OBJECT = "First Object";
    public static final String STRING_1 = "String 1";
    public static final String STRING_2 = "String 2";

    private Environment environment;

    private Utils util;

    @BeforeAll
    static void setEnvironmentToDev() {
        System.setProperty("spring.profiles.active", "DEV");
    }

    @AfterAll
    static void forgetEnvironmentToDev() {
        System.setProperty("spring.profiles.active", "PROD");
    }

    @BeforeEach
    void setUp() {
        util = new Utils();
    }

    @AfterEach
    void tearDown() {
        util = null;
    }

    @Test
    @DisplayName("Test addition feature in support for JIRA #EPRT-17480")
    void Test_Two_And_Three_Returns_Five() {
        double sumOfTwoAndThree = util.addTwoNumbers(2, 3);

        // Try providing the supplier of String, which is lazily fetched rather than a String, which is always calculated,
        assertEquals(5, sumOfTwoAndThree, "Expected sum of '2' and '3' to be '5', but received " + sumOfTwoAndThree);
    }

    @Test
    void Test_Not_Null_String_Returns_False() {
        boolean checkedForNull = util.checkForNull(STRING_THAT_IS_NOT_NULL);

        assertEquals(false, checkedForNull,
                () -> String.format("A string with value '%s', should have returned 'false'", STRING_THAT_IS_NOT_NULL));
    }

    @Test
    @Order(-3)
    void TEST_NULL_STRING_RETURNS_FALSE() {
        boolean checkedForNull = util.checkForNull(null);

        assertEquals(true, checkedForNull, () -> String.format("A null string, should have returned 'true'"));
    }

    @Test
    @Order(-4)
    void TEST_OBJECTS_SAME() {

        List<String> listOfString = List.of(STRING_1, STRING_2);
        List<String> anotherListOfString = List.of(STRING_1, STRING_2);

        boolean checkedStringsAreSame = util.checkObjectsSame(STRING_OBJECT, STRING_OBJECT);
        boolean checkedListsAreSameWhenSameElementAndOrder = util.checkObjectsSame(listOfString, anotherListOfString);

        assertSame(STRING_OBJECT, STRING_OBJECT,
                () -> String.format("Expected '%s' and '%s' to be same, but are different", STRING_OBJECT,
                        STRING_OBJECT));

        assertTrue(checkedStringsAreSame,
                () -> String.format("Expected %s and %s to return true, but returned false", STRING_OBJECT,
                        STRING_OBJECT));

        assertTrue(checkedListsAreSameWhenSameElementAndOrder, () -> String.format(
                "Expected two lists of same String elements and inserted in same order to return 'true', but are returned 'false'"));
    }

    @Test
    @Order(-3)
    void TEST_OBJECTS_NOT_SAME() {

        List<String> listOfString = List.of(STRING_1, STRING_2);
        List<String> anotherListOfSameStringsWithSameOrder = List.of(STRING_1, STRING_2);
        List<String> anotherListOfSameStringsWithDifferentOrder = List.of(STRING_2, STRING_1);

        boolean checkedListsWithSameInsertionOrder = util.checkObjectsSame(listOfString,
                anotherListOfSameStringsWithSameOrder);

        boolean checkedListWithDifferentInsertionOrder = util.checkObjectsSame(listOfString,
                anotherListOfSameStringsWithDifferentOrder);

        assertNotSame(listOfString, anotherListOfSameStringsWithSameOrder, () -> String.format(
                "Expected two lists of same String elements, with same insertion order to be different, but are same"));
        assertNotSame(listOfString, anotherListOfSameStringsWithDifferentOrder, () -> String.format(
                "Expected two lists of same String elements, with different insertion order to be different, but are same"));

        assertIterableEquals(listOfString, anotherListOfSameStringsWithSameOrder);
        assertArrayEquals(listOfString.toArray(), anotherListOfSameStringsWithSameOrder.toArray());

        assertFalse(checkedListWithDifferentInsertionOrder);

        assertTrue(checkedListsWithSameInsertionOrder, () -> String.format(
                "Expected two lists of same String elements but inserted in different order to return 'false', but are returned 'true'"));
    }

    @Test
    void CHECK_ELEMENT_EXISTS_IN_A_LIST() {

        List<String> listOfStrings = List.of(STRING_1, STRING_OBJECT, STRING_2, STRING_THAT_IS_NOT_NULL);

        assertDoesNotThrow(() -> util.checkElementExistsInAList(listOfStrings, STRING_2));

        assertThrows(NoSuchElementException.class,
                () -> util.checkElementExistsInAList(listOfStrings, "Unknown String"),
                () -> String.format("Expected to throw '%s', but didn't.", NoSuchElementException.class));
    }

    @Test
    @Disabled("Do not use this, or provide a reason here and tend to it ASAP")
    void CHECK_DISABLED_ANNOTATION() {
        // need to fix this...
    }

    @Test
    @Disabled("Enable after populating specific environment")
    @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = "*")
    void RUN_WHEN_SYSTEM_ENVIRONMENT_PRESENT() {

        String activeProfile = System.getenv("JAVA_HOME");

        Assertions.assertEquals("LOL", activeProfile,
                () -> String.format("Expected environment to be 'LOL', but was %s", activeProfile));
    }

    @Test
    @EnabledIfSystemProperty(named = "spring.profiles.active", matches = "DEV")
    void RUN_ONLY_WHEN_SYSTEM_PROPERTY() {
        String property = System.getProperty("spring.profiles.active");
        assertEquals("DEV", property,
                () -> String.format("Expected the system property to be 'DEV', but was %s", property));

        //Specified at the OS level
        System.getenv()
              .forEach((key, value) -> System.out.println(String.format("Env Key %s, Value %s", key, value)));

        //Passed to JVM at run time with -D command
        System.getProperties()
              .forEach((key, value) -> System.out.println(String.format("Property Key %s, Value %s", key, value)));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Hello", "JUnite", "Testing"})
    void testStringLengthIsGreaterThanFive(String value) {
        org.assertj.core.api.Assertions.assertThat(value.length())
                                       .isGreaterThanOrEqualTo(5)
                                       .isLessThanOrEqualTo(7);

        org.assertj.core.api.Assertions.assertThat(value)
                                       .contains("e")
                                       .hasSizeBetween(5, 7);

    }
}