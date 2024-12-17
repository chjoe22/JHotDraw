package org.jhotdraw.draw.action;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.AbstractAction;
import javax.swing.undo.UndoableEdit;
import org.jhotdraw.beans.WeakPropertyChangeListener;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.DrawingEditor;
import org.jhotdraw.draw.DrawingView;

/**
 * This abstract class can be extended to implement an {@code Action} that acts
 * on behalf of a {@link org.jhotdraw.draw.DrawingEditor}.
 * <p>
 * AbstractDrawingEditorAction listens for enabled state changes using a
 * {@link WeakPropertyChangeListener} on the {@code DrawingEditor}.
 * </p>
 */
public abstract class AbstractDrawingEditorAction extends AbstractAction {

    private static final long serialVersionUID = 1L;
    protected DrawingEditor editor;
    private final PropertyChangeListener eventHandler;

    /**
     * Creates a new instance and sets up the editor and event handler.
     */
    public AbstractDrawingEditorAction(DrawingEditor editor) {
        this.eventHandler = this::handlePropertyChange;
        setEditor(editor);
    }

    public void setEditor(DrawingEditor newEditor) {
        unregisterEventHandler();
        this.editor = newEditor;
        registerEventHandler();
        updateEnabledState();
    }

    protected void updateEnabledState() {
        setEnabled(editor != null && editor.isEnabled());
    }

    public DrawingEditor getEditor() {
        return editor;
    }

    protected DrawingView getView() {
        return editor != null ? editor.getActiveView() : null;
    }

    protected Drawing getDrawing() {
        DrawingView view = getView();
        return view != null ? view.getDrawing() : null;
    }

    protected void fireUndoableEditHappened(UndoableEdit edit) {
        Drawing drawing = getDrawing();
        if (drawing != null) {
            drawing.fireUndoableEditHappened(edit);
        }
    }

    public void setUpdateEnabledState(boolean shouldUpdate) {
        if (shouldUpdate) {
            registerEventHandler();
        } else {
            unregisterEventHandler();
        }
        updateEnabledState();
    }

    public boolean isUpdateEnabledState() {
        return editor != null && editor.isEnabled();
    }

    private void unregisterEventHandler() {
        if (editor != null) {
            editor.removePropertyChangeListener(eventHandler);
        }
    }

    private void registerEventHandler() {
        if (editor != null) {
            editor.addPropertyChangeListener(new WeakPropertyChangeListener(eventHandler));
        }
    }

    private void handlePropertyChange(PropertyChangeEvent evt) {
        if ("enabled".equals(evt.getPropertyName())) {
            updateEnabledState();
        }
    }
}
