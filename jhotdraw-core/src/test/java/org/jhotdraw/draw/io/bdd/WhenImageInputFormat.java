package org.jhotdraw.draw.io.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.io.ImageInputFormat;

import java.io.File;
import java.io.IOException;

public class WhenImageInputFormat extends Stage<WhenImageInputFormat> {
    @ProvidedScenarioState
    Drawing drawing;

    @ProvidedScenarioState
    ImageInputFormat imageInputFormat;

    public WhenImageInputFormat the_png_is_imported(File file) throws IOException {
        imageInputFormat.read(file.toURI(), drawing);
        return self();
    }
}