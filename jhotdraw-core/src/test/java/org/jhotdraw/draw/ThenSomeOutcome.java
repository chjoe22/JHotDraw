package org.jhotdraw.draw;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.figure.Figure;

import java.util.ArrayList;

import static org.testng.Assert.assertEquals;

public class ThenSomeOutcome extends Stage<ThenSomeOutcome> {

    @ProvidedScenarioState
    private DefaultDrawingView drawingView;
    @ProvidedScenarioState
    private Figure figure;

    public ThenSomeOutcome the_figure_is_in_the_back_layer() {
        ArrayList<Figure> figures = new ArrayList<>(drawingView.getSelectedFigures());
        assertEquals(figure, figures.get(0));
        return this;
    }
}