package org.icepdf.core.pobjects.graphics;

import org.icepdf.core.pobjects.DictionaryEntries;
import org.icepdf.core.pobjects.Stream;
import org.icepdf.core.util.Library;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Free-form Gouraud-shaded Triangle Meshes support.
 * <p>
 * Note: currently only parsing data and returning the first colour of the first vertex.
 *
 * @since 6.2
 */
public class ShadingType4Pattern extends ShadingMeshPattern {

    private static final Logger logger =
            Logger.getLogger(ShadingType4Pattern.class.toString());

    private ArrayList<Color> colorComponents = new ArrayList<>();

    public ShadingType4Pattern(Library l, DictionaryEntries h, Stream meshDataStream) {
        super(l, h, meshDataStream);
    }

    public synchronized void init(GraphicsState graphicsState) {

        ArrayList<Integer> vertexEdgeFlag = new ArrayList<>();
        ArrayList<Point2D.Float> coordinates = new ArrayList<>();
        colorComponents = new ArrayList<>();
        try {
            while (vertexBitStream.available() > 0) {
                vertexEdgeFlag.add(readFlag());
                coordinates.add(readCoord());
                colorComponents.add(readColor());
            }
        } catch (IOException e) {
            logger.warning("Error parsing Shading type 4 pattern vertices.");
        }
    }

    public Paint getPaint() {
        return colorComponents.get(0);
    }

}
