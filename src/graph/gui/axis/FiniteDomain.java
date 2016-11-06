package graph.gui.axis;

import java.math.BigDecimal;

/**
 * This class represents a finite domain.
 */
public final class FiniteDomain {

    /**
     * The start of the domain (inclusive).
     */
    private final BigDecimal start;

    /**
     * The end of the domain (inclusive).
     */
    private final BigDecimal end;

    /**
     * Creates a finite domain.
     *
     * @param start the start of the domain (inclusive)
     * @param end   the end of the domain (inclusive)
     */
    public FiniteDomain(BigDecimal start, BigDecimal end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the start of the domain (inclusive).
     *
     * @return the start of the domain (inclusive)
     */
    public BigDecimal getStart() {
        return start;
    }

    /**
     * Returns the end of the domain (inclusive).
     *
     * @return the end of the domain (inclusive
     */
    public BigDecimal getEnd() {
        return end;
    }

    /**
     * Returns the distance between the start and the end.
     *
     * @return the distance between the start and the end
     */
    public BigDecimal getDistance() {
        return end.subtract(start);
    }

}
