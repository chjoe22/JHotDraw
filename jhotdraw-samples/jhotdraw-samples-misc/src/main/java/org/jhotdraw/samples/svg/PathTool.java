package org.jhotdraw.samples.svg;

import org.jhotdraw.draw.AttributeKey;
import org.jhotdraw.draw.DrawingView;
import org.jhotdraw.draw.figure.BezierFigure;
import org.jhotdraw.draw.tool.BezierTool;
import org.jhotdraw.samples.svg.figures.SVGBezierFigure;
import org.jhotdraw.samples.svg.figures.SVGPathFigure;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PathTool extends BezierTool {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(PathTool.class.getName());

    private SVGPathFigure pathPrototype;

    public PathTool(SVGPathFigure pathPrototype, SVGBezierFigure bezierPrototype, Map<AttributeKey<?>, Object> attributes) {
        super(bezierPrototype, attributes);
        this.pathPrototype = pathPrototype;
    }

    protected SVGPathFigure createPath() {
        SVGPathFigure f = pathPrototype.clone();
        getEditor().applyDefaultAttributesTo(f);
        applyAttributes(f);
        return f;
    }

    private void applyAttributes(SVGPathFigure figure) {
        if (attributes != null) {
            for (Map.Entry<AttributeKey<?>, Object> entry : attributes.entrySet()) {
                figure.set((AttributeKey<Object>) entry.getKey(), entry.getValue());
            }
        }
    }

    @Override
    protected void finishCreation(BezierFigure createdFigure, DrawingView creationView) {
        logDebug("PathTool.finishCreation " + createdFigure);

        creationView.getDrawing().remove(createdFigure);
        SVGPathFigure createdPath = createPath();
        configurePath(createdPath, createdFigure, creationView);
    }

    private void logDebug(String message) {
        if (logger.isLoggable(Level.FINE)) {
            logger.fine(message);
        }
    }

    private void configurePath(SVGPathFigure createdPath, BezierFigure createdFigure, DrawingView creationView) {
        createdPath.removeAllChildren();
        createdPath.add(createdFigure);
        creationView.getDrawing().add(createdPath);
        fireUndoEvent(createdPath, creationView);
        creationView.addToSelection(createdPath);

        if (isToolDoneAfterCreation()) {
            fireToolDone();
        }
    }
}
