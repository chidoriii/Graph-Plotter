package graph.gui.input;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * This class dispatches all the window events.
 */
public final class WindowEventDispatcher extends WindowAdapter {

    /**
     * Available options for the dialogue
     */
    private static final String[] OPTIONS = {"Yes", "No"};

    /**
     * Displays the dialogue on top of this component.
     */
    private final Component component;

    /**
     * Creates a {@link WindowEventDispatcher}.
     *
     * @param component the background component
     */
    public WindowEventDispatcher(Component component) {
        this.component = component;
    }

    @Override
    public void windowClosing(WindowEvent e) {
        int option = JOptionPane.showOptionDialog(
                component, "Are you sure you want to exit?", "Graph",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, OPTIONS, OPTIONS[1]
        );
        if (option == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

}
