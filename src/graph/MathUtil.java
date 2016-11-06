package graph;

import java.math.BigDecimal;

/**
 * This class contains math utilties.
 */
public class MathUtil {

    /**
     * Maximum amount of decimals of floating point numbers
     */
    public static final int FLOATING_POINT_PRECISION = 30;

    /**
     * Checks if the {@code bigDecimal} exceeds the limits of an {@link Integer}.
     *
     * @param bigDecimal the {@link BigDecimal} to check
     * @return null if the {@code bigDecimal} exceeds the limits of an {@link Integer}
     */
    public static BigDecimal checkIntOverflow(BigDecimal bigDecimal) {
        if (bigDecimal.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) == 1
                || bigDecimal.compareTo(BigDecimal.valueOf(Integer.MIN_VALUE)) == -1) {
            return null;
        }
        return bigDecimal;
    }

    /**
     * Checks if a line between two points is rising.
     *
     * @param y1 y-coordinate of the first point
     * @param y2 y-coordinate of the second point
     * @return true if the line between the two points is rising
     */
    public static boolean isRising(BigDecimal y1, BigDecimal y2) {
        return y1.subtract(y2).compareTo(BigDecimal.ZERO) == -1;
    }

}
