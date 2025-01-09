package org.jhotdraw.draw.io;

import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.ImageHolderFigure;
import org.jhotdraw.draw.DefaultDrawing;
import org.junit.Before;
import org.junit.Test;

import java.awt.datatransfer.DataFlavor;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.awt.geom.Rectangle2D;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ImageInputFormatTest {

    private ImageInputFormat imageInputFormat;
    private ImageHolderFigure prototype;
    private Drawing drawing;

    @Before
    public void setUp() {
        prototype = mock(ImageHolderFigure.class);
        imageInputFormat = new ImageInputFormat(prototype);
        drawing = new DefaultDrawing();
    }

    @Test
    public void testReadFromFile() throws Exception {
        File file = new File("path/to/image.png");
        when(prototype.clone()).thenReturn(prototype);
        doNothing().when(prototype).loadImage(file);
        when(prototype.getBufferedImage()).thenReturn(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
        when(prototype.getBounds()).thenReturn(new Rectangle2D.Double(0, 0, 1, 1));

        imageInputFormat.read(file.toURI(), drawing);

        List<Figure> figures = drawing.getChildren();
        assertFalse(figures.isEmpty());
        assertTrue(figures.get(0) instanceof ImageHolderFigure);
    }

    @Test
    public void testIsDataFlavorSupported() {
        assertTrue(imageInputFormat.isDataFlavorSupported(DataFlavor.imageFlavor));
    }
}