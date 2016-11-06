package graph.function.concurrent;


import graph.MathUtil;
import graph.function.Formula;
import graph.function.ParametricEquation;
import graph.gui.GraphPanel;
import graph.gui.ZoomHandler;
import graph.gui.axis.FiniteDomain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

/**
 * This class is responsible for concurrent calculations.
 */
public class ConcurrentCalculation {

    /**
     * Calculates the coordinates of a formula. This method splits the
     * workload over two threads.
     *
     * @param f                    the formula
     * @param d                    the domain
     * @param zoomHandler          the {@link ZoomHandler}
     * @param stepSize             the step size
     * @param correctSingularities auto correct singularities?
     * @param extendLimits         auto extend limits?
     * @return the computed coordinates of this formula
     */
    public static CoordinateSet calculate(Formula f, FiniteDomain d, ZoomHandler zoomHandler, BigDecimal stepSize, boolean correctSingularities, boolean extendLimits) {
        CoordinateSet coordinateSet = new CoordinateSet();
        ArrayList<BigDecimal> yValues = calculateYValues(f, d, zoomHandler, stepSize, correctSingularities, extendLimits);
        coordinateSet.setY(yValues);

        // calculate and add the x values
        int domainStart = d.getStart().divide(stepSize, MathUtil.FLOATING_POINT_PRECISION, RoundingMode.HALF_UP).intValue();
        int domainEnd = d.getEnd().divide(stepSize, MathUtil.FLOATING_POINT_PRECISION, RoundingMode.HALF_UP).intValue();
        ArrayList<BigDecimal> xValues = new ArrayList<>(yValues.size());
        for (int i = domainStart; i < domainEnd; i++) {
            xValues.add(BigDecimal.valueOf(i).multiply(stepSize));
        }
        coordinateSet.setX(xValues);

        return coordinateSet;
    }

    /**
     * Calculates the coordinates of a parametric equation. This method
     * splits the workload over four threads.
     *
     * @param p                    the parametric equation
     * @param d                    the domain
     * @param zoomHandler          the {@link ZoomHandler}
     * @param correctSingularities auto correct singularities?
     * @param extendLimits         auto extend limits?
     * @return the computed coordinates of this parametric equation
     */
    public static CoordinateSet calculate(ParametricEquation p, FiniteDomain d, ZoomHandler zoomHandler, boolean correctSingularities, boolean extendLimits) {
        CoordinateSet coordinateSet = new CoordinateSet();
        Thread t1 = new Thread(() -> coordinateSet.setX(calculateYValues(p.getX(), d, zoomHandler, GraphPanel.PARAMETRIC_EQUATION_STEP_SIZE, correctSingularities, extendLimits)));
        Thread t2 = new Thread(() -> coordinateSet.setY(calculateYValues(p.getY(), d, zoomHandler, GraphPanel.PARAMETRIC_EQUATION_STEP_SIZE, correctSingularities, extendLimits)));
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return coordinateSet;
    }

    /**
     * Calculates the y-coordinates of a {@link Formula}. This method
     * splits the workload over two threads.
     *
     * @param f                    the formula
     * @param d                    the domain
     * @param zoomHandler          the {@link ZoomHandler}
     * @param stepSize             the step size
     * @param correctSingularities auto correct singularities?
     * @param extendLimits         auto extend limits?
     * @return an {@link ArrayList} with computed the y-coordinates
     */
    private static ArrayList<BigDecimal> calculateYValues(Formula f, FiniteDomain d, ZoomHandler zoomHandler, BigDecimal stepSize, boolean correctSingularities, boolean extendLimits) {
        BigDecimal workLoad = d.getStart().add(d.getDistance().divide(BigDecimal.valueOf(2), MathUtil.FLOATING_POINT_PRECISION, RoundingMode.UP));
        ArrayList<BigDecimal> values = new ArrayList<>(d.getDistance().intValue());
        Worker worker1 = new Worker(f, new FiniteDomain(d.getStart(), workLoad), stepSize);
        Worker worker2 = new Worker(f, new FiniteDomain(workLoad, d.getEnd()), stepSize);
        Thread t1 = new Thread(worker1);
        Thread t2 = new Thread(worker2);

        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
            values.addAll(worker1.getValues());
            values.addAll(worker2.getValues());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // filter singularities
        if (correctSingularities) {
            for (int i = 1; i < values.size() - 3; i++) {
                int j = i + 1;
                BigDecimal y1 = values.get(i);
                BigDecimal y2 = values.get(j);
                if (y1 != null && y2 != null) {
                    ArrayList<BigDecimal> filtered = filter(values, i, y1, y2, zoomHandler, extendLimits);
                    if (filtered != null) {
                        values = filtered;
                    }
                }
            }
        }

        return values;
    }

    /**
     * Filters the y-coordinates. This method removes the singularities from the graph.
     * It also can auto extend limits towards infinity.
     *
     * @param yValues      the y-coordinates
     * @param index        the index of the current coordinate
     * @param y1           y1
     * @param y2           y2
     * @param zoomHandler  the {@link ZoomHandler}
     * @param extendLimits auto extend limits?
     * @return null if the filter did not catch anything, else the filtered {@link ArrayList}
     */
    private static ArrayList<BigDecimal> filter(ArrayList<BigDecimal> yValues, int index, BigDecimal y1, BigDecimal y2, ZoomHandler zoomHandler, boolean extendLimits) {
        BigDecimal previousY = yValues.get(index - 1);
        BigDecimal nextY = yValues.get(index + 2);
        if (previousY != null && nextY != null) {
            boolean leftRising = MathUtil.isRising(previousY, y1);
            boolean rightRising = MathUtil.isRising(y2, nextY);
            int maxValue = Integer.MAX_VALUE / (zoomHandler.getVerticalStretch().intValue() + 10);
            if (leftRising && rightRising) {
                if (!MathUtil.isRising(y1, y2)) {
                    BigDecimal difference = y2.subtract(y1);
                    if (difference.compareTo(zoomHandler.getCurrentHeight()) == -1) {
                        yValues.set(index + 1, y1);
                        if (extendLimits) {
                            yValues.set(index + 1, BigDecimal.valueOf(maxValue));
                            yValues.set(index + 2, null);
                            yValues.set(index + 3, BigDecimal.valueOf(-maxValue));
                        } else {
                            yValues.set(index + 2, null);
                        }
                        return yValues;
                    }
                }
            } else if (!leftRising && !rightRising) {
                if (MathUtil.isRising(y1, y2)) {
                    BigDecimal difference = y2.subtract(y1);
                    if (difference.compareTo(zoomHandler.getCurrentHeight()) == 1) {
                        yValues.set(index + 1, y1);
                        if (extendLimits) {
                            yValues.set(index + 1, BigDecimal.valueOf(-maxValue));
                            yValues.set(index + 2, null);
                            yValues.set(index + 3, BigDecimal.valueOf(maxValue));
                        } else {
                            yValues.set(index + 2, null);
                        }
                        return yValues;
                    }
                }
            }
        }
        return null;
    }

}
