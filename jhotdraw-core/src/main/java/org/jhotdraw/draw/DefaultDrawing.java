/*
 * @(#)DefaultDrawing.java
 *
 * Copyright (c) 1996-2010 The authors and contributors of JHotDraw.
 * You may not use, copy or modify this file, except in compliance with the
 * accompanying license terms.
 */
package org.jhotdraw.draw;


import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import static org.jhotdraw.draw.AttributeKeys.*;
import org.jhotdraw.draw.figure.Figure;
import org.jhotdraw.geom.Geom;
import org.jhotdraw.util.ReversedList;

/**
 * A default implementation of {@link Drawing} useful for drawings which contain
 * only a few figures.
 * <p>
 * For larger drawings, {@link QuadTreeDrawing} is
 * recommended.
 * <p>
 * FIXME - Maybe we should rename this class to SimpleDrawing
 * or we should get rid of this class altogether.
 *
 *
 * @author Werner Randelshofer
 * @version $Id$
 */
public class DefaultDrawing
        extends AbstractDrawing {

    private static final long serialVersionUID = 1L;
    private boolean needsSorting = false;

    /**
     * Creates a new instance.
     */
    public DefaultDrawing() {
    }

    @Override
    public void basicAdd(int index, Figure figure) {
        super.basicAdd(index, figure);
        invalidateSortOrder();
    }

    @Override
    public void draw(Graphics2D graphics) {
        synchronized (getLock()) {
            ensureSorted();
            List<Figure> toDraw = new ArrayList<>(getChildren().size());
            Rectangle clipRect = graphics.getClipBounds();
            double scale = AttributeKeys.getScaleFactorFromGraphics(graphics);
            for (Figure f : getChildren()) {
                if (f.getDrawingArea(scale).intersects(clipRect)) {
                    toDraw.add(f);
                }
            }
            draw(graphics, toDraw);
        }
    }

    public void draw(Graphics2D graphics, Collection<Figure> children) {
        Rectangle2D clipBounds = graphics.getClipBounds();
        double scale = AttributeKeys.getScaleFactorFromGraphics(graphics);
        if (clipBounds != null) {
            for (Figure f : children) {
                if (f.isVisible() && f.getDrawingArea(scale).intersects(clipBounds)) {
                    f.draw(graphics);
                }
            }
        } else {
            for (Figure f : children) {
                if (f.isVisible()) {
                    f.draw(graphics);
                }
            }
        }
    }

    @Override
    public List<Figure> sort(Collection<? extends Figure> collection) {
        Set<Figure> unsorted = new HashSet<>();
        unsorted.addAll(collection);
        List<Figure> sorted = new ArrayList<>(collection.size());
        for (Figure figure : getChildren()) {
            if (unsorted.contains(figure)) {
                sorted.add(figure);
                unsorted.remove(figure);
            }
        }
        for (Figure figure : collection) {
            if (unsorted.contains(figure)) {
                sorted.add(figure);
                unsorted.remove(figure);
            }
        }
        return sorted;
    }

    @Override
    public Figure findFigure(Point2D.Double point) {
        for (Figure figure : getFiguresFrontToBack()) {
            if (figure.isVisible() && figure.contains(point)) {
                return figure;
            }
        }
        return null;
    }

    @Override
    public Figure findFigureExcept(Point2D.Double point, Figure ignore) {
        for (Figure figure : getFiguresFrontToBack()) {
            if (figure != ignore && figure.isVisible() && figure.contains(point)) {
                return figure;
            }
        }
        return null;
    }

    @Override
    public Figure findFigureBehind(Point2D.Double point, Figure figure) {
        boolean isBehind = false;
        for (Figure otherFigure : getFiguresFrontToBack()) {
            if (isBehind) {
                if (otherFigure.isVisible() && otherFigure.contains(point)) {
                    return otherFigure;
                }
            } else {
                isBehind = figure == otherFigure;
            }
        }
        return null;
    }

    @Override
    public Figure findFigureBehind(Point2D.Double point, Collection<? extends Figure> children) {
        int inFrontOf = children.size();
        for (Figure figure : getFiguresFrontToBack()) {
            if (inFrontOf == 0) {
                if (figure.isVisible() && figure.contains(point)) {
                    return figure;
                }
            } else {
                if (children.contains(figure)) {
                    inFrontOf--;
                }
            }
        }
        return null;
    }

    @Override
    public Figure findFigureExcept(Point2D.Double point, Collection<? extends Figure> ignore) {
        for (Figure figure : getFiguresFrontToBack()) {
            if (!ignore.contains(figure) && figure.isVisible() && figure.contains(point)) {
                return figure;
            }
        }
        return null;
    }

    @Override
    public List<Figure> findFigures(Rectangle2D.Double bounds) {
        List<Figure> intersection = new LinkedList<>();
        for (Figure figure : getChildren()) {
            if (figure.isVisible() && figure.getBounds().intersects(bounds)) {
                intersection.add(figure);
            }
        }
        return intersection;
    }

    @Override
    public List<Figure> findFiguresWithin(Rectangle2D.Double bounds) {
        List<Figure> contained = new LinkedList<>();
        for (Figure figure : getChildren()) {
            Rectangle2D.Double rectangle = figure.getBounds();
            if (figure.get(TRANSFORM) != null) {
                Rectangle2D rectangleTransformed = figure.get(TRANSFORM).createTransformedShape(rectangle).getBounds2D();
                rectangle = (rectangleTransformed instanceof Rectangle2D.Double) ? (Rectangle2D.Double) rectangleTransformed : new Rectangle2D.Double(rectangleTransformed.getX(), rectangleTransformed.getY(), rectangleTransformed.getWidth(), rectangleTransformed.getHeight());
            }
            if (figure.isVisible() && Geom.contains(bounds, rectangle)) {
                contained.add(figure);
            }
        }
        return contained;
    }

    @Override
    public Figure findFigureInside(Point2D.Double point) {
        Figure figure = findFigure(point);
        return (figure == null) ? null : figure.findFigureInside(point);
    }

    /**
     * Returns an iterator to iterate in Z-order front to back over the
     * children.
     */
    @Override
    public List<Figure> getFiguresFrontToBack() {
        ensureSorted();
        return new ReversedList<>(getChildren());
    }

    /**
     * Invalidates the sort order.
     */
    private void invalidateSortOrder() {
        needsSorting = true;
    }

    /**
     * Ensures that the children are sorted in z-order sequence from back to
     * front.
     */
    private void ensureSorted() {
        if (needsSorting) {
            Collections.sort(children, FigureLayerComparator.INSTANCE);
            needsSorting = false;
        }
    }

    @Override
    protected <T> void setAttributeOnChildren(AttributeKey<T> key, T newValue) {
        // empty
    }

    @Override
    public int indexOf(Figure figure) {
        return children.indexOf(figure);
    }

    @Override
    protected void drawFill(Graphics2D graphics) {
    }

    @Override
    protected void drawStroke(Graphics2D graphics) {
    }

    @Override
    public void drawCanvas(Graphics2D graphics) {
        if (get(CANVAS_WIDTH) != null && get(CANVAS_HEIGHT) != null) {
            // Determine canvas color and opacity
            Color canvasColor = get(CANVAS_FILL_COLOR);
            Double fillOpacity = get(CANVAS_FILL_OPACITY);
            if (canvasColor != null && fillOpacity > 0) {
                canvasColor = new Color(
                        (canvasColor.getRGB() & 0xffffff)
                        | ((int) (fillOpacity * 255) << 24), true);
                // Fill the canvas
                Rectangle2D.Double rectangle = new Rectangle2D.Double(
                        0, 0, get(CANVAS_WIDTH), get(CANVAS_HEIGHT));
                graphics.setColor(canvasColor);
                graphics.fill(rectangle);
            }
        }
    }
}
