package graph.gui;

import graph.MathUtil;
import graph.function.GraphFormula;
import graph.function.ParametricEquation;
import graph.function.concurrent.ConcurrentCalculation;
import graph.function.concurrent.CoordinateSet;
import graph.gui.axis.FiniteDomain;
import graph.gui.axis.Stripe;
import graph.gui.axis.XAxis;
import graph.gui.axis.YAxis;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Iterator;

/**
 * This class represents the panel that contains all the graphs.
 */
public class GraphPanel extends JPanel {

    /**
     * The step size used for parametric equations
     */
    public static final BigDecimal PARAMETRIC_EQUATION_STEP_SIZE = new BigDecimal("0.05");

    /**
     * The color of the x-axis and the y-axis
     */
    private static final Color AXIS_COLOR = new Color(0x1b1c1d);


    /**
     * The {@link ZoomHandler}
     */
    private final ZoomHandler zoomHandler;

    /**
     * The origin of the graph
     */
    private final Point ORIGIN;

    /**
     * The x-axis
     */
    private final XAxis xAxis;

    /**
     * The y-axis
     */
    private final YAxis yAxis;


    /**
     * This set contains all the formulas.
     */
    private HashSet<GraphFormula> formulas = new HashSet<>();

    /**
     * This set contains all the parametric equations.
     */
    private HashSet<ParametricEquation> parametricEquations = new HashSet<>();

    /**
     * The variable step size used for formulas
     */
    private BigDecimal formulaStepSize;

    /**
     * Creates a panel that contains all the graphs.
     *
     * @param panelSize the size of the panel
     */
    GraphPanel(Dimension panelSize) {
        setPreferredSize(panelSize);
        ORIGIN = new Point(panelSize.width / 2, panelSize.height / 2);
        zoomHandler = new ZoomHandler(panelSize);
        xAxis = new XAxis(panelSize);
        yAxis = new YAxis(panelSize);
        updateStepSize();
    }

    /**
     * Adds a {@link GraphFormula} to draw.
     *
     * @param f the formula to draw
     * @return true if the {@link GraphFormula} was successfully added.
     */
    boolean add(GraphFormula f) {
        boolean result = formulas.add(f);
        if (result) {
            repaint();
        }
        return result;
    }

    /**
     * Adds a {@link ParametricEquation} to draw.
     *
     * @param p the parametric equation to draw
     * @return true if the {@link ParametricEquation} was successfully added.
     */
    boolean add(ParametricEquation p) {
        boolean result = parametricEquations.add(p);
        if (result) {
            repaint();
        }
        return result;
    }

    /**
     * Removes a {@link GraphFormula} from the drawing panel.
     *
     * @param name the name of the {@link GraphFormula}  to remove
     */
    void removeGraphFormula(String name) {
        for (Iterator<GraphFormula> iterator = formulas.iterator(); iterator.hasNext(); ) {
            GraphFormula graphFormula = iterator.next();
            if (graphFormula.getName().equals(name)) {
                iterator.remove();
                break;
            }
        }
        repaint();
    }

    /**
     * Removes a {@link ParametricEquation} from the drawing panel.
     *
     * @param name the name of the {@link ParametricEquation}  to remove
     */
    void removeParametricEquation(String name) {
        for (Iterator<ParametricEquation> iterator = parametricEquations.iterator(); iterator.hasNext(); ) {
            ParametricEquation parametricEquation = iterator.next();
            if (parametricEquation.getName().equals(name)) {
                iterator.remove();
                break;
            }
        }
        repaint();
    }

    /**
     * Retrieves a {@link GraphFormula} by name.
     *
     * @param name the name of the {@link GraphFormula}
     * @return the found {@link GraphFormula} or {@code null} if the
     * {@link GraphFormula}  has not been found
     */
    GraphFormula getGraphFormula(String name) {
        for (GraphFormula graphFormula : formulas) {
            if (graphFormula.getName().equals(name)) {
                return graphFormula;
            }
        }
        return null;
    }

    /**
     * Retrieves a {@link ParametricEquation} by name.
     *
     * @param name the name of the {@link ParametricEquation}
     * @return the found {@link ParametricEquation} or {@code null} if the
     * {@link ParametricEquation}  has not been found
     */
    ParametricEquation getParametricEquation(String name) {
        for (ParametricEquation parametricEquation : parametricEquations) {
            if (parametricEquation.getName().equals(name)) {
                return parametricEquation;
            }
        }
        return null;
    }

    /**
     * Returns a set({@link HashSet}) of formulas that are currently being drawn
     * on the panel.
     *
     * @return a set of formulas that are currently being drawn on the panel.
     */
    HashSet<GraphFormula> getFormulas() {
        return formulas;
    }

    /**
     * Returns a set({@link HashSet}) of parametric equations that are currently
     * being drawn
     * on the panel.
     *
     * @return a set of parametric equations that are currently being drawn
     * on the panel.
     */
    HashSet<ParametricEquation> getParametricEquations() {
        return parametricEquations;
    }

    /**
     * Zooms in on the graph.
     */
    public void zoomIn() {
        if (zoomHandler.zoomIn()) {
            updateStepSize();
            repaint();
        }
    }

    /**
     * Zooms out on the graph.
     */
    public void zoomOut() {
        if (zoomHandler.zoomOut()) {
            updateStepSize();
            repaint();
        }
    }

    /**
     * Updates the step size of the formulas. The step size is dependent on the
     * current width and height. The smaller the current width and current
     * height, the smaller the step size. This dynamic step size eliminates
     * jagged graphs.
     */
    private void updateStepSize() {
        formulaStepSize = zoomHandler.getCurrentWidth().divide(
                BigDecimal.valueOf(2000), MathUtil.FLOATING_POINT_PRECISION, RoundingMode.HALF_UP
        );
    }

    /**
     * {@inheritDoc}
     *
     * @param graphics {@inheritDoc}
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        Dimension size = getPreferredSize();
        Graphics2D background = (Graphics2D) graphics;
        BufferedImage buffer = new BufferedImage(size.width, size.height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D g = (Graphics2D) buffer.getGraphics();
        graphics.clearRect(0, 0, size.width, size.height);

        drawPrimaryAxes(g);
        for (GraphFormula graphFormula : formulas) {
            g.setColor(graphFormula.getColor());
            drawGraph(g, computeCoordinateSet(graphFormula));
            if (graphFormula.drawDerivative()) {
                drawGraph(g, computeCoordinateSet(graphFormula.differentiate()));
            }
        }
        for (ParametricEquation parametricEquation : parametricEquations) {
            g.setColor(parametricEquation.getColor());
            drawGraph(g, computeCoordinateSet(parametricEquation));
            if (parametricEquation.drawDerivative()) {
                drawGraph(g, computeCoordinateSet(parametricEquation.differentiate()));
            }
        }

        // double buffering
        background.drawImage(buffer, 0, 0, null);
    }

    /**
     * Computes a {@link CoordinateSet} for a {@link ParametricEquation}.
     *
     * @param p the parametric equation
     * @return the computed {@link CoordinateSet}
     */
    private CoordinateSet computeCoordinateSet(ParametricEquation p) {
        FiniteDomain d = new FiniteDomain(
                BigDecimal.valueOf(getPreferredSize().width / 3).negate(), BigDecimal.valueOf(getPreferredSize().width / 3)
        );
        return ConcurrentCalculation.calculate(p, d, zoomHandler, p.autoDeleteSingularities(), p.autoCorrectLimits());
    }

    /**
     * Computes a {@link CoordinateSet} for a {@link GraphFormula}.
     *
     * @param f the formula
     * @return the computed {@link CoordinateSet}
     */
    private CoordinateSet computeCoordinateSet(GraphFormula f) {
        FiniteDomain d = xAxis.getCurrentDomain(zoomHandler);
        return ConcurrentCalculation.calculate(
                f.getFormula(), d, zoomHandler, formulaStepSize, f.autoDeleteSingularities(), f.autoCorrectLimits()
        );
    }

    /**
     * Draws a graph of a given {@link CoordinateSet}.
     *
     * @param g             graphics
     * @param coordinateSet the coordinate set
     */
    private void drawGraph(Graphics2D g, CoordinateSet coordinateSet) {
        for (int i = 0; i < coordinateSet.getX().size() - 1; i++) {
            int j = i + 1;
            BigDecimal x1 = coordinateSet.getX().get(i);
            BigDecimal y1 = coordinateSet.getY().get(i);
            BigDecimal x2 = coordinateSet.getX().get(j);
            BigDecimal y2 = coordinateSet.getY().get(j);

            if (x1 != null && y1 != null && x2 != null && y2 != null) {
                drawGraphLine(g, x1, y1, x2, y2);
            }
        }
    }

    /**
     * Draws one line of the graph.
     *
     * @param g  graphics
     * @param x1 the first x-coordinate
     * @param y1 the first y-coordinate
     * @param x2 the second x-coordinate
     * @param y2 the second y-coordinate
     */
    private void drawGraphLine(Graphics2D g, BigDecimal x1, BigDecimal y1, BigDecimal x2, BigDecimal y2) {
        int baseX = ORIGIN.x;
        int baseY = ORIGIN.y;
        int movingX = xAxis.getCurrentXOrigin();
        int movingY = yAxis.getCurrentYOrigin();

        x1 = MathUtil.checkIntOverflow(
                BigDecimal.valueOf(baseX).add(x1.multiply(zoomHandler.getHorizontalStretch())).add(BigDecimal.valueOf(movingX))
        );
        y1 = MathUtil.checkIntOverflow(
                BigDecimal.valueOf(baseY).subtract(y1.multiply(zoomHandler.getVerticalStretch())).add(BigDecimal.valueOf(movingY))
        );
        x2 = MathUtil.checkIntOverflow(
                BigDecimal.valueOf(baseX).add(x2.multiply(zoomHandler.getHorizontalStretch())).add(BigDecimal.valueOf(movingX))
        );
        y2 = MathUtil.checkIntOverflow(
                BigDecimal.valueOf(baseY).subtract(y2.multiply(zoomHandler.getVerticalStretch())).add(BigDecimal.valueOf(movingY))
        );

        if (x1 != null && x2 != null && y1 != null && y2 != null) {
            int ix1 = x1.intValue();
            int iy1 = y1.intValue();
            int ix2 = x2.intValue();
            int iy2 = y2.intValue();

            g.drawLine(ix1, iy1, ix2, iy2);
        }
    }

    /**
     * Moves the graph horizontally
     *
     * @param direction the horizontal direction to move towards
     */
    public void move(XAxis.HorizontalDirection direction) {
        xAxis.move(direction);
        repaint();
    }

    /**
     * Moves the graph vertically.
     *
     * @param direction the vertical direction to move towards
     */
    public void move(YAxis.VerticalDirection direction) {
        yAxis.move(direction);
        repaint();
    }

    /**
     * Draws the x-axis, the y-axis and the origin.
     *
     * @param g graphics
     */
    private void drawPrimaryAxes(Graphics2D g) {
        g.setColor(AXIS_COLOR);

        if (yAxis.xAxisIsVisible()) {
            drawXAxis(g);
        }

        if (xAxis.yAxisIsVisible()) {
            drawYAxis(g);
        }

        // draws the indicator for the origin
        if (xAxis.yAxisIsVisible() && yAxis.xAxisIsVisible()) {
            g.drawString("o", xAxis.getYAxisPosition() - 9, yAxis.getXAxisPosition() + 11);
        }
    }

    /**
     * Draws the x-axis
     *
     * @param g graphics
     */
    private void drawXAxis(Graphics2D g) {
        int xAxisYPosition = yAxis.getXAxisPosition();
        // the axis
        g.drawLine(0, xAxisYPosition, getPreferredSize().width, xAxisYPosition);

        // distribution on x-axis
        for (Stripe horizontalStripe : xAxis.getStripes()) {
            // draws the numbers
            if (horizontalStripe.getId() == XAxis.Y_AXIS_ID) {
                continue; // skip the origin
            }
            BigDecimal step = zoomHandler.getCurrentWidth().divide(
                    BigDecimal.valueOf(XAxis.X_AXIS_DISTRIBUTION).divide(
                            BigDecimal.valueOf(2), MathUtil.FLOATING_POINT_PRECISION, RoundingMode.HALF_UP
                    ), MathUtil.FLOATING_POINT_PRECISION, RoundingMode.HALF_UP
            );
            BigDecimal number = step.multiply(BigDecimal.valueOf((horizontalStripe.getId())))
                    .setScale(2, RoundingMode.HALF_UP).stripTrailingZeros();
            String formattedNumber = number.toPlainString();
            int numberWidth = g.getFontMetrics().stringWidth(formattedNumber);
            g.drawString(formattedNumber, horizontalStripe.getPosition() - (numberWidth / 2), xAxisYPosition + 15);

            // draws the stripes
            g.drawLine(horizontalStripe.getPosition(), xAxisYPosition + 3, horizontalStripe.getPosition(), xAxisYPosition - 3);
        }

        // draws the indicator for the x-axis
        g.drawString("x", ORIGIN.x + getPreferredSize().width / 2 - 15, yAxis.getXAxisPosition() + 23);
    }

    /**
     * Draws the y-axis
     *
     * @param g graphics
     */
    private void drawYAxis(Graphics2D g) {
        int yAxisXPosition = xAxis.getYAxisPosition();
        // the axis
        g.drawLine(yAxisXPosition, 0, yAxisXPosition, getPreferredSize().height);

        // distribution on y-axis
        for (Stripe verticalStripe : yAxis.getStripes()) {
            // draws the numbers
            if (verticalStripe.getId() == YAxis.X_AXIS_ID) {
                continue; // skip the origin
            }
            BigDecimal step = zoomHandler.getCurrentHeight().divide(
                    BigDecimal.valueOf(YAxis.Y_AXIS_DISTRIBUTION).divide(
                            BigDecimal.valueOf(2), MathUtil.FLOATING_POINT_PRECISION, RoundingMode.HALF_UP
                    ), MathUtil.FLOATING_POINT_PRECISION, RoundingMode.HALF_UP
            );
            BigDecimal number = step.multiply(BigDecimal.valueOf((verticalStripe.getId())))
                    .setScale(2, RoundingMode.HALF_UP).stripTrailingZeros();
            String formattedNumber = number.toPlainString();
            int numberWidth = g.getFontMetrics().stringWidth(formattedNumber);
            g.drawString(formattedNumber, yAxisXPosition - 6 - (numberWidth), verticalStripe.getPosition() + 4);

            // draws the stripes
            g.drawLine(yAxisXPosition - 3, verticalStripe.getPosition(), yAxisXPosition + 3, verticalStripe.getPosition());
        }

        // draws the indicator for the y-axis
        g.drawString("y", xAxis.getYAxisPosition() - 25, ORIGIN.y - getPreferredSize().height / 2 + 15);
    }

}
