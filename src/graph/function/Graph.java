package graph.function;

import java.awt.*;

/**
 * This class represents an abstract graph.
 */
abstract class Graph {

    /**
     * The default color of the graph
     */
    private static final Color DEFAULT_COLOR = Color.BLACK;

    /**
     * The name of the graph
     */
    private String name;

    /**
     * The color of the graph
     */
    private Color color;

    /**
     * Condition to draw the derivative
     */
    private boolean drawDerivative;

    /**
     * Condition to automatically delete singularities
     */
    private boolean autoDeleteSingularities;

    /**
     * Condition to automatically extend limits towards infinity
     */
    private boolean autoExtendLimits;

    /**
     * Creates a {@link Graph} object.
     *
     * @param name  the name of the graph
     * @param color the color of the graph
     */
    Graph(String name, Color color) {
        setName(name);
        setColor(color);

        autoDeleteSingularities(true);
        autoCorrectLimits(true);
    }

    /**
     * Returns the name of the graph.
     *
     * @return the name of the graph
     */
    public String getName() {
        return name;
    }

    /**
     * Changes the name of the graph. The new name cannot
     * be {@code null}.
     *
     * @param name the new name of the graph
     */
    public void setName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("The name of the graph cannot be null");
        }
        this.name = name;
    }

    /**
     * Returns the color({@link Color}) of the graph.
     *
     * @return the color({@link Color})of the graph
     */
    public Color getColor() {
        return color;
    }

    /**
     * Sets the color({@link Color}) of the graph. The default
     * color is used if the new color is {@code null}.
     *
     * @param color the new color({@link Color})
     */
    public void setColor(Color color) {
        this.color = color == null ? DEFAULT_COLOR : color;
    }

    /**
     * Determines whether singularities should automatically be deleted.
     *
     * @return {@code true} if singularities are automatically being deleted,
     * {@code false} otherwise
     */
    public boolean autoDeleteSingularities() {
        return autoDeleteSingularities;
    }

    /**
     * Sets the state whether singularities should automatically be deleted.
     *
     * @param flag whether singularities should automatically be deleted
     */
    public void autoDeleteSingularities(boolean flag) {
        this.autoDeleteSingularities = flag;
    }

    /**
     * Determines whether limits towards infinity should automatically be extended.
     *
     * @return {@code true} if limits towards infinity should automatically be extended,
     * {@code false} otherwise
     */
    public boolean autoCorrectLimits() {
        return autoExtendLimits;
    }

    /**
     * Sets the state whether limits towards infinity should automatically be extended.
     *
     * @param flag whether limits towards infinity should automatically be extended
     */
    public void autoCorrectLimits(boolean flag) {
        this.autoExtendLimits = flag;
    }

    /**
     * Determines whether the derivative should be drawn.
     *
     * @return {@code true} if the derivative should be drawn, {@code false} otherwise
     */
    public boolean drawDerivative() {
        return drawDerivative;
    }

    /**
     * Sets the state whether the derivative should be drawn.
     *
     * @param flag whether the derivative should be drawn
     */
    public void drawDerivative(boolean flag) {
        this.drawDerivative = flag;
    }

    /**
     * Compares this graph to the specified object.  The result is {@code
     * true} if and only if the argument is not {@code null} and is a {@link
     * Graph} object with the same {@code name} as this
     * object.
     *
     * @param o the object to compare this {@link Graph} against
     * @return {@code true} if the given object represents a {@link Graph}
     * equivalent to this {@link Graph}, {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o instanceof Graph) {
            Graph that = (Graph) o;
            return this.name.equals(that.name);
        }
        return false;
    }

    /**
     * Returns a hash code for this graph.
     *
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
