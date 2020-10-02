package com.shpp.p2p.cs.ldebryniuk.assignment11.rpn;

import java.util.Arrays;

/**
 * Tests different cases of input
 */
public class Tester {

    private final Calculator calc = new Calculator();

    /**
     * Tests for different cases of input
     */
    public void runTests() {
        String[][] tests = {
                {"5+3+4+6+7"}, {"25.00000"},
                {"5+3-4"}, {"4.00000"},
                {"5*3+4"}, {"19.00000"},
                {"5+3*4"}, {"17.00000"},
                {"1+3^2+2^3"}, {"18.00000"},
                {"5-10+3^2"}, {"4.00000"},
                {"2*2*2^2-4"}, {"12.00000"},
                {"10*-3"}, {"-30.00000"},
                {"-3*10"}, {"-30.00000"},
                {"-5^-5"}, {"-0.00032"},
                {"5^-5"}, {"0.00032"},
                {"5^5"}, {"3125.00000"},
                {"-5^5"}, {"-3125.00000"},
                {"10*-3^3"}, {"-270.00000"},
                {"1+3^2"}, {"10.00000"},
                {"3/4^-2"}, {"48.00000"},
                {"3/-4^-2"}, {"48.00000"},
                {"-3/-4^-2"}, {"-48.00000"},
                {"1.0+ 2"}, {"3.00000"},
                {"3+2,0"}, {"5.00000"},
                {"1+3*2"}, {"7.00000"},
                {"-10+3"}, {"-7.00000"},
                {"0-5"}, {"-5.00000"},
                {"3*4"}, {"12.00000"},
                {"3*4*10"}, {"120.00000"},
                {"3*4*10/10"}, {"12.00000"},
                {"3/4"}, {"0.75000"},
                {"5-3"}, {"2.00000"},
                {"2-3"}, {"-1.00000"},
                {"-2-3"}, {"-5.00000"},
                {"11-10/-2"}, {"16.00000"},
                {"2.33*2"}, {"4.66000"},
                {"11-3^2+3^3"}, {"29.00000"},

                {"5+a", "a=5"}, {"10.00000"}, // with args below
                {"a+5", "a=5"}, {"10.00000"},
                {"11-10/-a", "a=5"}, {"13.00000"},
                {"11-10/-a", "a=-5"}, {"9.00000"},
                {"11+10/-a", "a=-5"}, {"13.00000"},
                {"5^a", "a = 4"}, {"625.00000"},
                {"5^-a", "a=4"}, {"0.00160"},
                {"-3*a^2", "a=2"}, {"-12.00000"},
                {"-3*a^2", "a=-2"}, {"-12.00000"},
                {"2,0*a", "a=-2"}, {"-4.00000"},
                {"-a*2.0", "a=-2"}, {"4.00000"},
                {"-a*2*4", "a=-2"}, {"16.00000"},
                {"2-a", "a=-2"}, {"4.00000"},
                {"3*a", "a=-2"}, {"-6.00000"},
                {"3+a", "a=-3"}, {"0.00000"},
                {"3-a", "a=-3"}, {"6.00000"},
                {"2-a*5", "a=-2"}, {"12.00000"},
                {"a+a", "a=-2"}, {"-4.00000"},
                {"a*a", "a=2"}, {"4.00000"},
                {"a*a", " a=4"}, {"16.00000"},
                {"a*a", "a=-4"}, {"16.00000"},
                {"-a*a", "a=-4"}, {"-16.00000"},
                {"-a*a", "a=4"}, {"-16.00000"},
                {"a+5", "a=-4"}, {"1.00000"},
                {"5-a", "a=-4"}, {"9.00000"},
                {"-a+5", "a=-4"}, {"9.00000"},
                {"-a*a", "a=-4"}, {"-16.00000"},
                {"2-a*a", "a=2"}, {"-2.00000"},
                {"5^-a", "a=-4"}, {"625.00000"},
                {"5-a*a", "a=-2"}, {"1.00000"},
                {"a+55*a", "a=10"}, {"560.00000"},
                {"1+a*2", "a=2"}, {"5.00000"},
                {"1+a*2/2", "a=2"}, {"3.00000"},
                {"1*1-a^2", "a=2"}, {"-3.00000"},
                {"1+a*2/2-1", "a=2"}, {"2.00000"},
                {"11*a^3", "a=-2"}, {"-88.00000"},
                {"1-2*a+3", "a=-5"}, {"14.00000"},
                {"-a+5.0", "a=-2.000"}, {"7.00000"},
                {"5-10+a^2-b^c", "a=5", "b=5", "c=5"}, {"-3105.00000"},
                {"d-10+a^2-b^c", "c=-5", "a= 5", "b= -5", "d=33"}, {"48.00032"},
                {"d-10+a^2-b^c+a", "c=-5", "a= 5", "b= -5", "d=33"}, {"53.00032"},

                {"(3+3)"}, {"6.00000"}, // with parentheses below
                {"3+(3+3)"}, {"9.00000"},
                {"(1+3)*2"}, {"8.00000"},
                {"(1+3)^2"}, {"16.00000"},
                {"24+(1+3)^2"}, {"40.00000"},
                {"2+(3+5)+(1+3)^2"}, {"26.00000"},
                {"2+(3+5)+(1+3^2)^2"}, {"110.00000"},
                {"3+3+(-3)"}, {"3.00000"},
                {"3+3-(-3)"}, {"9.00000"},
                {"3+3-(5-10)"}, {"11.00000"},
                {"3+3-(8-(2+2))"}, {"2.00000"},
                {"3-(8-(2+2))+33"}, {"32.00000"},
                {"3-(8-(2+2)^2)+33"}, {"44.00000"},
                {"3-(8-(2+2+(3^2))^2)+33"}, {"197.00000"},
                {"3-(8-(2+2+(3^2))^2)+(33+53)"}, {"250.00000"},

//                {"sin(3)"}, {"0.1411200080598672"}, // functions below
//                {"-sin(3)"}, {"0.1411200080598672"}, // weird
//                {"sin(3+3)"}, {"-0.27941549819892586"},
//                {"sin(3+3) + cos(4^2)"}, {"-1.237074979"},
//                {"sin(3+3) + sin(4^2)"}, {"-0.567318815"},
//                {"1+(2+3*(4+5-sin(45*cos(a))))/7", "a=55"}, {"4.783224812"},
//                {"log10(100)"}, {"2.00000"},

//                {"5/0"}, {"Infinity"}, // special cases, check for crashes and infinite loops are below
//                {"22"}, {"22.00000"},
//                {"1-2*a+3", "a=a=-5"}, {"1-2*a+3"}
//                {"2(5+5)"}, {"20"},
//                {"(5+5)2"}, {"20"},
//                {"2(5+5)2"}, {"40"},
//                {"(3+4)(2+2)"}, {"28"},
//                {"3-(8-a^2)+33", "a=(2+2)"}, {"44"}, // parentheses + args
//                {"3-a+33", "a=(8-(2+2)^2)"}, {"44"},
//                {"3-a^2", "a=(8-(2+2)^2)"}, {"-61"},
//                {"-5-10+a^2-b^codi+10000.44", "codi =5", "a=5", "b=5"}, {"6885.44"}, // not mendatory with args
//                {"dodo-10*3", "dodo=33"}, {"3"},
//                {"a2", "a=5"}, {"10"},
//                {"2a", "a=5"}, {"10"},
//                {"3a", "a=-3"}, {"-9"},
//                {"3 a", "a=-3"}, {"-9"},
//                {"32+a2", "a=4"}, {"40"},
//                {"33+2a", "a=5"}, {"43"},
//                {"a2+b/2-c", "a=5", "b=6", "c=3"}, {"10"},
//                {"4^2^2"}, {"256"},
        };

        for (int i = 0; i < tests.length; i += 2) {
            String res = calc.runCalc(tests[i]);
            if (res != null && res.equals(tests[i + 1][0])) {
                System.out.println("  Pass: " + Arrays.toString(tests[i]) + " Result: " + res);
            } else {
                System.out.println("! FAIL: " + Arrays.toString(tests[i]) +
                        " Expected " + tests[i + 1][0] + " Got: " + res);
                return;
            }
        }

        System.out.println("passed all tests");
    }

}