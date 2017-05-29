package utils;

import java.awt.*;

/**
 * Represents a pixel of a gif image. In contrast to the actual pixel of a gif image, an object of this class contains
 * the color of a pixel and the position (x,y) of a pixels.
 * <p>
 * Created by Florian Gibala on 29.06.16.
 */
public class Pixel {

    private int unsignedPixelValue;
    private Color color;
    private int x;
    private int y;
    private Pixel p1;
    private Pixel p2;
    private Pixel p3;
    private Pixel p4;

    /**
     * Constructor setting the color of the pixel and its position.
     *
     * @param unsignedPixelValue the unsigned pixel value
     * @param color              the {@link Color} of a pixel
     * @param x                  the x-coordinate of a pixel
     * @param y                  the y-coordinate of a pixel
     */
    public Pixel(int unsignedPixelValue, Color color, int x, int y) {

        this.unsignedPixelValue = unsignedPixelValue;
        this.color = color;
        this.x = x;
        this.y = y;

    }

    public int getUnsignedPixelValue() {
        return unsignedPixelValue;
    }

    /**
     * Returns the x-coordinate of a pixel
     *
     * @return the x-coordinate of a pixel
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of a pixel
     *
     * @return the y-coordinate of a pixel
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the color of a pixel
     *
     * @return the color of a pixel
     */
    public Color getColor() {
        return color;
    }

    public Pixel getP1() {
        return p1;
    }

    public Pixel getP2() {
        return p2;
    }

    public Pixel getP3() {
        return p3;
    }

    public Pixel getP4() {
        return p4;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setUnsignedPixelValue(int unsignedPixelValue) {
        this.unsignedPixelValue = unsignedPixelValue;
    }

    public void setP1(Pixel p1) {
        this.p1 = p1;
    }

    public boolean hasNeighbors(){
        return this.p1 != null && this.p2 != null && this.p3 != null && this.p4 != null;
    }

    public void setP2(Pixel p2) {
        this.p2 = p2;
    }

    public void setP3(Pixel p3) {
        this.p3 = p3;
    }

    public void setP4(Pixel p4) {
        this.p4 = p4;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ") -> " + "byte: "+unsignedPixelValue+ " color: "+ this.color.toString();
    }

    /**
     * Compares two objects of type {@link Pixel} for equality. They are only equal if there coordinates are equal
     * and there colors are equal. The last comparison is done by the comparison of two objects of type {@link Color}.
     *
     * @param pixel the pixel object to be compared
     * @return true, if and only if the compared objects are equal. False if they are not equal
     */
    public boolean equals(Pixel pixel) {
        return pixel.getColor().equals(this.color) && this.x == pixel.getX() && this.y == pixel.getY();
    }
}
