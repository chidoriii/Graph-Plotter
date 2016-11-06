package graph.gui;


import graph.MathUtil;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * This class controls all the zoom elements.
 */
public class ZoomHandler {

    /**
     * Limit for zooming in
     */
    private static final BigDecimal ZOOM_IN_LIMIT = new BigDecimal("400");

    /**
     * Limit for zooming out
     */
    private static final BigDecimal ZOOM_OUT_LIMIT = new BigDecimal("4");


    /**
     * Size of the graph panel
     */
    private final Dimension panelSize;


    /**
     * Horizontal stretching
     */
    private BigDecimal horizontalStretch = new BigDecimal("10");

    /**
     * vertical stretching
     */
    private BigDecimal verticalStretch = new BigDecimal("10");


    /**
     * The current width of a graph
     */
    private BigDecimal currentWidth;

    /**
     * The current height of a graph
     */
    private BigDecimal currentHeight;

    /**
     * Creates a {@link ZoomHandler} instance.
     * @param panelSize the size of the graph panel
     */
    ZoomHandler(Dimension panelSize) {
        this.panelSize = panelSize;
        updateFieldOfView();
    }

    /**
     * Zooms in on the graph.
     *
     * @return {@code true} if the action was successful, else {@code false}
     */
    boolean zoomIn() {
        BigDecimal newHorizontalStretch = horizontalStretch.add(BigDecimal.ONE);
        BigDecimal newVerticalStretch = verticalStretch.add(BigDecimal.ONE);
        if (newHorizontalStretch.compareTo(ZOOM_IN_LIMIT) == -1 && newVerticalStretch.compareTo(ZOOM_IN_LIMIT) == -1) {
            horizontalStretch = newHorizontalStretch;
            verticalStretch = newVerticalStretch;
            updateFieldOfView();
            return true;
        }
        return false;
    }

    /**
     * Zooms out on the graph.
     *
     * @return {@code true} if the action was successful, else {@code false}
     */
    boolean zoomOut() {
        BigDecimal newHorizontalStretch = horizontalStretch.subtract(BigDecimal.ONE);
        BigDecimal newVerticalStretch = verticalStretch.subtract(BigDecimal.ONE);
        if (newHorizontalStretch.compareTo(ZOOM_OUT_LIMIT) == 1 && newVerticalStretch.compareTo(ZOOM_OUT_LIMIT) == 1) {
            horizontalStretch = newHorizontalStretch;
            verticalStretch = newVerticalStretch;
            updateFieldOfView();
            return true;
        }
        return false;
    }

    /**
     * Updates the field of view.
     */
    private void updateFieldOfView() {
        currentWidth = BigDecimal.valueOf(panelSize.width / 2).multiply(
                BigDecimal.valueOf(1).divide(horizontalStretch, MathUtil.FLOATING_POINT_PRECISION, RoundingMode.HALF_UP)
        );
        currentHeight = BigDecimal.valueOf(panelSize.height / 2).multiply(
                BigDecimal.valueOf(1).divide(verticalStretch, MathUtil.FLOATING_POINT_PRECISION, RoundingMode.HALF_UP)
        );
    }

    /**
     * Returns the horizontal stretch.
     *
     * @return the horizontal stretch
     */
    public BigDecimal getHorizontalStretch() {
        return horizontalStretch;
    }

    /**
     * Returns the vertical stretch.
     *
     * @return the vertical stretch
     */
    public BigDecimal getVerticalStretch() {
        return verticalStretch;
    }

    /**
     * Returns the current width.
     *
     * @return the current width
     */
    public BigDecimal getCurrentWidth() {
        return currentWidth;
    }

    /**
     * Returns the current height.
     *
     * @return the current height
     */
    public BigDecimal getCurrentHeight() {
        return currentHeight;
    }

}
