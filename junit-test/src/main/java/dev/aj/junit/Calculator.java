package dev.aj.junit;

public class Calculator {


    public int integerDivision(int dividend, int divisor) {

        int result;

        try {
            result = dividend / divisor;
        } catch (ArithmeticException exception) {
            throw new ArithmeticException(String.format("divisor shouldn't be '%d'", divisor));
        }

        return result;
    }

    public int integerSubtraction(int minuend, int subtrahend) {
        return minuend - subtrahend;
    }

    public int integerAddition(int addend1, int addend2) {
        return addend1 + addend2;
    }
}
