package imagecontroller.commands;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import helpers.Constants;
import imagecontroller.ImageCommandInterface;
import imagemodel.ImageInterface;
import imagemodel.ImageStore;

/**
 * This class represents the Mosaic operation, that is performed
 * on an image.
 */
public class Mosaic implements ImageCommandInterface {

  @Override
  public void operate(ImageStore imageStore, List<String> arguments)
          throws IllegalArgumentException {

    Stack<String> workedOn = imageStore.getStack();

    if (arguments.size() == 0 && workedOn.size() > 0) {
      arguments.add(Constants.NUM_OF_SEEDS);
      String source = workedOn.peek();
      arguments.add(source);
      arguments.add(source + Instant.now());
    } else if (arguments.size() == 1 && !workedOn.isEmpty()) {
      String source = workedOn.peek();
      arguments.add(source);
      arguments.add(source + Instant.now());
    }
    CommandUtil.fixArgs(workedOn, arguments);

    Map<String, ImageInterface> images = imageStore.getImages();
    CommandUtil.validateArgCount(Constants.MOSAIC_CAP, arguments, 3);
    CommandUtil.validateImageExists(images, arguments.get(1));
    if (!arguments.get(0).equals(Constants.EMPTY_MSG)) {
      int seeds = Integer.parseInt(arguments.get(0));
      if (seeds > 0) {
        ImageInterface result = images.get(arguments.get(1))
                .mosaic(seeds);
        images.put(arguments.get(2), result);
        workedOn.push(arguments.get(2));
      } else {
        throw new IllegalArgumentException(Constants.MOSAIC_COMMAND_ERR_MESSAGE);
      }
    }
  }

  @Override
  public String toString() {
    return Constants.MOSAIC_COMMAND;
  }
}
