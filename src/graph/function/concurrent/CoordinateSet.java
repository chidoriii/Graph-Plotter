package graph.function.concurrent;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * This class represents two sets of coordinates.
 */
public final class CoordinateSet {

    /**
     * The set of x-coordinates
     */
    private ArrayList<BigDecimal> x;

    /**
     * The set of y-coordinates
     */
    private ArrayList<BigDecimal> y;

    /**
     * Returns the set of x-coordinates.
     * @return the set of x-coordinates
     */
    public ArrayList<BigDecimal> getX() {
        return x;
    }

    /**
     * Changes the set of x-coordinates.
     * @param x the new set of x-coordinates
     */
    void setX(ArrayList<BigDecimal> x) {
        this.x = x;
    }

    /**
     * Returns the set of y-coordinates.
     * @return the set of y-coordinates
     */
    public ArrayList<BigDecimal> getY() {
        return y;
    }

    /**
     * Changes the set of y-coordinates.
     * @param y the new set of y-coordinates
     */
    public void setY(ArrayList<BigDecimal> y) {
        this.y = y;
    }

}
