package org.jhotdraw.samples.svg.gui;

import org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI;
import org.jhotdraw.gui.plaf.palette.PaletteLabelUI;
import org.jhotdraw.gui.plaf.palette.PaletteLookAndFeel;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.LabelUI;
import javax.swing.plaf.TextUI;
import javax.swing.text.DefaultFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.awt.*;
import java.util.ResourceBundle;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.event.FigureAttributeEditorHandler;
import org.jhotdraw.draw.event.SelectionComponentDisplayer;
import org.jhotdraw.draw.gui.JAttributeTextArea;
import org.jhotdraw.draw.gui.JAttributeTextField;
import static org.jhotdraw.samples.svg.SVGAttributeKeys.LINK;

public class LinkToolBar extends AbstractToolBar {

    private static final long serialVersionUID = 1L;
    private static final Insets DEFAULT_INSETS = new Insets(3, 0, 0, 0);

    private SelectionComponentDisplayer displayer;
    private ResourceBundle labels;

    public LinkToolBar() {
        labels = ResourceBundle.getBundle("org.jhotdraw.samples.svg.Labels");
        setName(labels.getString(getID() + ".toolbar"));
        setDisclosureStateCount(3);
    }

    @Override
    public void setEditor(DrawingEditor newValue) {
        if (displayer != null) {
            displayer.dispose();
        }
        super.setEditor(newValue);
        displayer = (newValue != null) ? new SelectionComponentDisplayer(editor, this) : null;
    }

    @Override
    protected JComponent createDisclosedComponent(int state) {
        JPanel panel = createPanel();

        if (editor == null) {
            return panel;
        }

        switch (state) {
            case 1:
                configurePanel(panel, 8, 2, 4);
                break;
            case 2:
                configurePanel(panel, 12, 2, 7);
                break;
        }

        return panel;
    }

    private void configurePanel(JPanel panel, int linkColumns, int linkRows, int targetColumns) {
        addLinkComponents(panel, linkColumns, linkRows);
        addTargetComponents(panel, targetColumns);
    }

    private JPanel createPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(5, 5, 5, 8));
        return panel;
    }

    public void addLinkComponents(JPanel panel, int columns, int rows) {
        GridBagConstraints gbc = createGridBagConstraints(0, 0, GridBagConstraints.SOUTHWEST);
        JLabel linkLabel = createLabel("attribute.figureLink");
        panel.add(linkLabel, gbc);

        JAttributeTextArea<String> linkField = createTextArea(columns, rows, "attribute.figureLink.toolTipText");
        JScrollPane scrollPane = createScrollPane(linkField);

        gbc = createGridBagConstraints(0, 1, GridBagConstraints.BOTH);
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        panel.add(scrollPane, gbc);

        disposables.add(new FigureAttributeEditorHandler<>(LINK, linkField, editor, false));
    }

    private void addTargetComponents(JPanel panel, int columns) {
        GridBagConstraints gbc = createGridBagConstraints(0, 2, GridBagConstraints.FIRST_LINE_START);
        JLabel targetLabel = createLabel("attribute.figureLinkTarget");
        panel.add(targetLabel, gbc);

        JAttributeTextField<String> targetField = createTextField(columns, "attribute.figureLinkTarget.toolTipText");

        gbc = createGridBagConstraints(1, 2, GridBagConstraints.HORIZONTAL);
        panel.add(targetField, gbc);
    }

    private JScrollPane createScrollPane(JAttributeTextArea<String> textArea) {
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.putClientProperty("JComponent.sizeVariant", "small");
        scrollPane.setBorder(PaletteLookAndFeel.getInstance().getBorder("ScrollPane.border"));
        return scrollPane;
    }

    private JAttributeTextArea<String> createTextArea(int columns, int rows, String toolTipKey) {
        JAttributeTextArea<String> textArea = new JAttributeTextArea<>();
        textArea.setColumns(columns);
        textArea.setRows(rows);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setToolTipText(labels.getString(toolTipKey));
        textArea.setFont(PaletteLookAndFeel.getInstance().getFont("SmallSystemFont"));
        textArea.setFormatterFactory(new DefaultFormatterFactory(new DefaultFormatter()));
        return textArea;
    }

    private JAttributeTextField<String> createTextField(int columns, String toolTipKey) {
        JAttributeTextField<String> textField = new JAttributeTextField<>();
        textField.setColumns(columns);
        textField.setToolTipText(labels.getString(toolTipKey));
        textField.setFormatterFactory(new DefaultFormatterFactory(new DefaultFormatter()));

        ComponentUI ui = PaletteFormattedTextFieldUI.createUI(textField);
        if (ui instanceof TextUI) {
            textField.setUI((TextUI) ui);
        } else {
            throw new IllegalStateException("PaletteFormattedTextFieldUI.createUI did not return a TextUI instance");
        }
        return textField;
    }

    public JLabel createLabel(String key) {
        JLabel label = new JLabel();
        label.setUI((LabelUI) PaletteLabelUI.createUI(label));
        label.setToolTipText(labels.getString(key + ".toolTipText"));
        label.setText(labels.getString(key + ".text"));
        label.setFont(PaletteLookAndFeel.getInstance().getFont("SmallSystemFont"));
        return label;
    }

    private GridBagConstraints createGridBagConstraints(int gridx, int gridy, int anchor) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridy = gridy;
        gbc.insets = DEFAULT_INSETS;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = anchor;
        return gbc;
    }

    @Override
    protected String getID() {
        return "link";
    }
}
