package imagecontroller.commands;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import imagecontroller.ImageCommandInterface;
import imagecontroller.savers.JPEGSaver;
import imagecontroller.savers.PNGSaver;
import imagecontroller.savers.PPMSaver;
import imagemodel.ImageInterface;
import imagecontroller.ImageSaver;
import imagemodel.ImageStore;

/**
 * Save class implements ImageCommandInterface. Save class is used to save an image.
 */
public class Save implements ImageCommandInterface {

  private final Map<String, ImageSaver> imageSaverMap = new HashMap<>() {
    {
      put("ppm", new PPMSaver());
      put("jpeg", new JPEGSaver());
      put("jpg", new JPEGSaver());
      put("png", new PNGSaver());

      // Add variations of the file extensions
      put("PPM", new PPMSaver());
      put("JPEG", new JPEGSaver());
      put("JPG", new JPEGSaver());
      put("PNG", new PNGSaver());
    }
  };

  @Override
  public void operate(ImageStore imageStore, List<String> arguments)
          throws IllegalArgumentException {

    Map<String, ImageInterface> images = imageStore.getImages();

    // Validate if the number of arguments is either 1 or 2
    if (arguments.size() > 2) {
      throw new IllegalArgumentException("The number of arguments is not valid");
    }

    CommandUtil.validateFolder(arguments.get(0));

    ImageInterface imageToSave = null;

    if (arguments.size() == 1) {
      CommandUtil.validateImageExists(images, imageStore.getStack().peek());
      imageToSave = images.get(imageStore.getStack().peek());
    } else {
      CommandUtil.validateImageExists(images, arguments.get(1));
      imageToSave = images.get(arguments.get(1));
    }

    if (imageToSave == null) {
      throw new IllegalArgumentException("Image does not exist");
    }

    String savePath = arguments.get(0);

    // Check if the file path is valid
    if (savePath == null || savePath.equals("")) {
      throw new IllegalArgumentException("The file path is not valid");
    }

    // Get the file extension
    String extension = savePath.substring(savePath.lastIndexOf(".") + 1);

    System.out.println("Extension : " + extension);

    // Check if the extension is supported
    if (!this.imageSaverMap.containsKey(extension)) {
      throw new IllegalArgumentException("The extension is not supported");
    }

    ImageSaver imageSaver = this.imageSaverMap.get(extension);

    if (imageSaver == null) {
      throw new IllegalArgumentException("The extension is not supported");
    }

    try {
      imageSaver.save(
            imageToSave,
            savePath
      );
    } catch (IOException e) {
      throw new IllegalArgumentException("The image could not be saved due to : " + e.getMessage());
    }
  }

  @Override
  public String toString() {
    return "Save file-path image-name";
  }
}
