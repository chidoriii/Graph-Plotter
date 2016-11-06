package graph.function;

import java.awt.*;

/**
 * This class represents a normal formula. This class is different
 * from the  {@link Formula} interface. It contains several properties
 * of a formula. The {@link Formula} class cannot contain any
 * properties.
 */
public class GraphFormula extends Graph {

    /**
     * The formula that calculates the y-coordinate
     */
    private Formula formula;

    /**
     * Creates a {@link GraphFormula}.
     *
     * @param name    the name of the formula
     * @param formula the formula for the y-coordinates
     * @param color   the color of the formula
     */
    public GraphFormula(String name, Formula formula, Color color) {
        super(name, color);
        setFormula(formula);
    }

    /**
     * Returns the formula that calculates the y-coordinates.
     *
     * @return the formula that calculates the y-coordinates
     */
    public Formula getFormula() {
        return formula;
    }

    /**
     * Sets the formula that provides the y-coordinates. The
     * new formula cannot be {@code null}.
     *
     * @param formula the new formula for the y-coordinates
     */
    public void setFormula(Formula formula) {
        if (formula == null) {
            throw new IllegalArgumentException("The formula of the graph cannot be null");
        }
        this.formula = formula;
    }

    /**
     * Finds the derivative of the formula and wraps it in a {@link GraphFormula}.
     *
     * @return the derivative as a {@link GraphFormula}
     */
    public GraphFormula differentiate() {
        return new GraphFormula("[" + getName() + "]'", formula.differentiate(), getColor());
    }

    /**
     * Compares this formula to the specified object.  The result is {@code
     * true} if and only if the argument is not {@code null} and is a {@link
     * GraphFormula} object with the same {@code name} as this
     * object.
     *
     * @param o the object to compare this {@link GraphFormula} against
     * @return {@code true} if the given object represents a {@link GraphFormula}
     * equivalent to this {@link GraphFormula}, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof GraphFormula && super.equals(o);
    }

}
