package imagecontroller.commands;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import imagecontroller.ImageCommandInterface;
import imagemodel.ColorTransform;
import imagemodel.ImageInterface;
import imagemodel.ImageStore;

/**
 * Greyscale class implements ImageCommandInterface. Greyscale class is used to greyscale an image.
 * This class operates on all three color channels of an image unlike the other version of greyscale
 * which only operates on any one specified channel.
 */
public class Greyscale implements ImageCommandInterface {

  final ColorTransform greyscale;

  /**
   * Greyscale constructor creates a new Greyscale object. Greyscale constructor creates a new
   * ColorTransform object that is used to greyscale an image.
   */
  public Greyscale() {
    List<List<Double>> greyscaleTransform = new ArrayList<>();
    greyscaleTransform.add(Arrays.asList(0.2126, 0.7152, 0.0722));
    greyscaleTransform.add(Arrays.asList(0.2126, 0.7152, 0.0722));
    greyscaleTransform.add(Arrays.asList(0.2126, 0.7152, 0.0722));
    greyscale = new ColorTransform(greyscaleTransform);
  }

  @Override
  public void operate(ImageStore imageStore, List<String> arguments)
          throws IllegalArgumentException {

    Stack<String> workedOn = imageStore.getStack();
    if (arguments.size() == 1 && !workedOn.isEmpty()) {
      String source = workedOn.peek();
      arguments.add(source);
      arguments.add(source + Instant.now());
    }
    arguments = CommandUtil.fixArgs(workedOn, arguments);

    if (arguments == null || arguments.size() == 0) {
      throw new IllegalArgumentException("Greyscale command needs 2 or 3 arguments");
    }

    Map<String, ImageInterface> images = imageStore.getImages();
    if (arguments.size() == 3) {
      CommandUtil.validateImageExists(images, arguments.get(1));
      ImageInterface result = images.get(arguments.get(1)).greyscale(arguments.get(0));
      images.put(arguments.get(2), result);
      workedOn.push(arguments.get(2));
    } else if (arguments.size() == 2) {
      CommandUtil.validateImageExists(images, arguments.get(0));
      ImageInterface result = images.get(arguments.get(0)).greyscale(greyscale);
      images.put(arguments.get(1), result);
      workedOn.push(arguments.get(1));
    } else {
      throw new IllegalArgumentException("Greyscale command needs 2 or 3 arguments");
    }
  }

  public String toString() {
    return "greyscale component img-src img-dest\ngreyscale img-src img-dest";
  }
}
