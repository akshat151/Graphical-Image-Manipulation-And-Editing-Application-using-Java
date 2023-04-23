package imagecontroller;

import java.util.List;
import imagemodel.ImageStore;

/**
 * This interface represents any command that can be executed on an image.
 */
public interface ImageCommandInterface {

  /**
   * This method executes the command on the image. It takes in a map of images and a list of
   * arguments. The first argument is the name of the image to be operated on. The second argument
   * is the name of the image to be created. The rest of the arguments are specific to the command.
   * For example, the blur command takes in a kernel as an argument.
   * @param imageStore wrapper around the map of images
   * @param arguments the list of arguments
   * @throws IllegalArgumentException if the arguments are invalid
   */
  void operate(ImageStore imageStore, List<String> arguments)
      throws IllegalArgumentException;
}
