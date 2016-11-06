package graph.gui.input;

import graph.gui.GraphPanel;
import graph.gui.axis.XAxis;
import graph.gui.axis.YAxis;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * This class dispatches all the key events.
 */
public final class KeyEventDispatcher extends KeyAdapter {

    /**
     * The graph panel
     */
    private final GraphPanel graphPanel;

    /**
     * Creates a {@link KeyEventDispatcher}.
     *
     * @param graphPanel the graph panel
     */
    public KeyEventDispatcher(GraphPanel graphPanel) {
        this.graphPanel = graphPanel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                graphPanel.move(YAxis.VerticalDirection.UP);
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                graphPanel.move(YAxis.VerticalDirection.DOWN);
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                graphPanel.move(XAxis.HorizontalDirection.RIGHT);
                break;
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                graphPanel.move(XAxis.HorizontalDirection.LEFT);
                break;
        }
    }

}
