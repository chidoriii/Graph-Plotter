package graph.gui;

import graph.gui.input.WindowEventDispatcher;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This class constructs a menu bar.
 */
class MenuBar extends JMenuBar implements ActionListener {

    /**
     * Date and time format of the saved image
     */
    private static final DateFormat dateFormat = new SimpleDateFormat("HH-mm-ss_dd-MM-yyyy");

    /**
     * The {@link GraphPanel} instance
     */
    private final GraphPanel graphPanel;

    /**
     * Creates a {@link MenuBar} object.
     *
     * @param graphPanel the graph panel
     */
    MenuBar(GraphPanel graphPanel) {
        this.graphPanel = graphPanel;

        JMenu file = new JMenu("File");
        file.add(createItem("Save"));
        file.add(createItem("Quit"));

        add(file);
    }

    /**
     * Creates a menu item.
     *
     * @param name name of the menu item
     * @return a menu item
     */
    private JMenuItem createItem(String name) {
        JMenuItem menuItem = new JMenuItem(name);
        menuItem.addActionListener(this);
        menuItem.setActionCommand(name);
        return menuItem;
    }

    /**
     * Takes a screenshot of the graphs and saves them on the disk.
     */
    private void saveImage() {
        JFileChooser fileChooser = new JFileChooser(new File(System.getProperty("user.dir")));
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.showOpenDialog(graphPanel);
        File dir = fileChooser.getSelectedFile();

        if (dir.exists()) {
            File imageFile = new File(dir, dateFormat.format(new Date()) + ".jpg");
            if (!imageFile.exists()) {
                BufferedImage image = new BufferedImage(graphPanel.getWidth(), graphPanel.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
                graphPanel.paintAll(image.getGraphics());
                try {
                    ImageIO.write(image, "JPEG", imageFile);
                    JOptionPane.showConfirmDialog(graphPanel, "Saved image to: " + imageFile.getAbsolutePath(), "Confirmation", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
                } catch (IOException e) {
                    JOptionPane.showConfirmDialog(graphPanel, "Failed to write image: " + e.getMessage(), "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
                    e.printStackTrace();
                }
            } else {
                JOptionPane.showConfirmDialog(graphPanel, "Image already exists!", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showConfirmDialog(graphPanel, "Invalid directory selected!", "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Save":
                saveImage();
                break;
            case "Quit":
                new WindowEventDispatcher(graphPanel).windowClosing(null);
                break;
        }
    }

}
