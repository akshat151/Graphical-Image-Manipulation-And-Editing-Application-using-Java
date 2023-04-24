package imagecontroller.commands;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import imagecontroller.ImageCommandInterface;
import imagemodel.ImageInterface;
import imagemodel.ImageStore;

public class Mosaic implements ImageCommandInterface {

  @Override
  public void operate(ImageStore imageStore, List<String> arguments)
          throws IllegalArgumentException {

    Stack<String> workedOn = imageStore.getStack();

    if (arguments.size() == 1 && !workedOn.isEmpty()) {
      String source = workedOn.peek();
      arguments.add(source);
      arguments.add(source + Instant.now());
    }
    CommandUtil.fixArgs(workedOn, arguments);

    if (arguments.size() == 0) {
      throw new IllegalArgumentException("Mosaic command needs seed value");
    }

    Map<String, ImageInterface> images = imageStore.getImages();
    CommandUtil.validateArgCount("Mosaic", arguments, 3);
    CommandUtil.validateImageExists(images, arguments.get(1));
    if (!arguments.get(0).equals("")) {
      int seeds = Integer.parseInt(arguments.get(0));
      if (seeds > 0) {
        ImageInterface result = images.get(arguments.get(1))
                .mosaic(seeds);
        images.put(arguments.get(2), result);
        workedOn.push(arguments.get(2));
      } else {
        throw new IllegalArgumentException("Please enter positive number of seeds");
      }
    }
  }

  @Override
  public String toString() {
    return "mosaic num-seeds img-src img-dest";
  }
}
