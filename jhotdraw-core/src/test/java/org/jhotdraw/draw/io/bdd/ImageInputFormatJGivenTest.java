package org.jhotdraw.draw.io.bdd;

import org.junit.Test;
import com.tngtech.jgiven.junit.ScenarioTest;
import java.io.File;


public class ImageInputFormatJGivenTest extends ScenarioTest<GivenImageInputFormat, WhenImageInputFormat, ThenImageInputFormat> {

    @Test
    public void testReadFromFile() throws Exception {
        File file = new File("path/to/image.png");
        given().an_image_input_format()
                .a_mocked_image_holder_figure()
                .a_file_with_image(file);
        when().the_image_is_read_from_file(file);
        then().the_drawing_should_contain_the_imported_image();
    }

    @Test
    public void testIsDataFlavorSupported() {
        given().an_image_input_format();
        then().the_data_flavor_should_be_supported();
    }
}