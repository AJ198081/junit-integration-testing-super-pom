package dev.aj;

public class Main {


    public String compute(int input) {
        return input == 0 ?
                String.valueOf(input) :
                (input % 15 == 0) ?
                        "FizzBuzz" :
                        (input % 3 == 0) ?
                                "Fizz" :
                                (input % 5 == 0) ?
                                        "Buzz" :
                                        String.valueOf(input);
    }

    public void computeAndPrint(int input) {
        System.out.println(compute(input));
    }

    public static void main(String[] args) {
        Main mainObject = new Main();
        mainObject.compute(23);
    }
}
