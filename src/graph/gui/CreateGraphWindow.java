package graph.gui;

import graph.function.Formula;
import graph.function.GraphFormula;
import graph.function.ParametricEquation;
import graph.parser.ExpressionParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * This class represents the GUI that is used to create graphs.
 */
class CreateGraphWindow extends JDialog implements ActionListener {

    /**
     * The graph panel instance
     */
    private final GraphPanel graphPanel;

    /**
     * The graph frame instance
     */
    private final InputPanel inputPanel;

    /**
     * The GUI components
     */
    private JComboBox<String> formulaType;
    private JLabel firstFormulaLabel, secondFormulaLabel, titleLabel, formulaTypeLabel, colorLabel;
    private JTextField firstFormulaField, secondFormulaField;
    private JSeparator titleSeparator;
    private JButton createButton, colorButton;

    /**
     * Creates a Create Graph Window.
     *
     * @param parent the parent frame
     */
    CreateGraphWindow(GraphFrame parent) {
        super(parent, "Create a graph");
        inputPanel = parent.getInputPanel();
        graphPanel = parent.getGraphPanel();

        titleLabel = new JLabel("Create a Graph");
        formulaTypeLabel = new JLabel("Formula Type:");
        titleSeparator = new JSeparator();

        formulaType = new JComboBox<>(new String[]{"Formula", "Parametric Equation"});
        formulaType.addActionListener(this);
        formulaType.setActionCommand("formula_type_changed");

        firstFormulaField = new JTextField();
        secondFormulaField = new JTextField();
        firstFormulaLabel = new JLabel("f(x) = ");
        secondFormulaLabel = new JLabel("y(t)");
        secondFormulaField.setVisible(false);
        secondFormulaLabel.setVisible(false);

        colorLabel = new JLabel("Color:");
        colorButton = new JButton();
        colorButton.setBackground(Color.BLACK);
        colorButton.addActionListener(this);
        colorButton.setActionCommand("color");

        createButton = new JButton("Create");
        createButton.addActionListener(this);
        createButton.setActionCommand("create");

        setLayout();
        setSize(380, 265);
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "formula_type_changed":
                switch ((String) formulaType.getSelectedItem()) {
                    case "Formula":
                        updateFormulaFields();
                        break;
                    case "Parametric Equation":
                        updateParametricEquationFields();
                        break;
                }
                break;
            case "color":
                changeColor();
                break;
            case "create":
                switch ((String) formulaType.getSelectedItem()) {
                    case "Formula":
                        addGraphFormula();
                        break;
                    case "Parametric Equation":
                        addParametricEquation();
                        break;
                }
                break;
        }
    }

    /**
     * Changes the fields to be used for parametric equations.
     */
    private void updateParametricEquationFields() {
        firstFormulaLabel.setText("x(t) = ");
        secondFormulaLabel.setText("y(t) =");
        secondFormulaLabel.setVisible(true);
        secondFormulaField.setVisible(true);
    }

    /**
     * Changes the fields to be used for normal formulas.
     */
    private void updateFormulaFields() {
        firstFormulaLabel.setText("f(x) =");
        secondFormulaLabel.setVisible(false);
        secondFormulaField.setVisible(false);
    }

    /**
     * Changes the color of the new graph.
     */
    private void changeColor() {
        Color newColor = JColorChooser.showDialog(this, "Select a color", colorButton.getBackground());
        if (newColor != null) {
            colorButton.setBackground(newColor);
        }
    }

    /**
     * Adds a new parametric equation.
     */
    private void addParametricEquation() {
        String formulaX = firstFormulaField.getText();
        Formula x = ExpressionParser.createFormula(formulaX, "t");
        String formulaY = secondFormulaField.getText();
        Formula y = ExpressionParser.createFormula(formulaY, "t");
        String graphName = formulaX + "," + formulaY;
        if (x != null && y != null) {
            ParametricEquation p = new ParametricEquation(x, y, colorButton.getBackground(), graphName);
            if (!graphPanel.add(p)) {
                JOptionPane.showMessageDialog(this, "Graph already exists", "Formula Input", JOptionPane.ERROR_MESSAGE);
            } else {
                postGraphAddition(graphName);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid formula!", "Formula Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Adds a new formula.
     */
    private void addGraphFormula() {
        String formula = firstFormulaField.getText();
        Formula f = ExpressionParser.createFormula(formula, "x");
        if (f != null) {
            GraphFormula graphFormula = new GraphFormula(formula, f, colorButton.getBackground());
            if (!graphPanel.add(graphFormula)) {
                JOptionPane.showMessageDialog(this, "Graph already exists", "Formula Input", JOptionPane.ERROR_MESSAGE);
            } else {
                postGraphAddition(formula);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid formula!", "Formula Input", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Performs all the needed actions before disposing. Updates the
     * {@link JComboBox} that contains all the graphs and selects the
     * new graph.
     *
     * @param graphName the new graph
     */
    private void postGraphAddition(String graphName) {
        inputPanel.updateGraphList();
        inputPanel.setSelectedGraph(graphName);
        dispose();
    }

    /**
     * Layout created with NetBeans
     */
    private void setLayout() {
        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(137, 137, 137)
                                .addComponent(titleLabel)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(titleSeparator)
                                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(formulaTypeLabel)
                                                        .addComponent(firstFormulaLabel)
                                                        .addComponent(secondFormulaLabel)
                                                        .addComponent(colorLabel))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                .addComponent(formulaType, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(firstFormulaField)
                                                                .addComponent(secondFormulaField, GroupLayout.PREFERRED_SIZE, 181, GroupLayout.PREFERRED_SIZE))
                                                        .addComponent(colorButton, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE))
                                                .addGap(53, 53, 53)))
                                .addContainerGap())
                        .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(createButton, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
                                .addGap(148, 148, 148))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(titleLabel)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(titleSeparator, GroupLayout.PREFERRED_SIZE, 2, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(formulaType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(formulaTypeLabel))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(firstFormulaLabel)
                                        .addComponent(firstFormulaField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(secondFormulaLabel)
                                        .addComponent(secondFormulaField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(colorLabel)
                                        .addComponent(colorButton, 20, 20, 20))
                                .addGap(18, 18, 18)
                                .addComponent(createButton)
                                .addContainerGap(20, Short.MAX_VALUE))
        );
    }

}
