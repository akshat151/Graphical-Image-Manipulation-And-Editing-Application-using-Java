package imagecontroller;

import java.io.IOException;
import java.util.List;

/**
 * This class is used to handle operations of the GraphicalController class.
 */
public class GraphicHelper {

  /**
   * This method is used to load an image.
   *
   * @param imageFile  the path to the image file
   * @param controller the controller
   * @throws IOException if the image file is not found or unreadable
   */
  public static void loadImage(String imageFile, GraphicalController controller)
          throws IOException {
    controller.loadImage(imageFile);
  }

  /**
   * This method is used to load multiple images.
   *
   * @param imageFiles the list of paths to the image files
   * @param controller the controller
   * @throws IOException if any of the image files is not found or unreadable
   */
  public static void loadMultipleImages(List<String> imageFiles, GraphicalController controller)
          throws IOException {
    for (String imageFile : imageFiles) {
      controller.loadImage(imageFile);
    }
  }

  /**
   * This method is used to retrieve the most recent image from the image store and push to the
   * view.
   *
   * @param controller appropriate controller.
   * @throws IOException if no recent image exists.
   */
  public static void getRecentImage(GraphicalController controller) throws IOException {
    controller.getRecentImage();
  }

  /**
   * This method is used to retrieve the name of the most recent image from the image store.
   *
   * @param controller appropriate controller.
   * @return the name of the most recent image.
   */
  public static String getRecentImageName(GraphicalController controller) {
    return controller.imageStore.getStack().peek();
  }

  /**
   * This method is used to perform an undo of the last operation performed in the application.
   *
   * @param controller appropriate controller.
   */
  public static void undo(GraphicalController controller) {
    controller.undo();
  }
}
