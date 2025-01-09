/*
 * @(#)FillToolBar.java
 *
 * Copyright (c) 2007-2008 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.samples.svg.gui;

import org.jhotdraw.gui.action.ButtonFactory;
import org.jhotdraw.gui.plaf.palette.PaletteFormattedTextFieldUI;
import org.jhotdraw.gui.plaf.palette.PaletteButtonUI;
import org.jhotdraw.gui.plaf.palette.PaletteSliderUI;
import org.jhotdraw.gui.plaf.palette.PaletteColorChooserUI;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.plaf.SliderUI;
import javax.swing.text.DefaultFormatterFactory;
import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.action.*;
import org.jhotdraw.draw.event.FigureAttributeEditorHandler;
import org.jhotdraw.draw.event.SelectionComponentDisplayer;
import org.jhotdraw.draw.event.SelectionComponentRepainter;
import org.jhotdraw.draw.gui.JAttributeSlider;
import org.jhotdraw.draw.gui.JAttributeTextField;
import org.jhotdraw.gui.JPopupButton;
import static org.jhotdraw.samples.svg.SVGAttributeKeys.*;
import org.jhotdraw.text.ColorFormatter;
import org.jhotdraw.formatter.JavaNumberFormatter;
import org.jhotdraw.util.*;

/**
 * FillToolBar.
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class FillToolBar extends AbstractToolBar {

    private static final long serialVersionUID = 1L;
    private SelectionComponentDisplayer displayer;

    /**
     * Creates new instance.
     */
    public FillToolBar() {
        ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
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
        switch (state) {
            case 1:
                return createStateOneComponent();
            case 2:
                return createStateTwoComponent();
            default:
                return new JPanel();
        }
    }

    private JComponent createStateOneComponent() {
        if (editor == null) {
            return new JPanel(); // Null check, returning an empty JPanel
        }

        ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
        JPanel panel = createDefaultPanel();

        // Setting the Fill color to button
        AbstractButton fillColorButton = createFillColorButton(labels);
        panel.add(fillColorButton, createGridBagConstraints(0, 2, GridBagConstraints.FIRST_LINE_START));

        // Setting the opacity slider
        JPopupButton opacityPopupButton = createOpacitySliderButton(labels);
        panel.add(opacityPopupButton, createGridBagConstraints(0, 1, GridBagConstraints.FIRST_LINE_START));

        return panel;
    }

    private JComponent createStateTwoComponent() {
        if (editor == null) {
            return new JPanel(); //Null check, returning an empty JPanel
        }

        ResourceBundleUtil labels = ResourceBundleUtil.getBundle("org.jhotdraw.samples.svg.Labels");
        JPanel panel = createDefaultPanel();

        // Field and Button for Fill Color
        JPanel colorPanel = createColorPanel(labels);
        panel.add(colorPanel, createGridBagConstraints(0, 0, GridBagConstraints.FIRST_LINE_START));

        // Field and slider for Opacity
        JPanel opacityPanel = createOpacityPanel(labels);
        panel.add(opacityPanel, createGridBagConstraints(0, 1, GridBagConstraints.FIRST_LINE_START));

        return panel;
    }

    private JPanel createDefaultPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(5, 5, 5, 8));
        return panel;
    }

    private AbstractButton createFillColorButton(ResourceBundleUtil labels) {
        Map<AttributeKey<?>, Object> defaultAttributes = new HashMap<>();
        FILL_GRADIENT.put(defaultAttributes, null);

        AbstractButton button = ButtonFactory.createSelectionColorChooserButton(
                editor,
                FILL_COLOR,
                "attribute.fillColor",
                labels,
                defaultAttributes,
                new Rectangle(3, 3, 10, 10),
                PaletteColorChooserUI.class,
                disposables
        );
        button.setUI((PaletteButtonUI) PaletteButtonUI.createUI(button));
        return button;
    }

    private JPopupButton createOpacitySliderButton(ResourceBundleUtil labels) {
        JPopupButton popupButton = new JPopupButton();
        JAttributeSlider slider = new JAttributeSlider(JSlider.VERTICAL, 0, 100, 100);
        popupButton.add(slider);

        labels.configureToolBarButton(popupButton, "attribute.fillOpacity");
        popupButton.setUI((PaletteButtonUI) PaletteButtonUI.createUI(popupButton));
        popupButton.setIcon(new SelectionOpacityIcon(
                editor, FILL_OPACITY, FILL_COLOR, null,
                Images.createImage(getClass(), labels.getString("attribute.fillOpacity.largeIcon")),
                new Rectangle(5, 5, 6, 6), new Rectangle(4, 4, 7, 7)
        ));
        popupButton.setPopupAnchor(SOUTH_EAST);

        slider.setUI((SliderUI) PaletteSliderUI.createUI(slider));
        slider.setScaleFactor(100d);

        disposables.add(new SelectionComponentRepainter(editor, popupButton));
        disposables.add(new FigureAttributeEditorHandler<>(FILL_OPACITY, slider, editor));

        return popupButton;
    }

    private JPanel createColorPanel(ResourceBundleUtil labels) {
        JPanel colorPanel = new JPanel(new GridBagLayout());
        colorPanel.setOpaque(false);

        // Setting Field for Color
        JAttributeTextField<Color> colorField = new JAttributeTextField<>();
        colorField.setColumns(7);
        colorField.setToolTipText(labels.getString("attribute.fillColor.toolTipText"));
        colorField.setUI((PaletteFormattedTextFieldUI) PaletteFormattedTextFieldUI.createUI(colorField));
        colorField.setFormatterFactory(ColorFormatter.createFormatterFactory(ColorFormatter.Format.RGB_INTEGER_SHORT, false, false));
        colorField.setHorizontalAlignment(JTextField.LEFT);

        disposables.add(new FigureAttributeEditorHandler<>(FILL_COLOR, new HashMap<>(), colorField, editor, true));
        colorPanel.add(colorField, createGridBagConstraints(0, 1, GridBagConstraints.FIRST_LINE_START));

        // adding Color button to panel
        AbstractButton colorButton = createFillColorButton(labels);
        colorPanel.add(colorButton, createGridBagConstraints(1, 2, GridBagConstraints.FIRST_LINE_START));

        return colorPanel;
    }

    private JPanel createOpacityPanel(ResourceBundleUtil labels) {
        JPanel opacityPanel = new JPanel(new GridBagLayout());
        opacityPanel.setOpaque(false);

        // Setting field for Opacity
        JAttributeTextField<Double> opacityField = new JAttributeTextField<>();
        opacityField.setColumns(4);
        opacityField.setToolTipText(labels.getString("attribute.fillOpacity.toolTipText"));
        opacityField.setUI((PaletteFormattedTextFieldUI) PaletteFormattedTextFieldUI.createUI(opacityField));
        opacityField.setFormatterFactory(new DefaultFormatterFactory(
                new JavaNumberFormatter(0d, 100d, 100d, false, "%")
        ));
        opacityField.setHorizontalAlignment(JTextField.LEFT);

        disposables.add(new FigureAttributeEditorHandler<>(FILL_OPACITY, opacityField, editor));
        opacityPanel.add(opacityField, createGridBagConstraints(0, 1, GridBagConstraints.FIRST_LINE_START));

        // Setting slider for Opacity
        JPopupButton opacityButton = createOpacitySliderButton(labels);
        opacityPanel.add(opacityButton, createGridBagConstraints(1, 2, GridBagConstraints.FIRST_LINE_START));

        return opacityPanel;
    }

    private GridBagConstraints createGridBagConstraints(int gridx, int gridwidth, int anchor) {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = gridx;
        gbc.gridwidth = gridwidth;
        gbc.anchor = anchor;
        gbc.insets = new Insets(3, 0, 0, 0);
        return gbc;
    }


    @Override
    protected String getID() {
        return "fill";
    }

    @Override
    protected int getDefaultDisclosureState() {
        return 1;
    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setOpaque(false);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
