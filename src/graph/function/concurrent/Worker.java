package graph.function.concurrent;

import graph.MathUtil;
import graph.function.Formula;
import graph.gui.axis.FiniteDomain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * Worker class to divide the workload.
 */
class Worker implements Runnable {

    /**
     * Start value to mitigate division by zero
     */
    private static final BigDecimal START_VALUE = new BigDecimal("0.000001");

    /**
     * The formula to compute values for
     */
    private final Formula f;

    /**
     * The domain of the formula
     */
    private final FiniteDomain d;

    /**
     * the computed values
     */
    private final ArrayList<BigDecimal> values;

    /**
     * The step size
     */
    private BigDecimal stepSize;

    /**
     * Creates a {@link Worker} object that computes value for a
     * formula.
     *
     * @param f        the formula
     * @param d        the domain of the formula
     * @param stepSize the step size
     */
    Worker(Formula f, FiniteDomain d, BigDecimal stepSize) {
        this.f = f;
        this.d = d;
        this.stepSize = stepSize;

        // reduce growing time
        values = new ArrayList<>(d.getDistance().intValue());
    }

    @Override
    public void run() {
        int max = d.getEnd().divide(stepSize, MathUtil.FLOATING_POINT_PRECISION, RoundingMode.UP).intValue();
        for (int i = d.getStart().divide(stepSize, MathUtil.FLOATING_POINT_PRECISION, RoundingMode.UP).intValue(); i < max; i++) {
            BigDecimal x = BigDecimal.valueOf(i).multiply(stepSize).add(START_VALUE);
            values.add(Formula.calculateSafe(f, x));
        }
    }

    /**
     * Returns the computed values.
     *
     * @return the computed values
     */
    ArrayList<BigDecimal> getValues() {
        return values;
    }
}
