package imagecontroller.savers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import imagecontroller.ImageSaver;
import imagemodel.ImageInterface;
import imagemodel.PixelInterface;
import helpers.Response;

/**
 * GenericSaver class implements ImageSaver. GenericSaver class is used to save an image. Any image
 * type that is supported by the javax.imageio package must be handled by a class that extends this
 * class.
 */
public abstract class GenericSaver implements ImageSaver {

  @Override
  public Response save(ImageInterface image, String path) throws IOException {

    // Check if the path is valid
    if (path == null || path.isEmpty()) {
      throw new IllegalArgumentException("Invalid path");
    }

    // Check if the image is valid
    if (image == null) {
      throw new IllegalArgumentException("Invalid image");
    }

    // Check if the path has the correct extension
    if (!path.endsWith(this.getExtension())) {
      throw new IllegalArgumentException("Invalid extension");
    }

    // Save the image
    BufferedImage bufferedImage = getBufferedImage(image);

    // Open a file
    File outputFile = new File(path);
    // Write the image to the file
    ImageIO.write(bufferedImage, this.getExtension(), outputFile);

    return new Response("Image saved successfully");
  }

  /**
   * Method to convert an ImageInterface object to BufferedImage.
   * @param imageInterfaceObject object of type ImageInterface.
   */
  public static BufferedImage getBufferedImage(ImageInterface imageInterfaceObject) {
    List<List<PixelInterface>> imageArray = imageInterfaceObject.getImageArray();
    BufferedImage image = new BufferedImage(
          imageArray.get(0).size(),
          imageArray.size(),
          BufferedImage.TYPE_INT_RGB
    );
    for (int i = 0; i < imageArray.size(); i++) {
      for (int j = 0; j < imageArray.get(0).size(); j++) {
        // Make the corresponding rgb value from the red, green and blue values
        int rgbVal = (imageArray.get(i).get(j).getRed() << 16)
              | (imageArray.get(i).get(j).getGreen() << 8)
              | imageArray.get(i).get(j).getBlue();
        image.setRGB(j, i, rgbVal);
      }
    }
    return image;
  }
}
