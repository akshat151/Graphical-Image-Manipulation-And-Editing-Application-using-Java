package helpers.loaders;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import imagemodel.ImageImpl;
import imagemodel.ImageInterface;
import imagemodel.PixelInterface;
import imagemodel.RGBPixel;

/**
 * This class implements the ImageLoader interface. It provides the basic functionality to load an
 * image from a file. It also provides the functionality to check if a file is a valid image.
 * <p>
 * This class only operates on image formats supported by the ImageIO class. The supported formats
 * are: BMP, GIF, JPEG, PNG, WBMP, and JPEG.
 * </p>
 */
public abstract class GeneralLoader implements ImageLoader {

  // Get the supported ImageReader and Register the ImageReader with the ImageIO class
  static {
    ImageIO.scanForPlugins();
  }

  @Override
  public ImageInterface loadImage(FileInputStream fileInputStream) throws IOException {

    // Check if fileInputStream is null
    if (fileInputStream == null) {
      throw new IllegalArgumentException("The file input stream is null");
    }

    // Check if the stream is empty
    if (fileInputStream.available() == 0) {
      throw new IllegalArgumentException("The file input stream is empty");
    }

    BufferedImage image = ImageIO.read(fileInputStream);

    // Check if the image is null - ImageIO.read() returns null if the file is not a valid image
    // It fails silently. Does not throw an exception.
    if (image == null) {
      throw new IllegalArgumentException("Invalid image bytes");
    }

    // Construct a List<List<PixelInterface>> from the BufferedImage
    List<List<PixelInterface>> pixelList = new ArrayList<>();
    for (int i = 0; i < image.getHeight(); i++) {
      List<PixelInterface> row = new ArrayList<>();
      for (int j = 0; j < image.getWidth(); j++) {
        int rgb = image.getRGB(j, i);
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = (rgb) & 0xFF;
        PixelInterface pixel = new RGBPixel(red, green, blue);
        row.add(pixel);
      }
      pixelList.add(row);
    }

    // Convert from BufferedImage to ImageInterface

    // Return the image
    return new ImageImpl(pixelList);
  }

  @Override
  public boolean isValidImage(String filePath) {
    // Check if the file path is valid
    if (filePath != null || filePath.length() > 0) {
      return false;
    }

    // Check if the file path has the supported extension
    if (!filePath.endsWith(getSupportedExtension())) {
      return false;
    }

    // Check if the file exists
    File file = new File(filePath);
    if (!file.exists()) {
      return false;
    }

    // Check if the file is a valid image
    try {
      loadImage(
              new FileInputStream(
                      file.toPath().toString()
              )
      );
      return true;
    } catch (IOException e) {
      return false;
    }
  }
}
