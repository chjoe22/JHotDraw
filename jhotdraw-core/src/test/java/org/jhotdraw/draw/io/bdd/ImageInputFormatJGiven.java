package org.jhotdraw.draw.io.bdd;

import com.tngtech.jgiven.junit.JGivenMethodRule;
import org.junit.Rule;
import org.junit.Test;
import com.tngtech.jgiven.junit.ScenarioTest;

import java.io.File;
import java.io.IOException;

// User Story: As a User,
// I want to be able open different formats,
// so I can input my pictures in different formats

public class ImageInputFormatJGiven extends ScenarioTest<GivenImageInputFormat, WhenImageInputFormat, ThenImageInputFormat> {

    @Rule
    public JGivenMethodRule jgivenMethodRule = new JGivenMethodRule();

    @Test
    public void testImageInputFormat() throws IOException {
        given().an_image_input_format();
        when().the_png_is_imported(new File("src/test/resources/testImage.png"));
        then().the_drawing_should_contain_the_imported_image();
    }
}