package imagemodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

import imagecontroller.ImageSaver;
import imagecontroller.savers.JPEGSaver;
import imagecontroller.savers.PNGSaver;
import imagecontroller.savers.PPMSaver;

/**
 * This class represents the model for the Image Processing application. It is responsible for
 * storing the image data and performing the operations on the image data. It implements the
 * ImageInterface.
 */
public class ImageImpl implements ImageInterface {
  private final List<List<PixelInterface>> imageArray;
  private int maxColorValue;
  // Image Saver Classes
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
  // Function map of all the operations that retrieve a particular component from the pixel
  private final Map<String, Function<PixelInterface, Integer>> functionMap = new HashMap<>() {
    {
      put("red-component", PixelInterface::getRed);
      put("green-component", PixelInterface::getGreen);
      put("blue-component", PixelInterface::getBlue);
      put("value-component", PixelInterface::getValue);
      put("intensity-component", PixelInterface::getIntensity);
      put("luma-component", PixelInterface::getLuma);
    }
  };

  /**
   * Construct a new Image with an empty image array.
   */
  public ImageImpl() {
    imageArray = new ArrayList<>();
  }

  /**
   * Construct a new Image with the given image array.
   *
   * @param imageArray the image array
   */
  public ImageImpl(List<List<PixelInterface>> imageArray) {

    if (imageArray == null) {
      throw new IllegalArgumentException("The image array is null");
    }

    if (imageArray.size() == 0) {
      throw new IllegalArgumentException("The image array is empty");
    }

    // Validate that the image array is in a rectangular shape and all the rows have the same width
    int width = imageArray.get(0).size();
    for (int i = 1; i < imageArray.size(); i++) {
      if (imageArray.get(i).size() != width) {
        throw new IllegalArgumentException("The image array is not in a rectangular shape");
      }
    }

    this.imageArray = imageArray;

    this.maxColorValue = 255;
  }

  // Parameterized Constructor

  /**
   * Construct a new Image with the given width and height. All pixels will be initialized to black.
   * This constructor expects a row-major order of the pixels. For example, if the image is 2x2, the
   * pixels should be in the following order: (0,0), (0,1), (1,0), (1,1).
   *
   * @param pixelsList the list of pixels in the image
   * @param width      the width of the image
   * @param height     the height of the image
   */
  public ImageImpl(List<PixelInterface> pixelsList, int width, int height, int maxColorValue) {

    // Validate that the image array is in a rectangular shape
    if (pixelsList.size() != width * height) {
      throw new IllegalArgumentException("The image array is not of expected size");
    }

    // Initialize the image array
    this.imageArray = new ArrayList<>();
    int index = 0;
    for (int i = 0; i < height; i++) {
      List<PixelInterface> row = new ArrayList<>();
      for (int j = 0; j < width; j++) {
        row.add(pixelsList.get(index));
        index++;
      }
      this.imageArray.add(row);
    }
    this.maxColorValue = maxColorValue;
  }

  /**
   * Construct a new Image with the given imageInterface object.
   * This constructor is used to create a deep copy of the given image.
   *
   * @param image the image to be copied
   */
  public ImageImpl(ImageInterface image) {
    this.imageArray = new ArrayList<>();
    for (int i = 0; i < image.getSize().get(0); i++) {
      List<PixelInterface> row = new ArrayList<>();
      for (int j = 0; j < image.getSize().get(1); j++) {
        row.add(image.getImageArray().get(i).get(j));
      }
      this.imageArray.add(row);
    }
    this.maxColorValue = 255;
  }

  @Override
  public ImageInterface flipHorizontal() {

    // Conditions for making a horizontal flip
    // 1. The image array is not empty
    // 2. The image array is not null
    // 3. The image array is not a single column
    if (this.imageArray.get(0).size() == 1) {
      // It would be the same image even when trying to make a flip
      return this;
    }

    List<List<PixelInterface>> flippedImageArray = new ArrayList<>();
    for (int i = 0; i < this.imageArray.size(); i++) {
      ArrayList<PixelInterface> row = new ArrayList<>();
      for (int j = this.imageArray.get(i).size() - 1; j >= 0; j--) {
        row.add(this.imageArray.get(i).get(j));
      }
      flippedImageArray.add(row);
    }
    return new ImageImpl(flippedImageArray);
  }

  @Override
  public ImageInterface flipVertical() {

    // Conditions for making a vertical flip
    // 1. The image array is not empty
    // 2. The image array is not null
    // 3. The image array is not a single row
    if (this.imageArray.size() == 1) {
      // It would be the same image even when trying to make a flip
      return this;
    }

    List<List<PixelInterface>> flippedImageArray = new ArrayList<>();
    for (int i = this.imageArray.size() - 1; i >= 0; i--) {
      flippedImageArray.add(this.imageArray.get(i));
    }
    return new ImageImpl(flippedImageArray);
  }

  @Override
  public ImageInterface brighten(int value) {
    int n = this.imageArray.size();
    int m = this.imageArray.get(0).size();

    List<List<PixelInterface>> newImageArray = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      List<PixelInterface> row = new ArrayList<>();
      for (int j = 0; j < m; j++) {
        int r = makeValueAcceptable(this.imageArray.get(i).get(j).getRed() + value);
        int g = makeValueAcceptable(this.imageArray.get(i).get(j).getGreen() + value);
        int b = makeValueAcceptable(this.imageArray.get(i).get(j).getBlue() + value);
        row.add(new RGBPixel(r, g, b));
      }
      newImageArray.add(row);
    }
    return new ImageImpl(newImageArray);
  }

  @Override
  public ImageInterface greyscale(String color) throws IllegalArgumentException {
    int n = this.imageArray.size();
    int m = this.imageArray.get(0).size();

    if (!functionMap.containsKey(color)) {
      throw new IllegalArgumentException("Illegal color component for RGB image + "
              + color + "\n");
    }

    List<List<PixelInterface>> greyImageArray = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      List<PixelInterface> row = new ArrayList<>();
      for (int j = 0; j < m; j++) {
        row.add(new RGBPixel(makeValueAcceptable(
                this.functionMap.get(color).apply(this.imageArray.get(i).get(j))
        )));
      }
      greyImageArray.add(row);
    }
    return new ImageImpl(greyImageArray);
  }

  @Override
  public ImageInterface greyscale(ColorTransform greyscale) {
    return applyColorTransform(greyscale);
  }

  @Override
  public List<ImageInterface> rgbSplit() {
    List<ImageInterface> imgSplit = new ArrayList<>();
    imgSplit.add(greyscale("red-component"));
    imgSplit.add(greyscale("green-component"));
    imgSplit.add(greyscale("blue-component"));
    return imgSplit;
  }

  @Override
  public List<List<PixelInterface>> getImageArray() {
    return this.imageArray;
  }

  @Override
  public List<Integer> getSize() {
    List<Integer> sizeArr = new ArrayList<>();
    sizeArr.add(this.imageArray.size());
    sizeArr.add(this.imageArray.get(0).size());
    return sizeArr;
  }

  @Override
  public ImageInterface blur(Kernel blur) {

    if (blur == null) {
      throw new IllegalArgumentException("Filter is null");
    }

    // Check if filter is valid
    // 1. Check if filter is square - Also validates that filter is not empty or null as well
    int filterSize = blur.filter.size();
    for (int i = 0; i < filterSize; i++) {
      if (blur.filter.get(i).size() != filterSize) {
        throw new IllegalArgumentException("Filter is not square");
      }
    }


    return this.applyFilter(blur);
  }

  @Override
  public ImageInterface sharpen(Kernel sharpen) {

    if (sharpen == null) {
      throw new IllegalArgumentException("Filter is null");
    }

    // Check if filter is valid
    // 1. Check if filter is square - Also validates that filter is not empty or null as well
    int filterSize = sharpen.filter.size();
    for (int i = 0; i < filterSize; i++) {
      if (sharpen.filter.get(i).size() != filterSize) {
        throw new IllegalArgumentException("Filter is not square");
      }
    }

    return this.applyFilter(sharpen);
  }

  @Override
  public ImageInterface sepia(ColorTransform sepia) {
    System.out.println("Doing sepia now"); // if you click on sepia this should get printed
    return applyColorTransform(sepia);
  }

  @Override
  public ImageInterface dither() {
    List<List<Double>> greyscaleTransform = new ArrayList<>();
    greyscaleTransform.add(Arrays.asList(0.2126, 0.7152, 0.0722));
    greyscaleTransform.add(Arrays.asList(0.2126, 0.7152, 0.0722));
    greyscaleTransform.add(Arrays.asList(0.2126, 0.7152, 0.0722));

    // Create a grayscale version of the current image
    ImageInterface grayscaleImage = this.greyscale(new ColorTransform(greyscaleTransform));

    List<List<PixelInterface>> greyScalePixelArray = grayscaleImage.getImageArray();

    // Create a new pixel array with the same dimensions as the current image
    List<List<PixelInterface>> pixelArray = new ArrayList<>();

    // Loop through each pixel in the image and clone it
    for (int i = 0; i < grayscaleImage.getImageArray().size(); i++) {
      List<PixelInterface> row = new ArrayList<>();
      for (int j = 0; j < grayscaleImage.getImageArray().get(0).size(); j++) {
        PixelInterface pixel = grayscaleImage.getImageArray().get(i).get(j);
        row.add(new RGBPixel(pixel.getRed(), pixel.getGreen(), pixel.getBlue()));
      }
      pixelArray.add(row);
    }

    // Make Error matrix
    List<List<Double>> errorMatrix = new ArrayList<>();
    for (int i = 0; i < grayscaleImage.getImageArray().size(); i++) {
      List<Double> row = new ArrayList<>();
      for (int j = 0; j < grayscaleImage.getImageArray().get(0).size(); j++) {
        row.add(0.0);
      }
      errorMatrix.add(row);
    }

    for (int i = 0; i < grayscaleImage.getImageArray().size(); i++) {
      for (int j = 0; j < grayscaleImage.getImageArray().get(0).size(); j++) {

        // Get value of pixel at (i, j)
        int pixelValue = greyScalePixelArray.get(i).get(j).getRed();
        int newPixelValue = 0;

        // Set pixel at (i, j) to black or white depending on value
        if (pixelValue < 128) {
          newPixelValue = 0;
        } else {
          newPixelValue = 255;
        }

        double error = pixelValue - newPixelValue;

        PixelInterface newPixel = new RGBPixel(newPixelValue, newPixelValue, newPixelValue);
        pixelArray.get(i).set(j, newPixel);

        // Update the error matrix
        errorMatrix.get(i).set(j, error);
      }
    }

    // Perform the error diffusion algorithm
    for (int i = 0; i < pixelArray.size(); i++) {
      for (int j = 0; j < pixelArray.get(0).size(); j++) {
        // Skip first column, first row, last column and last row
        if (i == 0 || j == 0 || i == pixelArray.size() - 1 || j == pixelArray.get(0).size() - 1) {
          continue;
        }

        // Propagate error to neighboring pixels

        // add (7/16 * error) to pixel on the right (i, j + 1)
        pixelArray.get(i).set(j + 1,
                new RGBPixel(
                        makeValueAcceptable(
                                (int) ((0.4375) * (errorMatrix.get(i).get(j))
                                        + (double) pixelArray.get(i).get(j + 1).getRed())
                        )
                )
        );

        // add (3/16 * error) to pixel on the next-row-left (i+1,j-1)
        pixelArray.get(i + 1).set(j - 1,
                new RGBPixel(
                        makeValueAcceptable(
                                (int) ((0.1875) * (errorMatrix.get(i).get(j))
                                        + (double) pixelArray.get(i + 1).get(j - 1).getRed())
                        )
                )
        );

        // add (5/16 * error) to pixel below in next row (i+1,j)
        pixelArray.get(i + 1).set(j,
                new RGBPixel(
                        makeValueAcceptable(
                                (int) ((0.3125) * (errorMatrix.get(i).get(j))
                                        + (double) pixelArray.get(i + 1).get(j).getRed())
                        )
                )
        );

        // add (1/16 * error) to pixel on the next-row-right (i+1,j+1)
        pixelArray.get(i + 1).set(j + 1,
                new RGBPixel(
                        makeValueAcceptable(
                                (int) ((0.0625) * (errorMatrix.get(i).get(j))
                                        + (double) pixelArray.get(i + 1).get(j + 1).getRed())
                        )
                )
        );
      }
    }

    return new ImageImpl(pixelArray);
  }

  @Override
  public ImageInterface mosaic(int num_of_seeds) {
    List<List<PixelInterface>> mosaicImage;

    int height = this.imageArray.size();
    int width = this.imageArray.get(0).size();

    List<int[]> seeds = generateRandomSeeds(num_of_seeds, width, height);
    Map<int[], List<PixelInterface>> clusters = createClusters(seeds, width, height);
    Map<int[], PixelInterface> clusterColors = computeClusterColors(clusters);
    mosaicImage = createMosaicImage(width, height, seeds, clusterColors);

    return new ImageImpl(mosaicImage);
  }

  private Map<int[], List<PixelInterface>> createClusters(List<int[]> seeds, int width,
                                                          int height) {
    Map<int[], List<PixelInterface>> clusters = new HashMap<>();
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        int[] pixel = { x, y };
        int[] closestSeed = findClosestSeed(pixel, seeds);
        PixelInterface color = getValidPixel(y, x);
        List<PixelInterface> cluster = clusters.getOrDefault(closestSeed, new ArrayList<>());
        cluster.add(color);
        clusters.put(closestSeed, cluster);
      }
    }
    return clusters;
  }

  private Map<int[], PixelInterface> computeClusterColors(Map<int[],
          List<PixelInterface>> clusters) {
    Map<int[], PixelInterface> clusterColors = new HashMap<>();
    for (Map.Entry<int[], List<PixelInterface>> entry : clusters.entrySet()) {
      int[] seed = entry.getKey();
      List<PixelInterface> colors = entry.getValue();
      PixelInterface averageColor = computeAverageColor(colors);
      clusterColors.put(seed, averageColor);
    }
    return clusterColors;
  }

  private List<List<PixelInterface>> createMosaicImage(int width, int height, List<int[]> seeds,
                                                       Map<int[], PixelInterface> clusterColors) {
    List<List<PixelInterface>> mosaicImage = new ArrayList<>();
    for (int x = 0; x < height; x++) {
      List<PixelInterface> row = new ArrayList<>();
      for (int y = 0; y < width; y++) {
        int[] pixel = { y, x };
        int[] closestSeed = findClosestSeed(pixel, seeds);
        PixelInterface color = clusterColors.get(closestSeed);
        row.add(color);
      }
      mosaicImage.add(row);
    }
    return mosaicImage;
  }


  private List<int[]> generateRandomSeeds(int numSeeds, int width, int height) {
    List<int[]> seeds = new ArrayList<>();
    Random random = new Random(200);
    for (int i = 0; i < numSeeds; i++) {
      int x = random.nextInt(width);
      int y = random.nextInt(height);
      seeds.add(new int[]{x, y});
    }
    return seeds;
  }

  private int[] findClosestSeed(int[] pixel, List<int[]> seeds) {
    double minDistance = Double.MAX_VALUE;
    int[] closestSeed = null;
    for (int[] seed : seeds) {
      double distance = Math.sqrt(Math.pow(seed[0] - pixel[0], 2)
              + Math.pow(seed[1] - pixel[1], 2));
      if (distance < minDistance) {
        minDistance = distance;
        closestSeed = seed;
      }
    }
    return closestSeed;
  }

  private PixelInterface computeAverageColor(List<PixelInterface> colors) {
    int r = 0, g = 0, b = 0;
    for (PixelInterface color : colors) {
      r += color.getRed();
      g += color.getGreen();
      b += color.getBlue();
    }
    int numColors = colors.size();
    int avgR = r / numColors;
    int avgG = g / numColors;
    int avgB = b / numColors;
    return new RGBPixel(avgR, avgG, avgB);
  }

  private ImageInterface applyFilter(Kernel kernel) throws IllegalArgumentException {
    List<List<Double>> filter = kernel.filter;

    List<List<PixelInterface>> newP = new ArrayList<>();

    for (int i = 0; i < imageArray.size(); i++) {
      List<PixelInterface> row = new ArrayList<>();
      for (int j = 0; j < imageArray.get(0).size(); j++) {
        row.add(filterPixel(i, j, filter));
      }
      newP.add(row);
    }
    return new ImageImpl(newP);
  }

  private ImageInterface applyColorTransform(ColorTransform cT) {
    List<List<PixelInterface>> transformedImage = new ArrayList<>();
    for (int i = 0; i < imageArray.size(); i++) {
      List<PixelInterface> row = new ArrayList<>();
      for (int j = 0; j < imageArray.get(0).size(); j++) {
        row.add(transformPixel(cT.transform, i, j));
      }
      transformedImage.add(row);
    }
    return new ImageImpl(transformedImage);
  }

  private PixelInterface transformPixel(List<List<Double>> coeffs, int i, int j) {
    int red = imageArray.get(i).get(j).getRed();
    int green = imageArray.get(i).get(j).getGreen();
    int blue = imageArray.get(i).get(j).getBlue();


    int r = makeValueAcceptable((int)
            Math.round(
                    coeffs.get(0).get(0) * red
                            + coeffs.get(0).get(1) * green
                            + coeffs.get(0).get(2) * blue
            )
    );
    int g = makeValueAcceptable((int)
            Math.round(
                    coeffs.get(1).get(0) * red
                            + coeffs.get(1).get(1) * green
                            + coeffs.get(1).get(2) * blue
            )
    );
    int b = makeValueAcceptable((int)
            Math.round(coeffs.get(2).get(0) * red
                    + coeffs.get(2).get(1) * green
                    + coeffs.get(2).get(2) * blue
            )
    );
    return new RGBPixel(r, g, b);
  }

  private PixelInterface filterPixel(int i, int j, List<List<Double>> filter) {
    int cVal = (filter.size() / 2);

    double rSum = 0;
    double gSum = 0;
    double bSum = 0;

    for (int fi = 0; fi < filter.size(); fi++) {
      int sR = i - cVal + fi;
      for (int fj = 0; fj < filter.size(); fj++) {
        int sC = j - cVal + fj;
        PixelInterface validPixel = getValidPixel(sR, sC);
        rSum += filter.get(fi).get(fj) * validPixel.getRed();
        gSum += filter.get(fi).get(fj) * validPixel.getGreen();
        bSum += filter.get(fi).get(fj) * validPixel.getBlue();
      }
    }
    int r = makeValueAcceptable((int) Math.round(rSum));
    int g = makeValueAcceptable((int) Math.round(gSum));
    int b = makeValueAcceptable((int) Math.round(bSum));
    return new RGBPixel(r, g, b);
  }

  private PixelInterface getValidPixel(int i, int j) {
    if (i < 0 || j < 0 || i >= imageArray.size() || j >= imageArray.get(0).size()) {
      return new RGBPixel(0);
    }
    return imageArray.get(i).get(j);
  }

  private int makeValueAcceptable(int x) {
    if (x < 0) {
      x = 0;
    }
    if (x > this.maxColorValue) {
      x = this.maxColorValue;
    }
    return x;
  }
}
