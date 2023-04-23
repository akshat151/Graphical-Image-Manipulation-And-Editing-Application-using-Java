package helpers.loaders;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import helpers.ImageUtil;
import imagemodel.ImageInterface;

/**
 * This class represents the PPMLoader class. It is a concrete implementation of the ImageLoader
 * interface.
 */
public class PPMLoader implements ImageLoader {

  @Override
  public ImageInterface loadImage(FileInputStream fileInputStream) throws IOException {
    if (fileInputStream.available() == 0) {
      throw new IllegalArgumentException("The file is empty");
    }
    return loadImagePPM(fileInputStream.readAllBytes());
  }

  /**
   * Load an image from the given bytes array.
   *
   * @param fileBytes the binary content of the file to load the image from
   * @return an image object
   */
  private ImageInterface loadImagePPM(byte[] fileBytes) {
    // Validate that the file is not empty
    if (fileBytes.length == 0) {
      throw new IllegalArgumentException("The file is empty");
    }
    return ImageUtil.readPPM(fileBytes);
  }

  @Override
  public String getSupportedExtension() {
    return "ppm";
  }

  @Override
  public boolean isValidImage(String filePath) {
    try {
      return this.loadImagePPM(Files.readAllBytes(Paths.get(filePath))) != null;
    } catch (IOException e) {
      return false;
    }
  }
}
