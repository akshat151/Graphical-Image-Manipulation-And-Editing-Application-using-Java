package imagemodel;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * This class represents an image store. It stores a map of image names to images.
 */
public class ImageStore {

  private final Map<String, ImageInterface> images;

  private final Stack<String> imageBeingWorkedOn;

  /**
   * This constructor creates an image store.
   */
  public ImageStore() {
    images = new HashMap<>();
    imageBeingWorkedOn = new Stack<>();
  }

  public Map<String, ImageInterface> getImages() {
    return this.images;
  }

  public Stack<String> getStack() {
    return this.imageBeingWorkedOn;
  }
}