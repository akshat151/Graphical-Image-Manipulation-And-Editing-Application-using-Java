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
 * Sharpen class implements ImageCommandInterface. Sharpen class is used to sharpen an image.
 */
public class Sharpen implements ImageCommandInterface {

  public final Kernel sharpen;

  /**
   * Sharpen constructor.
   */
  public Sharpen() {
    List<List<Double>> sharpenFilter = new ArrayList<>();
    sharpenFilter.add(Arrays.asList(-0.125, -0.125, -0.125, -0.125, -0.125));
    sharpenFilter.add(Arrays.asList(-0.125, 0.25, 0.25, 0.25, -0.125));
    sharpenFilter.add(Arrays.asList(-0.125, 0.25, 1.0, 0.25, -0.125));
    sharpenFilter.add(Arrays.asList(-0.125, 0.25, 0.25, 0.25, -0.125));
    sharpenFilter.add(Arrays.asList(-0.125, -0.125, -0.125, -0.125, -0.125));
    sharpen = new Kernel(sharpenFilter);
  }

  @Override
  public void operate(ImageStore imageStore, List<String> arguments)
          throws IllegalArgumentException {

    Stack<String> workedOn = imageStore.getStack();
    arguments = CommandUtil.fixArgs(workedOn, arguments);

    Map<String, ImageInterface> images = imageStore.getImages();
    CommandUtil.validateArgCount("Sharpen", arguments, 2);
    CommandUtil.validateImageExists(images, arguments.get(0));
    ImageInterface result = images.get(arguments.get(0)).sharpen(sharpen);
    images.put(arguments.get(1), result);
    workedOn.push(arguments.get(1));
  }

  @Override
  public String toString() {
    return "sharpen img-src img-dest";
  }
}
