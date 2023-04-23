package imagecontroller.commands;

import java.util.List;
import java.util.Map;

import helpers.ImageImplHelper;
import imagecontroller.ImageCommandInterface;
import imagemodel.ImageInterface;
import imagemodel.ImageStore;

/**
 * Load class implements ImageCommandInterface. Load class is used to load an image.
 */
public class Load implements ImageCommandInterface {

  private final ImageImplHelper imgHelper;

  /**
   * Constructor for Load class.
   * @param imgHelper the helper class for image implementation
   */
  public Load(ImageImplHelper imgHelper) {
    this.imgHelper = imgHelper;
  }

  @Override
  public void operate(ImageStore imageStore, List<String> arguments)
          throws IllegalArgumentException {

    Map<String, ImageInterface> images = imageStore.getImages();

    CommandUtil.validateArgCount("Load", arguments, 2);
    CommandUtil.validateFilePath(arguments.get(0));
    ImageInterface loadedImage = imgHelper.loadImage(arguments.get(0));
    images.put(arguments.get(1), loadedImage);
  }

  @Override
  public String toString() {
    return "Load file-path image-name";
  }
}
