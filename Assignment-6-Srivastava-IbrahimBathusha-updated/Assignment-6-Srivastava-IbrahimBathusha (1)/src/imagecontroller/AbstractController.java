package imagecontroller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import helpers.ImageImplHelper;
import helpers.Response;
import imagecontroller.commands.Blur;
import imagecontroller.commands.Brighten;
import imagecontroller.commands.Dither;
import imagecontroller.commands.Greyscale;
import imagecontroller.commands.HorizontalFlip;
import imagecontroller.commands.Load;
import imagecontroller.commands.RGBCombine;
import imagecontroller.commands.RGBSplit;
import imagecontroller.commands.Save;
import imagecontroller.commands.Sepia;
import imagecontroller.commands.Sharpen;
import imagecontroller.commands.VerticalFlip;
import imagemodel.ImageStore;

/**
 * Abstract class implements ImageControllerInterfaces and pulls out common functionality.
 */
public abstract class AbstractController implements ImageControllerInterface {

  protected final Map<String, ImageCommandInterface> knownCommands = new HashMap<>();
  ImageStore imageStore;

  ImageImplHelper imgHelper;

  /**
   * Constructs an AbstractController object.
   *
   * @param imgHelper  Helper that contains methods for loading/combining etc.
   * @param imageStore Wrapper class containing stack of images and map of image name to image.
   */
  public AbstractController(ImageImplHelper imgHelper, ImageStore imageStore) {

    if (imgHelper == null) {
      throw new IllegalArgumentException("Image Helper cannot be null");
    }

    if (imageStore == null) {
      throw new IllegalArgumentException("ImageStore cannot be null");
    }

    this.imageStore = imageStore;
    this.imgHelper = imgHelper;

    knownCommands.put("load", new Load(imgHelper));
    knownCommands.put("save", new Save());
    knownCommands.put("greyscale", new Greyscale());
    knownCommands.put("horizontal-flip", new HorizontalFlip());
    knownCommands.put("vertical-flip", new VerticalFlip());
    knownCommands.put("brighten", new Brighten());
    knownCommands.put("rgb-split", new RGBSplit());
    knownCommands.put("rgb-combine", new RGBCombine(imgHelper));
    knownCommands.put("blur", new Blur());
    knownCommands.put("sharpen", new Sharpen());
    knownCommands.put("sepia", new Sepia());
    knownCommands.put("dither", new Dither());
  }

  /**
   * This method is responsible for handling the delegation of the operations to the appropriate
   * handler.
   *
   * @param command   the command to be executed.
   * @param arguments the arguments for the command.
   * @return the response from the handler.
   */
  public Response operationHandler(String command, List<String> arguments) {
    command = command.toLowerCase().trim();

    ImageCommandInterface operation;

    try {
      operation = knownCommands.get(command);
      if (operation == null) {
        throw new IllegalArgumentException("Invalid Command please try again.");
      } else {
        operation.operate(imageStore, arguments);
      }
    } catch (IllegalArgumentException ile) {
      return new Response("Failure: " + ile.getMessage() + " ", Response.ResponseType.ERROR);
    }

    return new Response("Success: " + command + "\n ", Response.ResponseType.SUCCESS);
  }
}
