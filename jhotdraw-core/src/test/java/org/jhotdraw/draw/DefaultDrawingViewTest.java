package org.jhotdraw.draw;

import com.sun.tools.javac.util.List;
import org.jhotdraw.draw.figure.*;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

import static org.testng.Assert.*;

public class DefaultDrawingViewTest {

    private DefaultDrawingView drawingView = new DefaultDrawingView();
    private Figure figure1 = new EllipseFigure();
    private Figure figure2 = new RectangleFigure();
    private Figure figure3 = new RoundRectangleFigure();


    @BeforeEach
    public void setUp() {
        drawingView.getDrawing().add(figure1);
        drawingView.getDrawing().add(figure2);
        drawingView.getDrawing().add(figure3);

    }

    @Test
    public void testSendToBack() {
        //Send figure1 to back
        drawingView.sendToBack(drawingView, figure2);

        //Verify the order of figures and if it is sent to back
        ArrayList<Figure> figures = new ArrayList<>(drawingView.getSelectedFigures());
        assertEquals(figure2, figures.get(0));
        assertEquals(figure1, figures.get(1));
        assertEquals(figure3, figures.get(2));
    }


    //Boundary test
    @Test
    public void testSendToBackWithEmptyList(){
        DefaultDrawingView emptyDrawingView = new DefaultDrawingView();
        Figure figure = new RectangleFigure();
        emptyDrawingView.getDrawing().add(figure);

        //Send the single figure to back
        emptyDrawingView.sendToBack(emptyDrawingView, figure);

        // Verify the figure is still in the drawing and in the correct position
        ArrayList<Figure> figures = new ArrayList<>(emptyDrawingView.getSelectedFigures());
        assertEquals(1, figures.size());
        assertEquals(figure, figures.get(0));

    }
}