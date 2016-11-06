package graph.gui;

import graph.function.Formula;
import graph.function.GraphFormula;
import graph.function.ParametricEquation;
import graph.parser.ExpressionParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

/**
 * This class represents the panel that contains all the
 * input components.
 */
class InputPanel extends JPanel implements ActionListener {

    /**
     * The graph frame
     */
    private final GraphFrame frame;

    /**
     * GUI components
     */
    private JComboBox<String> graphList;
    private JButton applyButton, deleteButton, colorButton;
    private JCheckBox singularityFilter, extendToInfinity, derivativeCheckBox;
    private JLabel xFormulaLabel, yFormulaLabel, translateLabel, translateXLabel, translateYLabel,
            colorLabel, graphFunctionsTitle, selectGraphTitle, graphPropertiesTitle;
    private JRadioButton translateRadioButton, multiplyRadioButton, noneRadioButton;
    private JTextField xFormulaField, yFormulaField, translateXField, translateYField;
    private JSeparator propertySeparator, functionSeparator, titleSeparator;

    /**
     * Creates an input panel that contains all the input
     * components.
     *
     * @param panelSize  the size of the panel
     * @param graphFrame the parent frame
     */
    InputPanel(Dimension panelSize, GraphFrame graphFrame) {
        this.frame = graphFrame;

        // graph selection
        selectGraphTitle = new JLabel("Select graph:");
        graphList = new JComboBox<>();
        graphList.addItem("Add new graph");
        graphList.addActionListener(this);
        graphList.setActionCommand("select_graph");

        titleSeparator = new JSeparator();

        // graph properties
        graphPropertiesTitle = new JLabel("Graph properties");
        xFormulaLabel = new JLabel("f(x) =");
        xFormulaField = new JTextField();
        yFormulaLabel = new JLabel("y(t) = ");
        yFormulaField = new JTextField();
        colorLabel = new JLabel("Color:");
        colorButton = new JButton();
        colorButton.addActionListener(this);
        colorButton.setActionCommand("change_color");
        singularityFilter = new JCheckBox("Auto delete singularities");
        singularityFilter.addActionListener(this);
        singularityFilter.setActionCommand("filter_singularities");
        extendToInfinity = new JCheckBox("Auto extend limits towards infinity");
        extendToInfinity.addActionListener(this);
        extendToInfinity.setActionCommand("extend_infinity");

        propertySeparator = new JSeparator();

        // graph functions
        graphFunctionsTitle = new JLabel("Graph functions");
        translateLabel = new JLabel("Translate or multiply:");
        ButtonGroup translateGroup = new ButtonGroup();
        translateRadioButton = new JRadioButton("Translate");
        multiplyRadioButton = new JRadioButton("Multiply");
        noneRadioButton = new JRadioButton("None");
        translateGroup.add(translateRadioButton);
        translateGroup.add(multiplyRadioButton);
        translateGroup.add(noneRadioButton);
        noneRadioButton.setSelected(true);
        translateXLabel = new JLabel("x:");
        translateXField = new JTextField();
        translateYLabel = new JLabel("y:");
        translateYField = new JTextField();
        derivativeCheckBox = new JCheckBox("Draw derivative");

        functionSeparator = new JSeparator();

        applyButton = new JButton("Apply");
        applyButton.addActionListener(this);
        applyButton.setActionCommand("apply");
        deleteButton = new JButton("Delete");
        deleteButton.addActionListener(this);
        deleteButton.setActionCommand("delete");

        setLayout();
        setFieldsVisible(false);
        setPreferredSize(panelSize);
    }

    /**
     * Sets a graph as selected in the {@link JComboBox}.
     *
     * @param name the name of the selected graph
     */
    void setSelectedGraph(String name) {
        graphList.setSelectedItem(name);
        actionPerformed(new ActionEvent(graphList, 0, "select_graph"));
    }

    /**
     * Updates the graphs in the {@link JComboBox}.
     */
    void updateGraphList() {
        graphList.removeAllItems();
        for (GraphFormula graphFormula : frame.getGraphPanel().getFormulas()) {
            graphList.addItem(graphFormula.getName());
        }
        for (ParametricEquation p : frame.getGraphPanel().getParametricEquations()) {
            graphList.addItem(p.getName());
        }
        graphList.addItem("Add new graph");
    }

    /**
     * Opens the Create Graph window.
     */
    private void openCreateGraphWindow() {
        CreateGraphWindow createGraphWindow = new CreateGraphWindow(frame);
        createGraphWindow.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String currentItem = (String) graphList.getSelectedItem();
        if (currentItem == null) {
            return;
        }
        switch (e.getActionCommand()) {
            case "select_graph":
                if (currentItem.equals("Add new graph")) {
                    setFieldsVisible(false);
                    openCreateGraphWindow();
                } else if (currentItem.contains(",")) {
                    ParametricEquation p = frame.getGraphPanel().getParametricEquation(currentItem);
                    if (p != null) {
                        setFieldsVisible(true);
                        updateFields(p);
                    }
                } else {
                    GraphFormula graphFormula = frame.getGraphPanel().getGraphFormula(currentItem);
                    if (graphFormula != null) {
                        setFieldsVisible(true);
                        updateFields(graphFormula);
                    }
                }
                break;
            case "change_color":
                changeColor();
                break;
            case "filter_singularities":
                if (!singularityFilter.isSelected() && extendToInfinity.isSelected()) {
                    extendToInfinity.setSelected(false);
                }
                break;
            case "extend_infinity":
                if (!singularityFilter.isSelected() && extendToInfinity.isSelected()) {
                    singularityFilter.setSelected(true);
                }
                break;
            case "apply":
                boolean changed;
                if (currentItem.contains(",")) {
                    changed = editParametricEquation(currentItem);
                } else {
                    changed = editGraphFormula(currentItem);
                }
                if (changed) {
                    frame.getGraphPanel().repaint();
                    updateGraphList();
                }
                break;
            case "delete":
                if (currentItem.contains(",")) {
                    frame.getGraphPanel().removeParametricEquation(currentItem);
                } else {
                    frame.getGraphPanel().removeGraphFormula(currentItem);
                }
                updateGraphList();
                break;
        }

    }

    /**
     * Edits the selected graph.
     *
     * @param name name of the graph to edit
     * @return {@code true} if succeeded, else {@code false}
     */
    private boolean editGraphFormula(String name) {
        GraphFormula oldInstance = frame.getGraphPanel().getGraphFormula(name);
        if (oldInstance != null) {
            GraphFormula modifiedFormula = new GraphFormula(
                    oldInstance.getName(), oldInstance.getFormula(), oldInstance.getColor()
            );
            String errorMessage = "";

            String newFormula = xFormulaField.getText();
            if (!newFormula.equals(name)) {
                Formula f = ExpressionParser.createFormula(newFormula, "x");
                if (f != null) {
                    modifiedFormula.setName(newFormula);
                    modifiedFormula.setFormula(f);
                } else {
                    errorMessage += "Invalid formula!\n";
                }
            }

            if (multiplyRadioButton.isSelected() || translateRadioButton.isSelected()) {
                BigDecimal x = ExpressionParser.evaluateExpression(translateXField.getText());
                BigDecimal y = ExpressionParser.evaluateExpression(translateYField.getText());
                if (x == null && y == null) {
                    errorMessage += "Invalid expression for translation!\n";
                } else if (multiplyRadioButton.isSelected()) {
                    if (x != null) {
                        modifiedFormula.setFormula(modifiedFormula.getFormula().multiplyXAxis(x));
                    }
                    if (y != null) {
                        modifiedFormula.setFormula(modifiedFormula.getFormula().multiplyYAxis(y));
                    }
                } else if (translateRadioButton.isSelected()) {
                    if (x != null && y != null) {
                        modifiedFormula.setFormula(modifiedFormula.getFormula().translate(x, y));
                    } else if (x != null) {
                        modifiedFormula.setFormula(modifiedFormula.getFormula().translate(x, BigDecimal.ZERO));
                    } else {
                        modifiedFormula.setFormula(modifiedFormula.getFormula().translate(BigDecimal.ZERO, y));
                    }
                }
            }

            modifiedFormula.setColor(colorButton.getBackground());
            modifiedFormula.autoDeleteSingularities(singularityFilter.isSelected());
            modifiedFormula.autoCorrectLimits(extendToInfinity.isSelected());
            modifiedFormula.drawDerivative(derivativeCheckBox.isSelected());

            // checks if the formula already exists and is not equal to the old formula
            if (frame.getGraphPanel().getGraphFormula(modifiedFormula.getName()) != null && !modifiedFormula.equals(oldInstance)) {
                errorMessage += "Formula already exists!\n";
            }

            if (!errorMessage.isEmpty()) {
                JOptionPane.showConfirmDialog(
                        frame, errorMessage, "An error occurred", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE
                );
            } else {
                frame.getGraphPanel().removeGraphFormula(oldInstance.getName());
                frame.getGraphPanel().add(modifiedFormula);
                return true;
            }
        }
        return false;
    }

    /**
     * Edits the selected graph.
     *
     * @param name name of the graph to edit
     * @return {@code true} if succeeded, else {@code false}
     */
    private boolean editParametricEquation(String name) {
        ParametricEquation oldInstance = frame.getGraphPanel().getParametricEquation(name);
        if (oldInstance != null) {
            ParametricEquation modifiedParametricEquation = new ParametricEquation(
                    oldInstance.getX(), oldInstance.getY(), oldInstance.getColor(), oldInstance.getName()
            );
            String errorMessage = "";

            String newFormula = xFormulaField.getText() + "," + yFormulaField.getText();
            if (!newFormula.equals(name)) {
                Formula x = ExpressionParser.createFormula(xFormulaField.getText(), "t");
                Formula y = ExpressionParser.createFormula(yFormulaField.getText(), "t");
                if (x != null && y != null) {
                    modifiedParametricEquation.setName(newFormula);
                    modifiedParametricEquation.setX(x);
                    modifiedParametricEquation.setY(y);
                } else {
                    errorMessage += "Invalid formula!\n";
                }
            }

            if (translateRadioButton.isSelected()) {
                BigDecimal x = ExpressionParser.evaluateExpression(translateXField.getText());
                BigDecimal y = ExpressionParser.evaluateExpression(translateYField.getText());
                if (x == null && y == null) {
                    errorMessage += "Invalid expression for translation!\n";
                } else {
                    if (x != null && y != null) {
                        modifiedParametricEquation.translate(x, y);
                    } else if (x != null) {
                        modifiedParametricEquation.translate(x, BigDecimal.ZERO);
                    } else {
                        modifiedParametricEquation.translate(BigDecimal.ZERO, y);
                    }
                }
            }

            modifiedParametricEquation.setColor(colorButton.getBackground());
            modifiedParametricEquation.autoDeleteSingularities(singularityFilter.isSelected());
            modifiedParametricEquation.autoCorrectLimits(extendToInfinity.isSelected());
            modifiedParametricEquation.drawDerivative(derivativeCheckBox.isSelected());

            // checks if the parametric equation already exists and is not equal to the old parametric equation
            if (frame.getGraphPanel().getParametricEquation(modifiedParametricEquation.getName()) != null && !modifiedParametricEquation.equals(oldInstance)) {
                errorMessage += "Parametric equation already exists!\n";
            }

            if (!errorMessage.isEmpty()) {
                JOptionPane.showConfirmDialog(frame, errorMessage, "An error occurred", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
            } else {
                frame.getGraphPanel().removeParametricEquation(oldInstance.getName());
                frame.getGraphPanel().add(modifiedParametricEquation);
                return true;
            }
        }
        return false;
    }

    /**
     * Changes the visibility of the fields.
     *
     * @param flag the flag
     */
    private void setFieldsVisible(boolean flag) {
        Component[] staticComponents = {selectGraphTitle, graphList};
        for (int i = 0; i < getComponentCount(); i++) {
            Component c = getComponent(i);
            boolean isStaticComponent = false;
            for (Component staticComponent : staticComponents) {
                if (c.equals(staticComponent)) {
                    isStaticComponent = true;
                }
            }
            if (!isStaticComponent) {
                c.setVisible(flag);
            }
        }
    }

    /**
     * Arranges the fields for a parametric equation.
     *
     * @param p the parametric equation
     */
    private void updateFields(ParametricEquation p) {
        String[] formulas = p.getName().split(",");
        String xFormula = formulas[0];
        String yFormula = formulas[1];

        xFormulaLabel.setText("x(t) =");
        xFormulaField.setText(xFormula);
        yFormulaLabel.setText("y(t) =");
        yFormulaField.setText(yFormula);
        yFormulaLabel.setVisible(true);
        yFormulaField.setVisible(true);
        colorButton.setBackground(p.getColor());
        singularityFilter.setSelected(p.autoDeleteSingularities());
        extendToInfinity.setSelected(p.autoCorrectLimits());
        derivativeCheckBox.setSelected(p.drawDerivative());
        multiplyRadioButton.setVisible(false);
    }

    /**
     * Arranges the fields for a formula.
     *
     * @param graphFormula the formula
     */
    private void updateFields(GraphFormula graphFormula) {
        xFormulaLabel.setText("f(x) =");
        xFormulaField.setText(graphFormula.getName());
        yFormulaLabel.setVisible(false);
        yFormulaField.setVisible(false);
        colorButton.setBackground(graphFormula.getColor());
        singularityFilter.setSelected(graphFormula.autoDeleteSingularities());
        extendToInfinity.setSelected(graphFormula.autoCorrectLimits());
        derivativeCheckBox.setSelected(graphFormula.drawDerivative());
        multiplyRadioButton.setVisible(true);
    }

    /**
     * Changes the color of the current graph.
     */
    private void changeColor() {
        Color newColor = JColorChooser.showDialog(this, "Select a color", colorButton.getBackground());
        if (newColor != null) {
            colorButton.setBackground(newColor);
        }
    }

    /**
     * Layout created with NetBeans
     */
    private void setLayout() {
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(titleSeparator, GroupLayout.Alignment.TRAILING)
                        .addComponent(functionSeparator)
                        .addComponent(propertySeparator)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(graphList, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(applyButton)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(deleteButton))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(selectGraphTitle)
                                                                .addGap(0, 0, Short.MAX_VALUE))))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(100, 100, 100)
                                                                .addComponent(graphPropertiesTitle))
                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                .addGroup(layout.createSequentialGroup()
                                                                        .addGap(10, 10, 10)
                                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                                .addComponent(xFormulaLabel)
                                                                                .addComponent(xFormulaField, GroupLayout.PREFERRED_SIZE, 253, GroupLayout.PREFERRED_SIZE)
                                                                                .addComponent(yFormulaLabel)))
                                                                .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                        .addContainerGap()
                                                                        .addComponent(yFormulaField, GroupLayout.PREFERRED_SIZE, 253, GroupLayout.PREFERRED_SIZE)))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addComponent(singularityFilter)
                                                                        .addComponent(extendToInfinity)))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(101, 101, 101)
                                                                .addComponent(graphFunctionsTitle)))
                                                .addGap(0, 27, Short.MAX_VALUE)))
                                .addContainerGap())
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(colorLabel)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(colorButton, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                .addComponent(translateLabel)
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(translateYLabel)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(translateYField))
                                                .addGroup(layout.createSequentialGroup()
                                                        .addComponent(translateXLabel)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(translateXField, GroupLayout.PREFERRED_SIZE, 243, GroupLayout.PREFERRED_SIZE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(translateRadioButton)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(multiplyRadioButton)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(noneRadioButton)
                                        )
                                        .addComponent(derivativeCheckBox))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(selectGraphTitle)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(graphList, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(titleSeparator, GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(graphPropertiesTitle)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(xFormulaLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(xFormulaField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(yFormulaLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(yFormulaField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(colorLabel)
                                        .addComponent(colorButton, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(singularityFilter)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(extendToInfinity)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(propertySeparator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(graphFunctionsTitle)
                                .addGap(18, 18, 18)
                                .addComponent(translateLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(translateRadioButton)
                                        .addComponent(multiplyRadioButton)
                                        .addComponent(noneRadioButton))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(translateXLabel)
                                        .addComponent(translateXField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(translateYLabel)
                                        .addComponent(translateYField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(derivativeCheckBox)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 130, Short.MAX_VALUE)
                                .addComponent(functionSeparator, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(applyButton)
                                        .addComponent(deleteButton))
                                .addContainerGap())
        );
    }

}
