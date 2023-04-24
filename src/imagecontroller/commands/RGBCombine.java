package imagecontroller.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import helpers.ImageImplHelper;
import imagecontroller.ImageCommandInterface;
import imagemodel.ImageInterface;
import imagemodel.ImageStore;

/**
 * RGBCombine class implements ImageCommandInterface.
 * RGBCombine class is used to combine three greyscale components(images) into one image.
 */
public class RGBCombine implements ImageCommandInterface {

  private final ImageImplHelper imgHelper;

  /**
   * Constructor for RGBCombine class.
   *
   * @param imgHelper the helper class for image implementation
   */
  public RGBCombine(ImageImplHelper imgHelper) {
    this.imgHelper = imgHelper;
  }

  @Override
  public void operate(ImageStore imageStore, List<String> arguments)
          throws IllegalArgumentException {
    Map<String, ImageInterface> images = imageStore.getImages();

    CommandUtil.validateArgCount("RGB Combine", arguments, 4);
    CommandUtil.validateImageExists(images, arguments.get(1));
    CommandUtil.validateImageExists(images, arguments.get(2));
    CommandUtil.validateImageExists(images, arguments.get(3));

    ArrayList<ImageInterface> rgb = new ArrayList<>();
    rgb.add(images.get(arguments.get(1)));
    rgb.add(images.get(arguments.get(2)));
    rgb.add(images.get(arguments.get(3)));

    ImageInterface result = imgHelper.rgbCombine(rgb);
    images.put(arguments.get(0), result);
  }

  @Override
  public String toString() {
    return "rgb-combine img-dest img-red img-green img-blue";
  }
}
