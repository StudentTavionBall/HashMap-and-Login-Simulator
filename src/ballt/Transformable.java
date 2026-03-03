/*
 * Course: CSC-1120
 * Lab Assignment 3 - Image Manipulator 3000
 * ImageDisplayer
 * Name: Tavion Ball
 * Last Updated: 02/04/2026
 */
package ballt;

import javafx.scene.paint.Color;

/**
 * Interface for function programming for
 * applying filters to images
 */
@FunctionalInterface
public interface Transformable {
    /**
     * Applies a filter to a Color object at y
     * @param y in image
     * @param color of pixel
     * @return color with filter
     */
    Color apply(int y, Color color);
}
