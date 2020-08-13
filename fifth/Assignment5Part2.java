package com.shpp.p2p.cs.ldebryniuk.assignment5;

import com.shpp.cs.a.console.TextProgram;

/*

used resource: https://docs.oracle.com/javase/tutorial/java/data/converting.html
 */
public class Assignment5Part2 extends TextProgram {
    public void run() {
        runProgram();

//        runTests();
    }

    private void runProgram() {
        /* Sit in a loop, reading numbers and adding them. */
        while (true) {
            String n1 = readLine("Enter first number: ");
            if (n1.equals("q")) break;

            String n2 = readLine("Enter second number: ");
            if (n2.equals("q")) break;

            println(n1 + " + " + n2 + " = " + addNumericStrings(n1, n2));
            println("press \"q\" if you would like to stop ");
        }
    }

    /**
     * Given two string representations of non negative integers, adds the
     * numbers represented by those strings and returns the result.
     *
     * @param n1 The first number.
     * @param n2 The second number.
     * @return A String representation of n1 + n2
     */
    private String addNumericStrings(String n1, String n2) {
        final int ASCII_CODE_FOR_ZERO = 48;
        StringBuilder res = new StringBuilder();
        int inMemory = 0;

        if (n1.length() > n2.length()) {
            n2 = addZerosToSmallerNum(n2, (n1.length() - n2.length()));
        } else {
            n1 = addZerosToSmallerNum(n1, (n2.length() - n1.length()));
        }

        // start iterating the string from the end to start
        for (int i = n1.length() - 1; i > -1; i--) {
            // converting from chars to integers
            int firstNum = n1.charAt(i) - ASCII_CODE_FOR_ZERO;
            int secondNum = n2.charAt(i) - ASCII_CODE_FOR_ZERO;
            int sum = firstNum + secondNum;

            if (inMemory == 1) {
                sum++;
                inMemory = 0;
            }

            if (sum > 9) {
                inMemory = 1;
                sum -= 10;
            }

            res.insert(0, sum);
        }

        if (inMemory == 1) res.insert(0, 1);

        return res.toString();
    }

    private String addZerosToSmallerNum(String smallerNum, int numOfZerosToAdd) {
        StringBuilder res = new StringBuilder();
        res.append(smallerNum);

        for (int i = 0; i < numOfZerosToAdd; i++) {
            res.insert(0, 0);
        }

        return res.toString();
    }

    private void runTests() {
        check("7", "7", "14");
        check("77", "77", "154");
        check("777", "777", "1554");
        check("7777", "7777", "15554");
        check("999", "999", "1998");
        check("123", "123", "246");
        check("80", "80", "160");
        check("800", "800", "1600");
        check("1234567890", "1234567890", "2469135780");
        check("800", "80", "880");
        check("6993309969", "43252003274489856000", "43252003281483165969");
    }

    private void check(String n1, String n2, String expectedResult) {
        String result = addNumericStrings(n1, n2);
        if (result.equals(expectedResult)) {
            System.out.printf("  Pass for n1: %s and n2: %s \n", n1, n2);
        } else {
            System.out.printf("! FAIL for n1: %s and n2: %s; Result: %s", n1, n2, result);
        }
    }

}