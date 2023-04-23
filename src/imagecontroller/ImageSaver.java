package imagecontroller;

import java.io.IOException;

import imagemodel.ImageInterface;
import helpers.Response;

/**
 * ImageSaver interface is used to save an image. Any image type that is supported by the program
 * must be handled by a class that implements this interface.
 */
public interface ImageSaver {

  /**
   * Save the image to the given path.
   * @param image the image to be saved
   * @param path the path to save the image to
   * @return a Response object containing the result of the operation
   * @throws IOException if the image cannot be saved
   */
  Response save(ImageInterface image, String path) throws IOException;

  /**
   * Get the extension of the image type that this class handles.
   * @return the extension of the image type that this class handles
   */
  String getExtension();
}
