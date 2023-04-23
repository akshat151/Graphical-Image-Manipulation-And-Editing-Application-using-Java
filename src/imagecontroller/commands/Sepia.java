package imagecontroller.commands;

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
 * Sepia class implements ImageCommandInterface. Sepia class is used to apply a sepia filter to an
 * image.
 */
public class Sepia implements ImageCommandInterface {
  final ColorTransform sepia;

  /**
   * Constructor for Sepia class. Initializes the sepia filter.
   */
  public Sepia() {
    List<List<Double>> sepiaTransform = new ArrayList<>();
    sepiaTransform.add(Arrays.asList(0.393, 0.769, 0.189));
    sepiaTransform.add(Arrays.asList(0.349, 0.686, 0.168));
    sepiaTransform.add(Arrays.asList(0.272, 0.534, 0.131));
    sepia = new ColorTransform(sepiaTransform);
  }

  @Override
  public void operate(ImageStore imageStore, List<String> arguments)
          throws IllegalArgumentException {
    Stack<String> workedOn = imageStore.getStack();
    arguments = CommandUtil.fixArgs(workedOn, arguments);

    Map<String, ImageInterface> images = imageStore.getImages();
    CommandUtil.validateArgCount("Sepia", arguments, 2);
    CommandUtil.validateImageExists(images, arguments.get(0));
    ImageInterface result = images.get(arguments.get(0)).sepia(sepia);
    images.put(arguments.get(1), result);
    workedOn.push(arguments.get(1));
  }

  @Override
  public String toString() {
    return "sepia img-src img-dest";
  }
}
