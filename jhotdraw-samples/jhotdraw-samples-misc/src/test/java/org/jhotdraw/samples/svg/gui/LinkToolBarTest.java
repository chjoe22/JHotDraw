package org.jhotdraw.samples.svg.gui;

import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.event.FigureAttributeEditorHandler;
import org.jhotdraw.draw.event.SelectionComponentDisplayer;
import org.jhotdraw.draw.gui.JAttributeTextArea;
import org.jhotdraw.draw.gui.JAttributeTextField;
import org.jhotdraw.samples.svg.SVGAttributeKeys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LinkToolBarTest {

    private LinkToolBar linkToolBar;

    @Mock
    private DrawingEditor mockEditor;

    @Mock
    private SelectionComponentDisplayer mockDisplayer;

    @Mock
    private FigureAttributeEditorHandler<String> mockHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        linkToolBar = new LinkToolBar();
    }

    @Test
    void testCreateDisclosedComponent_State1() {
        linkToolBar.setEditor(mockEditor);

        // Create the disclosed component for state 1
        JComponent component = linkToolBar.createDisclosedComponent(1);

        assertNotNull(component, "The disclosed component should not be null.");
        assertTrue(component instanceof JPanel, "The disclosed component should be a JPanel.");

        JPanel panel = (JPanel) component;

        // Verify components added to the panel
        Component[] components = panel.getComponents();
        assertTrue(components.length > 0, "Panel should contain UI components.");
    }

    @Test
    void testCreateDisclosedComponent_State2() {
        linkToolBar.setEditor(mockEditor);

        // Create the disclosed component for state 2
        JComponent component = linkToolBar.createDisclosedComponent(2);

        assertNotNull(component, "The disclosed component should not be null.");
        assertTrue(component instanceof JPanel, "The disclosed component should be a JPanel.");

        JPanel panel = (JPanel) component;

        // Verify components added to the panel
        Component[] components = panel.getComponents();
        assertTrue(components.length > 0, "Panel should contain UI components.");
    }


    @Test
    void testCreateLabel() {
        ResourceBundle labels = ResourceBundle.getBundle("org.jhotdraw.samples.svg.Labels");
        JLabel label = linkToolBar.createLabel("attribute.figureLink");

        assertNotNull(label, "Label should not be null.");
        assertEquals(labels.getString("attribute.figureLink.text"), label.getText(), "Label text should match resource bundle.");
        assertEquals(labels.getString("attribute.figureLink.toolTipText"), label.getToolTipText(), "Label tooltip should match resource bundle.");
    }

    @Test
    void testAddLinkComponents() {
        JPanel panel = new JPanel();
        linkToolBar.addLinkComponents(panel, 10, 2);

        assertNotNull(panel.getComponents(), "Panel components should not be null.");
        assertTrue(panel.getComponents().length > 0, "Panel should have components added.");

        boolean containsTextArea = false;
        for (Component component : panel.getComponents()) {
            if (component instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) component;
                if (scrollPane.getViewport().getView() instanceof JAttributeTextArea) {
                    containsTextArea = true;
                    break;
                }
            }
        }
        assertTrue(containsTextArea, "Panel should contain a scroll pane with a text area.");
    }
}
