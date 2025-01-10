package org.jhotdraw.undo;

import javax.swing.undo.UndoableEdit;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Locale;

public class UndoRedoManagerTest {
    private UndoRedoManager manager;

    @Before
    public void setUpMethod() {
        Locale.setDefault(Locale.ENGLISH);
        manager = new UndoRedoManager();
    }

    @After
    public void tearDownMethod() {
        manager.discardAllEdits();
    }

    @Test
    public void testUndoRedoActions() {
        UndoableEdit edit = mock(UndoableEdit.class);
        when(edit.isSignificant()).thenReturn(true);
        manager.addEdit(edit);
        assertTrue(manager.canUndo());
        manager.undo();
        assertFalse(manager.canUndo());
        assertTrue(manager.canRedo());
        manager.redo();
        assertTrue(manager.canUndo());
        assertFalse(manager.canRedo());
    }
}