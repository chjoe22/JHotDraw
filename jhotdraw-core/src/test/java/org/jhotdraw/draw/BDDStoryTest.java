package org.jhotdraw.draw;

import org.testng.annotations.Test;
import com.tngtech.jgiven.junit.ScenarioTest;

public class BDDStoryTest extends ScenarioTest<GivenSomeState, WhenSomeAction, ThenSomeOutcome> {

    @Test
    public void arrangement_of_figures() {
        given().a_drawing_with_a_figure();
        when().the_figure_is_moved_to_the_back();
        then().the_figure_is_in_the_back_layer();
    }
}
