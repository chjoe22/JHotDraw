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
            displayer = null;
        }
        super.setEditor(newValue);
        if (newValue != null) {
            displayer = new SelectionComponentDisplayer(editor, this);
        }
    }

    @Override
    protected JComponent createDisclosedComponent(int state) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new GridBagLayout());
        panel.setBorder(new EmptyBorder(5, 5, 5, 8));

        if (editor == null) {
            return panel;
        }

        switch (state) {
            case 1:
                addLinkComponents(panel, 8, 2);
                addTargetComponents(panel, 4);
                break;
            case 2:
                addLinkComponents(panel, 12, 2);
                addTargetComponents(panel, 7);
                break;
        }

        return panel;
    }

    private void addLinkComponents(JPanel panel, int columns, int rows) {
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel linkLabel = createLabel("attribute.figureLink");
        gbc.gridx = 0;
        gbc.insets = new Insets(-2, 0, -2, 0);
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(linkLabel, gbc);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.putClientProperty("JComponent.sizeVariant", "small");
        scrollPane.setBorder(PaletteLookAndFeel.getInstance().getBorder("ScrollPane.border"));

        JAttributeTextArea<String> linkField = new JAttributeTextArea<>();
        linkField.setToolTipText(labels.getString("attribute.figureLink.toolTipText"));
        linkField.setColumns(columns);
        linkField.setLineWrap(true);
        linkField.setRows(rows);
        linkField.setWrapStyleWord(true);
        linkField.setFont(PaletteLookAndFeel.getInstance().getFont("SmallSystemFont"));
        linkField.setFormatterFactory(new DefaultFormatterFactory(new DefaultFormatter()));

        disposables.add(new FigureAttributeEditorHandler<>(LINK, linkField, editor, false));
        scrollPane.setViewportView(linkField);

        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(3, 0, 0, 0);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1d;
        gbc.weighty = 1d;
        panel.add(scrollPane, gbc);
    }

    private void addTargetComponents(JPanel panel, int columns) {
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel targetLabel = createLabel("attribute.figureLinkTarget");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.insets = new Insets(3, 0, 0, 0);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        panel.add(targetLabel, gbc);

        JAttributeTextField<String> targetField = new JAttributeTextField<>();
        targetField.setToolTipText(labels.getString("attribute.figureLinkTarget.toolTipText"));
        targetField.setColumns(columns);
        targetField.setFormatterFactory(new DefaultFormatterFactory(new DefaultFormatter()));

        ComponentUI targetUI = PaletteFormattedTextFieldUI.createUI(targetField);
        if (targetUI instanceof TextUI) {
            targetField.setUI((TextUI) targetUI);
        } else {
            throw new IllegalStateException("PaletteFormattedTextFieldUI.createUI did not return a TextUI instance");
        }

        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.insets = new Insets(3, 3, 0, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        panel.add(targetField, gbc);
    }

    private JLabel createLabel(String key) {
        JLabel label = new JLabel();
        label.setUI((LabelUI) PaletteLabelUI.createUI(label));
        label.setToolTipText(labels.getString(key + ".toolTipText"));
        label.setText(labels.getString(key + ".text"));
        label.setFont(PaletteLookAndFeel.getInstance().getFont("SmallSystemFont"));
        return label;
    }

    @Override
    protected String getID() {
        return "link";
    }
}

