package helpers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import helpers.loaders.ImageLoader;
import helpers.loaders.JpegLoader;
import helpers.loaders.PNGLoader;
import helpers.loaders.PPMLoader;
import helpers.loaders.BMPLoader;
import imagemodel.ImageImpl;
import imagemodel.ImageInterface;
import imagemodel.PixelInterface;
import imagemodel.RGBPixel;

/**
 * This class contains methods to load a PPM image from file and return an ImageInterface object.
 * It also contains method to combine rgb components of an image into one image.
 */
public class ImageImplHelper {

  // Map of all available loaders
  private final Map<String, ImageLoader> loaders = new HashMap<>();

  /**
   * Constructor for the ImageImplHelper class.
   */
  public ImageImplHelper() {
    // Add all the loaders to the map
    loaders.put("ppm", new PPMLoader());
    loaders.put("png", new PNGLoader());
    loaders.put("jpeg", new JpegLoader());

    // Add variations in spelling of jpeg
    loaders.put("jpg", loaders.get("jpeg"));
    loaders.put("JPEG", loaders.get("jpeg"));
    loaders.put("JPG", loaders.get("jpeg"));

    // Add variations in spelling of png
    loaders.put("PNG", loaders.get("png"));

    // Add variations in spelling of ppm
    loaders.put("PPM", loaders.get("ppm"));

    // Add BMP loader
    loaders.put("bmp", new BMPLoader());

    // Add variations in spelling of bmp
    loaders.put("BMP", loaders.get("bmp"));
  }

  /**
   * Load an image from the given file.
   *
   * @param filePath the path to the file to load the image from
   * @return an image object
   */
  public ImageInterface loadImagePPM(String filePath) {
    // Validate that the file path is not null
    if (filePath == null) {
      throw new IllegalArgumentException("The file path is null");
    }

    // Validate that the file path ends with .ppm
    if (!filePath.endsWith(".ppm")) {
      throw new IllegalArgumentException("The file path does not end with .ppm");
    }

    // Validate that the file exists
    File file = new File(filePath);
    if (!file.exists()) {
      throw new IllegalArgumentException("The file does not exist : " + filePath + "\n");
    }

    // Validate that the file is not empty
    if (file.length() == 0) {
      throw new IllegalArgumentException("The file is empty");
    }
    return ImageUtil.readPPM(filePath);
  }

  /**
   * Load an image from the given file.
   *
   * @param filePath the path to the file to load the image from
   * @return an image object
   */
  public ImageInterface loadImage(String filePath) {
    // Validate that the file path is not null
    if (filePath == null) {
      throw new IllegalArgumentException("The file path is null");
    }

    // Validate that the file exists
    File file = new File(filePath);
    if (!file.exists()) {
      throw new IllegalArgumentException("The file does not exist : " + filePath + "\n");
    }

    // Validate that the file is not empty
    if (file.length() == 0) {
      throw new IllegalArgumentException("The file is empty");
    }

    // Identify the type of the file
    String fileType = filePath.substring(filePath.lastIndexOf(".") + 1);

    ImageInterface image;
    ImageLoader loader;

    // Load using one of the loaders
    if (loaders.containsKey(fileType)) {

      loader = loaders.get(fileType);
      try {
        FileInputStream stream = new FileInputStream(filePath);
        image = loader.loadImage(stream);
      } catch (IOException e) {
        throw new IllegalArgumentException("Unable to read from File");
      }

      if (image == null) {
        throw new IllegalArgumentException("Unable to read image from file");
      }
    } else {
      throw new IllegalArgumentException("The file type is not supported");
    }
    return image;
  }

  /**
   * Combine the given channels into a single image.
   *
   * @param rgbSplit list of 3 images that need to be combined.
   * @return ImageInterface object that is the combined result of the given input.
   */
  public ImageInterface rgbCombine(List<ImageInterface> rgbSplit) {

    if (rgbSplit == null) {
      throw new IllegalArgumentException("List of images is null");
    }

    if (rgbSplit.size() != 3) {
      throw new IllegalArgumentException("Need 3 components to combine into one image");
    }

    if (rgbSplit.get(0) == null || rgbSplit.get(1) == null || rgbSplit.get(2) == null) {
      throw new IllegalArgumentException("One of the images is null");
    }

    if (!rgbSplit.get(0).getSize().equals(rgbSplit.get(1).getSize())
            || !rgbSplit.get(0).getSize().equals(rgbSplit.get(2).getSize())) {
      throw new IllegalArgumentException("3 images must be of the same size");
    }

    if (!rgbSplit.get(0).getSize().equals(rgbSplit.get(1).getSize())
            || !rgbSplit.get(0).getSize().equals(rgbSplit.get(2).getSize())) {
      throw new IllegalArgumentException("3 images must be of the same size");
    }

    List<Integer> size = rgbSplit.get(0).getSize();
    int n = size.get(0);
    int m = size.get(1);

    List<List<PixelInterface>> combineImageArray = new ArrayList<>();

    for (int i = 0; i < n; i++) {
      List<PixelInterface> row = new ArrayList<>();
      for (int j = 0; j < m; j++) {
        PixelInterface redP = rgbSplit.get(0).getImageArray().get(i).get(j);
        PixelInterface greenP = rgbSplit.get(1).getImageArray().get(i).get(j);
        PixelInterface blueP = rgbSplit.get(2).getImageArray().get(i).get(j);
        int r = redP.getRed();
        int g = greenP.getGreen();
        int b = blueP.getBlue();
        checkGreyscale(r,redP.getGreen(), redP.getBlue());
        checkGreyscale(g,greenP.getRed(), greenP.getBlue());
        checkGreyscale(b,blueP.getGreen(), blueP.getRed());
        row.add(new RGBPixel(r, g, b));
      }
      combineImageArray.add(row);
    }
    return new ImageImpl(combineImageArray);
  }

  private void checkGreyscale(int val, int valX, int valY) throws IllegalArgumentException {
    if (val != valX || val != valY) {
      throw new IllegalArgumentException("Image provided was not greyscale");
    }
  }
}
