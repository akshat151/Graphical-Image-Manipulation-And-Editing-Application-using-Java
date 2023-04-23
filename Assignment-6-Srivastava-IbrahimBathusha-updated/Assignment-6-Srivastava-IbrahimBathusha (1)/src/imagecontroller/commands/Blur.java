package imagecontroller.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import imagecontroller.ImageCommandInterface;
import imagemodel.ImageInterface;
import imagemodel.ImageStore;
import imagemodel.Kernel;

/**
 * Blur class implements ImageCommandInterface. Blur class is used to blur an image. Blur class
 * takes in two arguments, the first argument is the name of the image to be blurred, the second
 * argument is the name of the blurred image.
 */
public class Blur implements ImageCommandInterface {

  public final Kernel blur;

  /**
   * Blur constructor creates a new Blur object. Blur constructor creates a new Kernel object
   * that is used to blur an image.
   */
  public Blur() {
    List<List<Double>> blurFilter = new ArrayList<>();
    blurFilter.add(Arrays.asList(0.0625, 0.125, 0.0625));
    blurFilter.add(Arrays.asList(0.125, 0.25, 0.125));
    blurFilter.add(Arrays.asList(0.0625, 0.125, 0.0625));
    blur = new Kernel(blurFilter);
  }

  @Override
  public void operate(ImageStore imageStore, List<String> arguments)
          throws IllegalArgumentException {
    Stack<String> workedOn = imageStore.getStack();
    arguments = CommandUtil.fixArgs(workedOn, arguments);

    Map<String, ImageInterface> images = imageStore.getImages();
    CommandUtil.validateArgCount("Blur", arguments, 2);
    CommandUtil.validateImageExists(images, arguments.get(0));
    ImageInterface result = images.get(arguments.get(0)).blur(blur);
    images.put(arguments.get(1), result);
    workedOn.push(arguments.get(1));
  }

  @Override
  public String toString() {
    return "blur img-src img-dest";
  }
}
