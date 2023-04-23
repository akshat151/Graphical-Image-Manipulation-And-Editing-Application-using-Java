package imagecontroller.commands;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import imagecontroller.ImageCommandInterface;
import imagemodel.ImageInterface;
import imagemodel.ImageStore;
import imagemodel.Kernel;

public class Mosaic implements ImageCommandInterface {

  @Override
  public void operate(ImageStore imageStore, List<String> arguments)
          throws IllegalArgumentException {

    Stack<String> workedOn = imageStore.getStack();
    if (arguments.size() == 0 && workedOn.size() > 0) {
      arguments.add("10");
      String source = workedOn.peek();
      arguments.add(source);
      arguments.add(source + Instant.now());
    }

    Map<String, ImageInterface> images = imageStore.getImages();
    CommandUtil.validateArgCount("Mosaic", arguments, 3);
    CommandUtil.validateImageExists(images, arguments.get(1));
    ImageInterface result = images.get(arguments.get(1))
            .mosaic(Integer.parseInt(arguments.get(0)));
    images.put(arguments.get(2), result);
    workedOn.push(arguments.get(2));
  }

  @Override
  public String toString() {
    return "mosaic num-seeds img-src img-dest";
  }
}
