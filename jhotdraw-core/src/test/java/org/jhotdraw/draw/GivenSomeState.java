package org.jhotdraw.draw;

import com.tngtech.jgiven.Stage;
import com.tngtech.jgiven.annotation.ProvidedScenarioState;
import org.jhotdraw.draw.figure.EllipseFigure;
import org.jhotdraw.draw.figure.Figure;

public class GivenSomeState extends Stage<GivenSomeState> {

    @ProvidedScenarioState
    private DefaultDrawingView drawingView;
    @ProvidedScenarioState
    private Figure figure;

    public GivenSomeState a_drawing_with_a_figure() {
        drawingView = new DefaultDrawingView();
        figure = new EllipseFigure();
        drawingView.getDrawing().add(figure);
        return this;
    }
}