package imagecontroller.commands;

import java.util.List;
import java.util.Map;
import java.util.Stack;

import imagemodel.ImageInterface;
import imagecontroller.ImageCommandInterface;
import imagemodel.ImageStore;

/**
 * Dither class implements ImageCommandInterface. Dither class is used to dither an image.
 */
public class Dither implements ImageCommandInterface {

  @Override
  public void operate(ImageStore imageStore, List<String> arguments)
          throws IllegalArgumentException {
    Stack<String> workedOn = imageStore.getStack();
    arguments = CommandUtil.fixArgs(workedOn, arguments);

    Map<String, ImageInterface> images = imageStore.getImages();
    CommandUtil.validateArgCount("Dither", arguments, 2);
    CommandUtil.validateImageExists(images, arguments.get(0));
    ImageInterface result = images.get(arguments.get(0)).dither();
    images.put(arguments.get(1), result);
    workedOn.push(arguments.get(1));
  }

  @Override
  public String toString() {
    return "dither img-src img-dest";
  }
}
