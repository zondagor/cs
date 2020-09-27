package com.shpp.p2p.cs.ldebryniuk.assignment10;

public class FunctionManager extends Calculator implements Aux{

    private final String[] functions = {"sin", "cos", "tan", "atan", "log10", "log2", "sqrt"};

    /**
     * The following method
     */
    public void goFunctions(StringBuilder formula) {
        for (String func : functions) {
            int indOfFunc;
            if ((indOfFunc = formula.indexOf(func)) > -1) { // while there is at least one func left
                int openParenthesesInd = formula.indexOf("(", indOfFunc);
                int chNumTillClosing = findChNumTillClosing(formula.substring(openParenthesesInd));
                int closeParenthesesInd = openParenthesesInd + chNumTillClosing;
                String withinParentheses = formula.substring(openParenthesesInd + 1, closeParenthesesInd);

                String res = calcFunc(func, withinParentheses);
//                String res = goo(new StringBuilder(withinParentheses));
//                res = calcFunc(func, res);
                formula.replace(indOfFunc, closeParenthesesInd + 1, res);
            }
        }
    }

    /**
     * The following method
     */
    private String calcFunc(String func, String value) {
        double res = -1;

        double val = Double.parseDouble(value);

        switch (func) {
            case "sin":
                res = Math.sin(val);
                break;
            case "cos":
                res = Math.cos(val);
                break;
            case "tan":
                res = Math.tan(val);
                break;
            case "atan":
                res = Math.atan(val);
                break;
            case "sqrt":
                res = Math.sqrt(val);
                break;
            case "log2":
                res = Math.log(val);
                break;
            case "log10":
                res = Math.log10(val);
                break;
            default:
                System.out.println("unknown function please check your formula");
        }

        return String.valueOf(res);
    }
}
