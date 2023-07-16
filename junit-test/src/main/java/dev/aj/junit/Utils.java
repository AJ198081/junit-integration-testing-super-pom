package dev.aj.junit;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Utils {

    public double addTwoNumbers(double x, double y) {
        return x + y;
    }

    public boolean checkForNull(String input) {
        return !Objects.nonNull(input);
    }

    public boolean checkObjectsSame(Object object1, Object object2) {
        return Objects.equals(object1, object2);
    }

    public String checkElementExistsInAList(List<String> listOfStrings, String stringToBeChecked) {
        return listOfStrings.stream()
                .filter(element -> element.equals(stringToBeChecked))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException(String.format("%s doesn't exist in the provided list", stringToBeChecked)));
    }

}
