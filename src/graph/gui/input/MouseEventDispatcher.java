package graph.gui.input;

import graph.gui.GraphFrame;
import graph.gui.GraphPanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * This class dispatches all the mouse events.
 */
public final class MouseEventDispatcher extends MouseAdapter {

    /**
     * The graph frame
     */
    private final GraphFrame frame;

    /**
     * The graph panel
     */
    private final GraphPanel panel;

    /**
     * Creates a {@link MouseEventDispatcher}.
     *
     * @param frame the graph frame
     * @param panel the graph panel
     */
    public MouseEventDispatcher(GraphFrame frame, GraphPanel panel) {
        this.frame = frame;
        this.panel = panel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        frame.requestFocus();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation() == 1) {
            panel.zoomOut();
        } else {
            panel.zoomIn();
        }
    }

}
