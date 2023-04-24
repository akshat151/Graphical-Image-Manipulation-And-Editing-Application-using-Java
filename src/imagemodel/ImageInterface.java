package imagemodel;

import java.util.List;

/**
 * This interface represents an image. It contains methods to get the width and height of the image,
 * as well as methods to get the pixel at a given coordinate. This acts as the model within the
 * MVC architecture.
 * It also has methods to perform operations on the image.
 */
public interface ImageInterface {

  /**
   * Flips the image horizontally about the y-axis.
   *
   * @return a new image that is the flipped version of the original
   */
  ImageInterface flipHorizontal();

  /**
   * Flips the image vertically about the x-axis.
   *
   * @return a new image that is the flipped version of the original
   */
  ImageInterface flipVertical();


  /**
   * Brighten/darkens the image by the given value.
   *
   * @param value by which to brighten or darken this image.
   * @return a new image that is the changed version of the original.
   */
  ImageInterface brighten(int value);

  /**
   * Creates a greyscale version of the image.
   *
   * @param color component that needs to be isolated to create the greyscale
   * @return a new image that is the changed version of the original.
   */
  ImageInterface greyscale(String color);

  /**
   * Converts this image to greyscale.
   *
   * @param greyscale The color transform to use for greyscale.
   * @return a new image that is a greyscale version of the original.
   */
  ImageInterface greyscale(ColorTransform greyscale);

  /**
   * Splits an image into 3 greyscale images using its rgb components.
   *
   * @return a list of greyscale images.
   */
  List<ImageInterface> rgbSplit();

  /**
   * Return an array representation of the image pixels.
   *
   * @return a 2d array of PixelInterface that represent the given image.
   */
  List<List<PixelInterface>> getImageArray();

  /**
   * Return the height and width of the image in a list.
   *
   * @return a list of height and width.
   */
  List<Integer> getSize();

  /**
   * Performs the blur operation on the image using the given kernel.
   *
   * @param blur the kernel to use for the blur operation.
   * @return a new image that is the blurred version of the original.
   */
  ImageInterface blur(Kernel blur);

  /**
   * Performs the sharpen operation on the image using the given kernel.
   *
   * @param sharpen the kernel to use for the sharpen operation.
   * @return a new image that is the sharpened version of the original.
   */
  ImageInterface sharpen(Kernel sharpen);

  /**
   * Converts this image to sepia.
   *
   * @param sepia The color transform to use for sepia.
   * @return a new image that is a sepia version of the original.
   */
  ImageInterface sepia(ColorTransform sepia);

  /**
   * Performs the dither operation on this image and returns the new image.
   *
   * @return a new image that is the dithered version of the original.
   */
  ImageInterface dither();

  /**
   * Mosaics the image by the given seed_value.
   *
   * @param numOfSeeds by which to mosaic this image.
   * @return a new image that is the changed version of the original.
   */
  ImageInterface mosaic(int numOfSeeds);
}
