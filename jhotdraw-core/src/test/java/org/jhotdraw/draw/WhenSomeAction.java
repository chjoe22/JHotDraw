package org.jhotdraw.draw;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.figure.Figure;

public class WhenSomeAction extends Stage<WhenSomeAction> {

    @ProvidedScenarioState
    private DefaultDrawingView drawingView;
    @ProvidedScenarioState
    private Figure figure;

    public WhenSomeAction the_figure_is_moved_to_the_back() {
        drawingView.sendToBack(drawingView, figure);
        return this;
    }
}