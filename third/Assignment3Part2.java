package com.shpp.p2p.cs.ldebryniuk.assignment2;

import com.shpp.cs.a.console.TextProgram;

/**
 * This class resolves The Hailstone Sequence of Douglas Hofstadter
 */
public class Assignment3Part2 extends TextProgram {

    /**
     * This is the starting method of the program
     */
    public void run() {
        int n = getInputFromUser();

        calculateHailstoneSequence(n);
    }

    /**
     * The following method is used for receiving the input number from a user
     *
     * @return the input number
     */
    private int getInputFromUser() {
        int n = 1;

        try {
            n = readInt("Enter a number: "); // getting input from a user
            while (n < 2) { // there is no point in calculcations if n < 2
                n = readInt("input number can not be less than 2, Enter a number: ");
            }
        } catch (Exception e) {
            System.out.println("ooops wrong input o_O");
        }

        return n;
    }

    /**
     * This method calculates The Hailstone Sequence of Douglas Hofstadter
     *
     * @param n represents input number received from the user
     */
    private void calculateHailstoneSequence(int n) {
        int initialNum;

        // when n = 1 all the calculations should be finished
        while (n > 1) {
            initialNum = n; // represents the number at the beginning of iteration

            if (n % 2 == 0) {
                n /= 2; // logic for even numbers
                System.out.println(initialNum + " is even so I take half: " + n);
            } else {
                n = n * 3 + 1; // logic for odd numbers
                System.out.println(initialNum + " is odd so I make 3n + 1: " + n);
            }
        }
    }

}