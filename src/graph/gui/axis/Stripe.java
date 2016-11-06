package graph.gui.axis;

/**
 * This class represents a stripe on the x-axis or y-axis.
 */
public final class Stripe {

    /**
     * id of the stripe
     */
    private final int id;

    /**
     * absolute x or y position
     */
    private int position;

    /**
     * Creates a {@link Stripe} object.
     *
     * @param position the absolute x or y position
     * @param id       the id of the stripe
     */
    Stripe(int position, int id) {
        this.position = position;
        this.id = id;
    }

    /**
     * Returns the absolute position of the stripe. This
     * can be an x or a y value.
     *
     * @return the absolute position of the stripe
     */
    public int getPosition() {
        return position;
    }

    /**
     * Increments the position of the stripe with {@code dPosition}.
     *
     * @param dPosition delta position
     */
    void incrementPosition(int dPosition) {
        this.position += dPosition;
    }

    /**
     * Returns the id of the stripe.
     *
     * @return the id of the stripe
     */
    public int getId() {
        return id;
    }

}
