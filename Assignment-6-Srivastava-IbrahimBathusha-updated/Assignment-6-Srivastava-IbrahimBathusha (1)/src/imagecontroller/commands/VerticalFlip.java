package imagecontroller.commands;

import java.util.List;
import java.util.Map;
import java.util.Stack;

import imagecontroller.ImageCommandInterface;
import imagemodel.ImageInterface;
import imagemodel.ImageStore;

/**
 * VerticalFlip class implements ImageCommandInterface. VerticalFlip class is used to flip an
 * image vertically.
 */
public class VerticalFlip implements ImageCommandInterface {

  @Override
  public void operate(ImageStore imageStore, List<String> arguments)
          throws IllegalArgumentException {
    Stack<String> workedOn = imageStore.getStack();
    arguments = CommandUtil.fixArgs(workedOn, arguments);

    Map<String, ImageInterface> images = imageStore.getImages();
    CommandUtil.validateArgCount("Vertical Flip", arguments, 2);
    CommandUtil.validateImageExists(images, arguments.get(0));
    ImageInterface result = images.get(arguments.get(0)).flipVertical();
    images.put(arguments.get(1), result);
    workedOn.push(arguments.get(1));
  }

  @Override
  public String toString() {
    return "vertical-flip img-src img-dest";
  }
}
