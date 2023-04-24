package helpers.loaders;

import java.io.FileInputStream;
import java.io.IOException;

import imagemodel.ImageInterface;

/**
 * This interface represents the ImageLoader class. It is used to load images from a file path.
 * It is implemented by the ppmLoader, pngLoader, and jpegLoader classes. Any future extension to
 * be supported by the program should implement this interface.
 */
public interface ImageLoader {

  /**
   * This method loads an image from a file path.
   *
   * @param fileInputStream the image bytes to be loaded
   * @return the image loaded from the file path
   */
  ImageInterface loadImage(FileInputStream fileInputStream) throws IOException;

  /**
   * Returns the supported extension of the image loader.
   *
   * @return supported extension of the loader
   */
  String getSupportedExtension();

  /**
   * This method checks if the image is valid.
   *
   * @param filePath the file path of the image to be checked
   * @return true if the image is valid, false otherwise
   */
  boolean isValidImage(String filePath);
}
