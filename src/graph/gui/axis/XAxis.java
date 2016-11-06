package graph.gui.axis;


import graph.gui.ZoomHandler;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayDeque;

/**
 * This class represents the x-axis.
 */
public final class XAxis {

    /**
     * Amount of stripes on the x-axis
     */
    public static final int X_AXIS_DISTRIBUTION = 20;
    /**
     * Id of the y-axis
     */
    public static final int Y_AXIS_ID = 0;

    /**
     * Scrolling speed in the horizontal direction
     */
    private static final int HORIZONTAL_SCROLLING_SPEED = 5;


    /**
     * Absolute size of the graph canvas
     */
    private final Dimension panelSize;

    /**
     * Deque that contains the visible stripes
     */
    private final ArrayDeque<Stripe> stripes = new ArrayDeque<>();

    /**
     * Creates a x-axis object.
     *
     * @param panelSize the absolute size of the graph canvas
     */
    public XAxis(Dimension panelSize) {
        this.panelSize = panelSize;

        // initialize the stripes
        int horizontalQuadrantDistribution = X_AXIS_DISTRIBUTION / 2;
        for (int i = 0; i < X_AXIS_DISTRIBUTION; i++) {
            int position = panelSize.width / X_AXIS_DISTRIBUTION * i;
            int id = 0;

            if (i < horizontalQuadrantDistribution) { // negative numbers
                id = -(horizontalQuadrantDistribution - i);
            } else if (i > horizontalQuadrantDistribution) { // positive numbers
                id = i - horizontalQuadrantDistribution;
            }

            stripes.add(new Stripe(position, id));
        }
    }

    /**
     * Moves the x-axis in a horizontal direction with a
     * fixed speed. This updates the position of the
     * stripes and their id.
     *
     * @param direction the direction to move to
     */
    public void move(HorizontalDirection direction) {
        int stripeDistance = panelSize.width / X_AXIS_DISTRIBUTION;
        if (direction.equals(HorizontalDirection.RIGHT)) {
            if (stripes.getFirst().getPosition() - HORIZONTAL_SCROLLING_SPEED < 0) {
                stripes.removeFirst();
                Stripe last = stripes.getLast();
                stripes.addLast(new Stripe(last.getPosition() + stripeDistance, last.getId() + 1));
            }
            for (Stripe stripe : stripes) {
                stripe.incrementPosition(-HORIZONTAL_SCROLLING_SPEED);
            }
        } else {
            if (stripes.getLast().getPosition() + HORIZONTAL_SCROLLING_SPEED >= panelSize.width) {
                stripes.removeLast();
                Stripe first = stripes.getFirst();
                stripes.addFirst(new Stripe(first.getPosition() - stripeDistance, first.getId() - 1));
            }
            for (Stripe stripe : stripes) {
                stripe.incrementPosition(HORIZONTAL_SCROLLING_SPEED);
            }
        }
    }

    /**
     * Returns the current domain of the graph.
     *
     * @param zoomHandler the {@link ZoomHandler}
     * @return the current domain of the graph
     */
    public FiniteDomain getCurrentDomain(ZoomHandler zoomHandler) {
        int stripeDistance = panelSize.width / X_AXIS_DISTRIBUTION;
        int firstStripeId = stripes.getFirst().getId();
        int firstStripePosition = stripes.getFirst().getPosition();
        int absoluteStart = stripeDistance * firstStripeId - firstStripePosition;
        BigDecimal domainStart = BigDecimal.valueOf(absoluteStart).divide(zoomHandler.getHorizontalStretch(), 10, RoundingMode.HALF_UP);
        BigDecimal domainEnd = domainStart.add(zoomHandler.getCurrentWidth().multiply(BigDecimal.valueOf(2)));
        return new FiniteDomain(domainStart, domainEnd);
    }

    /**
     * Returns the x coordinate of the origin.
     *
     * @return the x coordinate of the origin
     */
    public int getCurrentXOrigin() {
        int stripeDistance = panelSize.width / X_AXIS_DISTRIBUTION;
        int firstStripeId = stripes.getFirst().getId();
        int firstStripePosition = stripes.getFirst().getPosition();
        int distance;
        if (firstStripeId >= 0) {
            stripeDistance = -stripeDistance;
        }
        // horizontal distance between the origin and the center of the graph canvas
        distance = stripeDistance * Math.abs(firstStripeId) - panelSize.width / 2 + firstStripePosition;
        return distance;
    }

    /**
     * Returns true if the y-axis is visible on the field
     * of view.
     *
     * @return true if the y-axis is visible
     */
    public boolean yAxisIsVisible() {
        for (Stripe stripe : stripes) {
            if (stripe.getId() == Y_AXIS_ID) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the absolute x coordinate of the y-axis.
     * This method returns -1 if the y-axis is not
     * visible.
     *
     * @return the absolute x coordinate of the y-axis
     */
    public int getYAxisPosition() {
        for (Stripe stripe : stripes) {
            if (stripe.getId() == Y_AXIS_ID) {
                return stripe.getPosition();
            }
        }
        return -1;
    }

    /**
     * Returns the visible stripes from the x-axis.
     *
     * @return the visible stripes from the x-axis
     */
    public ArrayDeque<Stripe> getStripes() {
        return stripes;
    }

    /**
     * A horizontal direction
     */
    public enum HorizontalDirection {
        LEFT, RIGHT
    }

}
