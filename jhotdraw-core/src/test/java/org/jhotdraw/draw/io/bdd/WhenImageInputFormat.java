package org.jhotdraw.draw.io.bdd;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.Drawing;
import org.jhotdraw.draw.io.ImageInputFormat;

import java.io.File;

public class WhenImageInputFormat extends Stage<WhenImageInputFormat> {
    @ProvidedScenarioState
    Drawing drawing;

    @ProvidedScenarioState
    ImageInputFormat imageInputFormat;

    public WhenImageInputFormat the_image_is_read_from_file(File file) throws Exception {
        imageInputFormat.read(file.toURI(), drawing);
        return this;
    }
}