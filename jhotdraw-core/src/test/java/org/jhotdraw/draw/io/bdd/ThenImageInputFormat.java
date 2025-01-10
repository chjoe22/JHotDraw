package org.jhotdraw.draw.io.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.ImageHolderFigure;
import org.jhotdraw.draw.io.ImageInputFormat;

import static org.junit.Assert.assertTrue;

import java.awt.datatransfer.DataFlavor;

public class ThenImageInputFormat extends Stage<ThenImageInputFormat> {
    @ProvidedScenarioState
    Drawing drawing;

    @ProvidedScenarioState
    ImageInputFormat imageInputFormat;

    public ThenImageInputFormat the_drawing_should_contain_the_imported_image() {
        boolean containsImage = false;
        for (Figure f : drawing.getChildren()) {
            if (f instanceof ImageHolderFigure) {
                containsImage = true;
                break;
            }
        }
        assertTrue("The drawing should contain the imported image", containsImage);
        return this;
    }

    public ThenImageInputFormat the_data_flavor_should_be_supported() {
        assertTrue(imageInputFormat.isDataFlavorSupported(DataFlavor.imageFlavor));
        return this;
    }
}