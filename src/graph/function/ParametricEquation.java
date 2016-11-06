package graph.function;

import graph.MathUtil;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * This class represents a parametric equation.
 */
public class ParametricEquation extends Graph {

    /**
     * The formula that calculates the x-coordinate
     */
    private Formula x;

    /**
     * The formula that calculates the y-coordinate
     */
    private Formula y;

    /**
     * Creates a {@link ParametricEquation}.
     *
     * @param x     the formula for the x-coordinate
     * @param y     the formula for the y-coordinate
     * @param color the color of the parametric equation
     * @param name  the name of the parametric equation
     */
    public ParametricEquation(Formula x, Formula y, Color color, String name) {
        super(name, color);
        setX(x);
        setY(y);
    }

    /**
     * Returns the formula that calculates the x-coordinates of the parametric equation.
     *
     * @return the formula that calculates the x-coordinates
     */
    public Formula getX() {
        return x;
    }

    /**
     * Changes the formula that provides the x-coordinates of the parametric equation.
     * The new formula cannot be {@code null}.
     *
     * @param x the new formula for the x-coordinates
     */
    public void setX(Formula x) {
        if (x == null) {
            throw new IllegalArgumentException("The formula of the x cannot be null");
        }
        this.x = x;
    }

    /**
     * Returns the formula that calculates the y-coordinates of the parametric equation.
     *
     * @return the formula that calculates the y-coordinates
     */
    public Formula getY() {
        return y;
    }

    /**
     * Sets the formula that provides the y-coordinates of the parametric equation. The
     * new formula cannot be {@code null}.
     *
     * @param y the new formula for the y-coordinates
     */
    public void setY(Formula y) {
        if (y == null) {
            throw new IllegalArgumentException("The formula of the y cannot be null");
        }
        this.y = y;
    }

    /**
     * Performs a translation on the parametric equation.
     *
     * @param dx delta x
     * @param dy delta y
     */
    public void translate(BigDecimal dx, BigDecimal dy) {
        x = x.translate(BigDecimal.ZERO, dx);
        y = y.translate(BigDecimal.ZERO, dy);
    }

    /**
     * Finds the derivative of the parametric equation using the limit definition
     * and wraps it in a {@link GraphFormula}.
     *
     * @return the derivative as a {@link GraphFormula}
     */
    public GraphFormula differentiate() {
        Formula derivative = a -> getY().differentiate().calculate(a).divide(
                getX().differentiate().calculate(a), MathUtil.FLOATING_POINT_PRECISION, RoundingMode.HALF_UP
        );
        return new GraphFormula("[" + getName() + "]'", derivative, getColor());
    }

    /**
     * Compares this parametric equation to the specified object.  The result is {@code
     * true} if and only if the argument is not {@code null} and is a {@link
     * ParametricEquation} object with the same {@code name} as this
     * object.
     *
     * @param o the object to compare this {@link ParametricEquation} against
     * @return {@code true} if the given object represents a {@link ParametricEquation}
     * equivalent to this {@link ParametricEquation}, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof ParametricEquation && super.equals(o);
    }

}
