package imagecontroller.commands;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import imagecontroller.ImageCommandInterface;
import imagemodel.ImageInterface;
import imagemodel.ImageStore;

/**
 * Brighten class implements ImageCommandInterface. Brighten class is used to brighten an image.
 */
public class Brighten implements ImageCommandInterface {

  @Override
  public void operate(ImageStore imageStore, List<String> arguments)
          throws IllegalArgumentException {

    Stack<String> workedOn = imageStore.getStack();
    if (arguments.size() == 0 && workedOn.size() > 0) {
      arguments.add("5");
      String source = workedOn.peek();
      arguments.add(source);
      arguments.add(source + Instant.now());
    }

    Map<String, ImageInterface> images = imageStore.getImages();
    CommandUtil.validateArgCount("Brighten", arguments, 3);
    CommandUtil.validateImageExists(images, arguments.get(1));
    ImageInterface result = images.get(arguments.get(1))
            .brighten(Integer.parseInt(arguments.get(0)));
    images.put(arguments.get(2), result);
    workedOn.push(arguments.get(2));
  }

  @Override
  public String toString() {
    return "brighten value img-src img-dest";
  }
}