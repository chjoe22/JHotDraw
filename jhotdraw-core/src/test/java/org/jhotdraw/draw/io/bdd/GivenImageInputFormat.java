package org.jhotdraw.draw.io.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.DefaultDrawing;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.figure.ImageFigure; // Assuming ImageFigure is a concrete class that implements ImageHolderFigure
import org.jhotdraw.draw.io.ImageInputFormat;

public class GivenImageInputFormat extends Stage<GivenImageInputFormat> {
    @ProvidedScenarioState
    Drawing drawing;

    @ProvidedScenarioState
    ImageInputFormat imageInputFormat;

    public GivenImageInputFormat an_image_input_format() {
        drawing = new DefaultDrawing();
        imageInputFormat = new ImageInputFormat(new ImageFigure()); // Use the concrete class here
        return this;
    }
}