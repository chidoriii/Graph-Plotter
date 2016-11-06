package graph.gui.axis;


import java.awt.*;
import java.util.ArrayDeque;

/**
 * This class represents the y-axis.
 */
public final class YAxis {

    /**
     * Amount of stripes on the y-axis
     */
    public static final int Y_AXIS_DISTRIBUTION = 12;

    /**
     * Id of the x-axis
     */
    public static final int X_AXIS_ID = 0;

    /**
     * Scrolling speed in the vertical direction
     */
    private static final int VERTICAL_SCROLLING_SPEED = 5;

    /**
     * Absolute size of the graph canvas
     */
    private final Dimension panelSize;

    /**
     * Deque that contains the visible stripes
     */
    private final ArrayDeque<Stripe> stripes = new ArrayDeque<>();

    /**
     * Creates a y-axis object.
     *
     * @param panelSize the absolute size of the graph canvas
     */
    public YAxis(Dimension panelSize) {
        this.panelSize = panelSize;

        // initialize the stripes
        int verticalQuadrantDistribution = Y_AXIS_DISTRIBUTION / 2;
        for (int i = 0; i < Y_AXIS_DISTRIBUTION; i++) {
            int position = panelSize.height / Y_AXIS_DISTRIBUTION * i;
            int id = 0;

            if (i < verticalQuadrantDistribution) { // positive numbers
                id = verticalQuadrantDistribution - i;
            } else if (i > verticalQuadrantDistribution) { // negative numbers
                id = verticalQuadrantDistribution - i;
            }

            stripes.add(new Stripe(position, id));
        }
    }

    /**
     * Moves the y-axis in a vertical direction with a
     * fixed speed. This updates the position of the
     * stripes and their id.
     *
     * @param direction the direction to move to
     */
    public void move(VerticalDirection direction) {
        int stripeDistance = panelSize.height / Y_AXIS_DISTRIBUTION;
        if (direction.equals(VerticalDirection.UP)) {
            if (stripes.getLast().getPosition() + VERTICAL_SCROLLING_SPEED >= panelSize.height) {
                stripes.removeLast();
                Stripe first = stripes.getFirst();
                stripes.addFirst(new Stripe(first.getPosition() - stripeDistance, first.getId() + 1));
            }
            for (Stripe stripe : stripes) {
                stripe.incrementPosition(VERTICAL_SCROLLING_SPEED);
            }
        } else {
            if (stripes.getFirst().getPosition() - VERTICAL_SCROLLING_SPEED < 0) {
                stripes.removeFirst();
                Stripe last = stripes.getLast();
                stripes.addLast(new Stripe(last.getPosition() + stripeDistance, last.getId() - 1));
            }
            for (Stripe stripe : stripes) {
                stripe.incrementPosition(-VERTICAL_SCROLLING_SPEED);
            }
        }
    }

    /**
     * Returns the y coordinate of the origin.
     *
     * @return the y coordinate of the origin
     */
    public int getCurrentYOrigin() {
        int stripeDistance = panelSize.height / Y_AXIS_DISTRIBUTION;
        int firstStripeId = stripes.getFirst().getId();
        int firstStripePosition = stripes.getFirst().getPosition();
        int distance;
        if (firstStripeId < 0) {
            stripeDistance = -stripeDistance;
        }
        // vertical distance between the origin and the center of the graph canvas
        distance = stripeDistance * Math.abs(firstStripeId) - panelSize.height / 2 + firstStripePosition;
        return distance;
    }

    /**
     * Returns true if the x-axis is visible on the field
     * of view.
     *
     * @return true if the x-axis is visible
     */
    public boolean xAxisIsVisible() {
        for (Stripe stripe : stripes) {
            if (stripe.getId() == X_AXIS_ID) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the absolute y coordinate of the x-axis.
     * This method returns -1 if the x-axis is not
     * visible.
     *
     * @return the absolute y coordinate of the x-axis
     */
    public int getXAxisPosition() {
        for (Stripe stripe : stripes) {
            if (stripe.getId() == X_AXIS_ID) {
                return stripe.getPosition();
            }
        }
        return -1;
    }

    /**
     * Returns the visible stripes from the y-axis.
     *
     * @return the visible stripes from the y-axis
     */
    public ArrayDeque<Stripe> getStripes() {
        return stripes;
    }

    /**
     * A vertical direction
     */
    public enum VerticalDirection {
        UP, DOWN
    }

}
