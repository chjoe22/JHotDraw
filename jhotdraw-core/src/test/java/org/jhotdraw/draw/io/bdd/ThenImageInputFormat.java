package org.jhotdraw.draw.io.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.draw.figure.ImageHolderFigure;

import static org.junit.Assert.assertFalse;

public class ThenImageInputFormat extends Stage<ThenImageInputFormat> {
    @ProvidedScenarioState
    Drawing drawing;

    public ThenImageInputFormat the_drawing_should_contain_the_imported_image() {
        boolean containsImage = false;
        for (Figure f : drawing.getChildren()) {
            if (f instanceof ImageHolderFigure) {
                containsImage = true;
                break;
            }
        }
        assertFalse("The drawing should contain the imported image", !containsImage);
        return self();
    }
}