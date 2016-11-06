package graph.parser;

import graph.function.Formula;

import java.math.BigDecimal;

/**
 * This class parses strings to expressions.
 */
public class ExpressionParser {

    /**
     * Creates a {@link Formula} from an expression.
     *
     * @param expression   the expression
     * @param variableName the name of the variable
     * @return a {@link Formula} or null if the expression is invalid
     */
    public static Formula createFormula(String expression, String variableName) {
        if (validExpression(expression, variableName)) {
            return x -> new Expression(expression).setVariable(variableName, x).eval();
        }
        return null;
    }

    /**
     * Evaluates an expression.
     *
     * @param expression the expression to evaluate
     * @return the result as a {@link BigDecimal} or {@code null} if the
     * expression was invalid
     */
    public static BigDecimal evaluateExpression(String expression) {
        try {
            return new Expression(expression).eval();
        } catch (Exception ignored) {
            return null;
        }
    }

    /**
     * Tests if the given expression is valid.
     *
     * @param expression   the expression to test
     * @param variableName the name of the variable
     * @return true if the expression is valid
     */
    private static boolean validExpression(String expression, String variableName) {
        try {
            new Expression(expression).setVariable(variableName, "1").eval();
        } catch (InvalidExpressionException e) {
            return false;
        } catch (Exception ignored) {
            // do nothing for arithmetic errors (e.g. division by zero)
        }
        return true;
    }

}
