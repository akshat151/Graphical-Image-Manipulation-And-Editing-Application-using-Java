package imagemodel;

/**
 * This interface represents the pixel in the image. It contains the methods to get the value,
 * intensity, and luma of the pixel.
 */
public interface PixelInterface {
  /**
   * Get the maximum value of the red, green, and blue values.
   * @return the value component of the pixel
   */
  int getValue();

  /**
   * Get the average value of the red, green, and blue values.
   * @return the intensity component of the pixel
   */
  int getIntensity();

  /**
   * Get the luma value of the pixel.
   * @return the luma component of the pixel
   */
  int getLuma();

  /**
   * Get the string representation of the pixel.
   * @return the string representation of the pixel
   */
  String toString();

  /**
   * Get the red value of the pixel.
   * @return the red value of the pixel
   */
  int getRed();

  /**
   * Get the green value of the pixel.
   * @return the green value of the pixel
   */
  int getGreen();

  /**
   * Get the blue value of the pixel.
   * @return the blue value of the pixel
   */
  int getBlue();

  /**
   * Add the given pixel to this pixel.
   * @param other the pixel to be added to this pixel
   * @return the new pixel
   */
  PixelInterface add(PixelInterface other);

  /**
   * Add the given value to this pixel.
   * @param value the value to be added to this pixel
   * @return the new pixel
   */
  PixelInterface add(double value);
}
