package graph.gui;

import graph.gui.input.KeyEventDispatcher;
import graph.gui.input.MouseEventDispatcher;
import graph.gui.input.WindowEventDispatcher;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;

/**
 * This class builds the top-level window.
 */
public class GraphFrame extends JFrame {

    /**
     * Size of the graph area
     */
    private final static Dimension GRAPH_PANEL_DIMENSION = new Dimension(1000, 600);

    /**
     * Size of the input area
     */
    private final static Dimension INPUT_PANEL_DIMENSION = new Dimension(300, 600);

    /**
     * The graph panel
     */
    private final GraphPanel graphPanel;

    /**
     * The input panel
     */
    private final InputPanel inputPanel;

    /**
     * Creates a top-level window that contains all the required panels.
     */
    public GraphFrame() {
        setTitle("Graph Plotter");
        setLayout(new BorderLayout());
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        // add the components
        graphPanel = new GraphPanel(GRAPH_PANEL_DIMENSION);
        inputPanel = new InputPanel(INPUT_PANEL_DIMENSION, this);
        add(inputPanel, BorderLayout.WEST);
        add(graphPanel, BorderLayout.EAST);
        setJMenuBar(new MenuBar(graphPanel));

        // add input listeners
        MouseEventDispatcher mouseEventDispatcher = new MouseEventDispatcher(this, graphPanel);
        addMouseWheelListener(mouseEventDispatcher);
        addMouseMotionListener(mouseEventDispatcher);
        addMouseListener(mouseEventDispatcher);
        addKeyListener(new KeyEventDispatcher(graphPanel));
        addWindowListener(new WindowEventDispatcher(this));

        pack();
        setLocationRelativeTo(null);
    }

    /**
     * Returns the graph panel.
     * @return the graph panel
     */
     GraphPanel getGraphPanel() {
        return graphPanel;
    }

    /**
     * Returns the input panel.
     * @return the input panel
     */
     InputPanel getInputPanel() {
        return inputPanel;
    }

    static {
        try {
            UIManager.setLookAndFeel(new MetalLookAndFeel());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
    }

}
