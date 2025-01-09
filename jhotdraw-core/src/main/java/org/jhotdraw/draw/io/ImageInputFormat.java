// ImageInputFormat.java
package org.jhotdraw.draw.io;

import org.jhotdraw.draw.figure.ImageHolderFigure;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.geom.*;
import java.io.*;
import java.net.URI;
import javax.imageio.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jhotdraw.draw.*;
import static org.jhotdraw.draw.AttributeKeys.*;
import org.jhotdraw.util.Images;

public class ImageInputFormat implements InputFormat {

    /**
     * The prototype for creating a figure that holds the imported image.
     */
    private ImageHolderFigure prototype;
    /**
     * Format description used for the file filter.
     */
    private String description;
    /**
     * File name extension used for the file filter.
     */
    private String[] fileExtensions;
    /**
     * Image IO image format name.
     */
    private String formatName;
    /**
     * The mime types which must be matched.
     */
    private String[] mimeTypes;

    /**
     * Creates a new image input format for all formats supported by
     * {@code javax.imageio.ImageIO}.
     */
    public ImageInputFormat(ImageHolderFigure prototype) {
        this(prototype, "Image", "Image", ImageIO.getReaderFileSuffixes(), ImageIO.getReaderMIMETypes());
    }

    /**
     * Creates a new image input format for the specified image format.
     *
     * @param formatName    The format name for the javax.imageio.ImageIO object.
     * @param description   The format description to be used for the file filter.
     * @param fileExtension The file extension to be used for the file filter.
     * @param mimeType      The mime type used for filtering data flavors from
     *                      Transferable objects.
     */
    public ImageInputFormat(ImageHolderFigure prototype, String formatName, String description, String fileExtension, String mimeType) {
        this(prototype, formatName, description, new String[]{fileExtension}, new String[]{mimeType});
    }

    /**
     * Creates a new image input format for the specified image format.
     *
     * @param formatName     The format name for the javax.imageio.ImageIO object.
     * @param description    The format description to be used for the file filter.
     * @param fileExtensions The file extensions to be used for the file filter.
     * @param mimeTypes      The mime typse used for filtering data flavors from
     *                       Transferable objects.
     */
    public ImageInputFormat(ImageHolderFigure prototype, String formatName, String description, String[] fileExtensions, String[] mimeTypes) {
        this.prototype = prototype;
        this.formatName = formatName;
        this.description = description;
        this.fileExtensions = fileExtensions.clone();
        this.mimeTypes = mimeTypes.clone();
    }

    @Override
    public javax.swing.filechooser.FileFilter getFileFilter() {
        return new FileNameExtensionFilter(description, fileExtensions);
    }

    public String[] getFileExtensions() {
        return fileExtensions.clone();
    }

    @Override
    public JComponent getInputFormatAccessory() {
        return null;
    }

    @Override
    public void read(URI uri, Drawing drawing) throws IOException {
        read(new File(uri), drawing);
    }

    @Override
    public void read(URI uri, Drawing drawing, boolean replace) throws IOException {
        read(new File(uri), drawing, replace);
    }

    public void read(File file, Drawing drawing, boolean replace) throws IOException {
        ImageHolderFigure figure = createImageHolder(file);
        updateDrawing(drawing, figure, replace);
    }

    public void read(File file, Drawing drawing) throws IOException {
        read(file, drawing, true);
    }

    @Override
    public void read(InputStream in, Drawing drawing, boolean replace) throws IOException {
        ImageHolderFigure figure = createImageHolder(in);
        updateDrawing(drawing, figure, replace);
    }

    private ImageHolderFigure createImageHolder(File file) throws IOException {
        ImageHolderFigure figure = (ImageHolderFigure) prototype.clone();
        figure.loadImage(file);
        setFigureBounds(figure);
        return figure;
    }

    private ImageHolderFigure createImageHolder(InputStream in) throws IOException {
        ImageHolderFigure figure = (ImageHolderFigure) prototype.clone();
        figure.loadImage(in);
        setFigureBounds(figure);
        return figure;
    }

    private void setFigureBounds(ImageHolderFigure figure) {
        figure.setBounds(
                new Point2D.Double(0, 0),
                new Point2D.Double(
                        figure.getBufferedImage().getWidth(),
                        figure.getBufferedImage().getHeight()));
    }

    private void updateDrawing(Drawing drawing, ImageHolderFigure figure, boolean replace) {
        if (replace) {
            drawing.removeAllChildren();
            drawing.set(CANVAS_WIDTH, figure.getBounds().width);
            drawing.set(CANVAS_HEIGHT, figure.getBounds().height);
        }
        drawing.basicAdd(figure);
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        if (DataFlavor.imageFlavor.match(flavor)) {
            return true;
        }
        for (String mimeType : mimeTypes) {
            if (flavor.isMimeTypeEqual(mimeType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void read(Transferable t, Drawing drawing, boolean replace) throws UnsupportedFlavorException, IOException {
        DataFlavor importFlavor = findImportFlavor(t);
        Object data = t.getTransferData(importFlavor);
        Image img = extractImage(data);
        ImageHolderFigure figure = createImageHolder(img);
        updateDrawing(drawing, figure, replace);
    }

    private DataFlavor findImportFlavor(Transferable t) throws UnsupportedFlavorException {
        for (DataFlavor flavor : t.getTransferDataFlavors()) {
            if (DataFlavor.imageFlavor.match(flavor)) {
                return flavor;
            }
            for (String mimeType : mimeTypes) {
                if (flavor.isMimeTypeEqual(mimeType)) {
                    return flavor;
                }
            }
        }
        throw new UnsupportedFlavorException(null);
    }

    private Image extractImage(Object data) throws IOException {
        if (data instanceof Image) {
            return (Image) data;
        } else if (data instanceof InputStream) {
            return ImageIO.read((InputStream) data);
        }
        throw new IOException("Unsupported data format");
    }

    private ImageHolderFigure createImageHolder(Image img) {
        ImageHolderFigure figure = (ImageHolderFigure) prototype.clone();
        figure.setBufferedImage(Images.toBufferedImage(img));
        setFigureBounds(figure);
        return figure;
    }
}