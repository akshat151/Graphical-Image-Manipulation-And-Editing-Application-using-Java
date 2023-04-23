package imagecontroller.commands;

import java.util.List;
import java.util.Map;

import imagecontroller.ImageCommandInterface;
import imagemodel.ImageInterface;
import imagemodel.ImageStore;

/**
 * RGBSplit class implements ImageCommandInterface. RGBSplit class is used to split an image into
 * three images, one for each color channel.
 */
public class RGBSplit implements ImageCommandInterface {

  @Override
  public void operate(ImageStore imageStore, List<String> arguments)
          throws IllegalArgumentException {

    Map<String, ImageInterface> images = imageStore.getImages();
    CommandUtil.validateArgCount("RGB Split", arguments, 4);
    CommandUtil.validateImageExists(images, arguments.get(0));
    List<ImageInterface> result = images.get(arguments.get(0)).rgbSplit();
    images.put(arguments.get(1), result.get(0));
    images.put(arguments.get(2), result.get(1));
    images.put(arguments.get(3), result.get(2));
  }

  @Override
  public String toString() {
    return "rgb-split img-src img-red img-green img-blue";
  }
}
