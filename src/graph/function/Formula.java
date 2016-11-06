package graph.function;

import graph.MathUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.function.DoubleFunction;

/**
 * Represents a formula that accepts a {@link BigDecimal} argument
 * and produces a result.
 */
@FunctionalInterface
public interface Formula {

    /**
     * Imaginary limit that approaches zero.
     */
    BigDecimal LIMIT = new BigDecimal("1E-10");

    /**
     * Applies this formula to the given argument.
     *
     * @param x the argument of the formula
     * @return the result of the formula
     */
    BigDecimal calculate(BigDecimal x);


    /**
     * Reflects the formula to the horizontal direction.
     *
     * @return the reflected {@link Formula}
     */
    default Formula negateHorizontal() {
        return x -> multiplyYAxis(BigDecimal.valueOf(-1)).calculate(x);
    }

    /**
     * Reflects the graph to the vertical direction.
     *
     * @return the reflected {@link Formula}
     */
    default Formula negateVertical() {
        return x -> multiplyXAxis(BigDecimal.valueOf(-1)).calculate(x);
    }

    /**
     * Performs a translation on the formula.
     *
     * @param dx delta x
     * @param dy delta y
     * @return the translated {@link Formula}
     */
    default Formula translate(BigDecimal dx, BigDecimal dy) {
        return x -> calculate(x.add(dx)).add(dy);
    }

    /**
     * Multiplies the whole {@link Formula} by the {@code multiplicand}.
     *
     * @param multiplicand value to be multiplied by this
     *                     {@code Formula}
     * @return {@code this * multiplicand}
     */
    default Formula multiplyXAxis(BigDecimal multiplicand) {
        return x -> calculate(x).multiply(multiplicand);
    }

    /**
     * Multiplies the x variable by {@code 1/multiplicand}.
     *
     * @param multiplicand the multiplicand
     * @return the transformed {@link Formula}
     */
    default Formula multiplyYAxis(BigDecimal multiplicand) {
        return x -> calculate(x.multiply(BigDecimal.ONE.divide(multiplicand, MathUtil.FLOATING_POINT_PRECISION, RoundingMode.HALF_UP)));
    }

    /**
     * Returns a {@link Formula} whose value is the ({@code this} &times; {@code multiplicand}).
     *
     * @param multiplicand value to be multiplied by this {@code Formula}
     * @return {@code this * multiplicand}
     */
    default Formula multiply(Formula multiplicand) {
        return x -> calculate(x).multiply(multiplicand.calculate(x));
    }

    /**
     * Finds the derivative of the formula using the limit definition.
     *
     * @return the derivative
     */
    default Formula differentiate() {
        return x -> (calculate(x.add(LIMIT)).subtract(calculate(x))).divide(LIMIT, MathUtil.FLOATING_POINT_PRECISION, RoundingMode.HALF_UP);
    }

    /**
     * Converts a {@link DoubleFunction} to a {@link Formula}.
     * <br>
     * This method should only be used for testing, therefore
     * it is deprecated. Use {@link #calculate} instead.
     *
     * @param f the double function
     * @return a {@link Formula} instance
     */
    @Deprecated
    static Formula transform(DoubleFunction<Double> f) {
        return x -> BigDecimal.valueOf(f.apply(x.doubleValue()));
    }

    /**
     * Calculates the y value with the x value. This calculation
     * returns null when an exception occurs
     *
     * @param f the formula
     * @param x the x value
     * @return the calculated value or null when an exception
     * occurs (e.g. division by zero)
     */
    static BigDecimal calculateSafe(Formula f, BigDecimal x) {
        try {
            return f.calculate(x);
        } catch (Exception e) {
            return null;
        }
    }

}
