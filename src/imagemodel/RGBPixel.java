package imagemodel;

/**
 * This class represents a single pixel in an image. It contains the red, green, and blue values of
 * the pixel. The pixel is designed to be immutable. Any method that changes the pixel will return a
 * copy of the pixel with the changed values.
 */
public class RGBPixel implements PixelInterface {

  private final int red;
  private final int blue;
  private final int green;

  /**
   * Construct a new RGBPixel with the given red, green, and blue values.
   *
   * @param red   the red value of the pixel
   * @param green the green value of the pixel
   * @param blue  the blue value of the pixel
   */
  public RGBPixel(int red, int green, int blue) {
    this.red = red;
    this.green = green;
    this.blue = blue;
  }

  /**
   * Construct a new RGBPixel with the given value across channels.
   *
   * @param value the value of the pixel
   */
  public RGBPixel(int value) {
    this.red = value;
    this.green = value;
    this.blue = value;
  }

  /**
   * Get the maximum value of the red, green, and blue values.
   *
   * @return the value component of the pixel
   */
  @Override
  public int getValue() {
    return Math.max(red, Math.max(green, blue));
  }

  @Override
  public int getIntensity() {
    return (red + green + blue) / 3;
  }

  @Override
  public int getLuma() {
    return (int) Math.round(0.2126 * red + 0.7152 * green + 0.0722 * blue);
  }

  @Override
  public String toString() {
    return "R:" + red + " G:" + green + " B:" + blue;
  }

  @Override
  public int getRed() {
    return red;
  }

  @Override
  public int getGreen() {
    return green;
  }

  @Override
  public int getBlue() {
    return blue;
  }

  @Override
  public PixelInterface add(PixelInterface other) {
    return new RGBPixel(red + other.getRed(), green + other.getGreen(), blue + other.getBlue());
  }

  @Override
  public PixelInterface add(double value) {
    return new RGBPixel(
            (int) Math.round(red + value),
            (int) Math.round(green + value),
            (int) Math.round(blue + value)
    );
  }
}
