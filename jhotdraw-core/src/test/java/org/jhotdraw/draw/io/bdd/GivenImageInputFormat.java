package org.jhotdraw.draw.io.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.figure.ImageHolderFigure;
import org.jhotdraw.draw.io.ImageInputFormat;
import org.jhotdraw.draw.DefaultDrawing;
import org.mockito.Mockito;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

import static org.mockito.Mockito.*;

public class GivenImageInputFormat extends Stage<GivenImageInputFormat> {
    @ProvidedScenarioState
    Drawing drawing;

    @ProvidedScenarioState
    ImageInputFormat imageInputFormat;

    @ProvidedScenarioState
    ImageHolderFigure prototype;

    public GivenImageInputFormat an_image_input_format() {
        prototype = mock(ImageHolderFigure.class);
        imageInputFormat = new ImageInputFormat(prototype);
        drawing = new DefaultDrawing();
        return this;
    }

    public GivenImageInputFormat a_mocked_image_holder_figure() {
        Mockito.when(prototype.clone()).thenReturn(prototype);
        return this;
    }

    public GivenImageInputFormat a_file_with_image(File file) throws Exception {
        doNothing().when(prototype).loadImage(file);
        Mockito.when(prototype.getBufferedImage()).thenReturn(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB));
        Mockito.when(prototype.getBounds()).thenReturn(new Rectangle2D.Double(0, 0, 1, 1));
        return this;
    }
}