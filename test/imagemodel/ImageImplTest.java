package imagemodel;

import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import helpers.ImageImplHelper;
import helpers.ImageUtil;
import helpers.loaders.BMPLoader;
import helpers.loaders.PNGLoader;
import imagecontroller.commands.Blur;
import imagecontroller.commands.Sharpen;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Test class for ImageImpl.
 */
public class ImageImplTest {
  ImageImplHelper imgHelper = new ImageImplHelper();
  ImageInterface img;
  ImageInterface img100;

  @Before
  public void setUp() {
    img = imgHelper.loadImagePPM("res/SMPTE/SMPTE-original.ppm");
    img100 = createSolidImage(10, 20, 100);
  }

  @Test
  public void basicFrameWorkTest() {
    int a = 1;
    int b = 2;
    assertEquals(3, a + b);
  }

  @Test
  public void brightenSimpleTest() {
    ImageInterface img50 = ImageUtil.readPPM("res/SMPTE/SMPTE-brighter-by-50.ppm");

    ImageInterface testResult = img.brighten(50);

    assertEquals(true, compareImages(testResult, img50));
  }

  @Test
  public void brightenSimpleTest2() {

    ImageInterface img00 = createSolidImage(10, 20, 255);

    assertEquals(true, compareImages(img00, img100.brighten(250)));
  }

  @Test
  public void darkenSimpleTest() {
    ImageInterface img75 = createSolidImage(10, 20, 75);

    assertEquals(true, compareImages(img75, img100.brighten(-25)));
  }

  @Test
  public void darkenSimpleTest2() {
    ImageInterface img00 = createSolidImage(10, 20, 0);

    assertEquals(true, compareImages(img00, img100.brighten(-125)));
  }

  @Test
  public void darkenFailure() {
    ImageInterface img00 = createSolidImage(10, 20, 0);
    assertEquals(false, compareImages(img00, img100.brighten(-50)));
  }

  @Test
  public void greyRedSimpleTest() {
    ImageInterface redGImg = ImageUtil.readPPM("res/SMPTE/SMPTE-red-greyscale.ppm");
    ImageInterface testResult = img.greyscale("red-component");
    assertEquals(true, compareImages(testResult, redGImg));
  }

  @Test
  public void greyGreenSimpleTest() {
    ImageInterface greenGImg = ImageUtil.readPPM("res/SMPTE/SMPTE-green-greyscale.ppm");
    ImageInterface testResult = img.greyscale("green-component");
    assertEquals(true, compareImages(testResult, greenGImg));
  }

  @Test
  public void greyBlueSimpleTest() {
    ImageInterface blueGImg = ImageUtil.readPPM("res/SMPTE/SMPTE-blue-greyscale.ppm");
    ImageInterface testResult = img.greyscale("blue-component");
    assertEquals(true, compareImages(testResult, blueGImg));
  }

  @Test(expected = IllegalArgumentException.class)
  public void greyNoneSimpleTest() {
    ImageInterface blueGImg = ImageUtil.readPPM("res/SMPTE/SMPTE-blue-greyscale.ppm");
    ImageInterface testResult = img.greyscale("color-component");
    assertEquals(true, compareImages(testResult, blueGImg));
  }

  @Test
  public void greySimpleTestFail1() {
    ImageInterface blueGImg = ImageUtil.readPPM("res/SMPTE/SMPTE-blue-greyscale.ppm");
    ImageInterface testResult = img.greyscale("green-component");
    assertEquals(false, compareImages(testResult, blueGImg));
  }

  @Test
  public void greySimpleTestFail2() {
    ImageInterface blueGImg = ImageUtil.readPPM("res/SMPTE/SMPTE-blue-greyscale.ppm");
    ImageInterface testResult = img.greyscale("red-component");
    assertEquals(false, compareImages(testResult, blueGImg));
  }

  @Test
  public void rgbSplitTest() {
    ImageInterface redGImg = ImageUtil.readPPM("res/SMPTE/SMPTE-red-greyscale.ppm");
    ImageInterface greenGImg = ImageUtil.readPPM("res/SMPTE/SMPTE-green-greyscale.ppm");
    ImageInterface blueGImg = ImageUtil.readPPM("res/SMPTE/SMPTE-blue-greyscale.ppm");

    List<ImageInterface> result = img.rgbSplit();
    assertEquals(3, result.size());
    assertEquals(true, compareImages(result.get(0), redGImg));
    assertEquals(true, compareImages(result.get(1), greenGImg));
    assertEquals(true, compareImages(result.get(2), blueGImg));
  }

  @Test
  public void rgbCombineTest() {
    ImageInterface redGImg = ImageUtil.readPPM("res/SMPTE/SMPTE-red-greyscale.ppm");
    ImageInterface greenGImg = ImageUtil.readPPM("res/SMPTE/SMPTE-green-greyscale.ppm");
    ImageInterface blueGImg = ImageUtil.readPPM("res/SMPTE/SMPTE-blue-greyscale.ppm");
    ArrayList<ImageInterface> rgb = new ArrayList<>();
    rgb.add(redGImg);
    rgb.add(greenGImg);
    rgb.add(blueGImg);

    ImageInterface testResult = imgHelper.rgbCombine(rgb);
    assertEquals(true, compareImages(testResult, img));
  }

  @Test
  public void rgbCombineTestFailure() {
    ImageInterface redGImg = ImageUtil.readPPM("res/SMPTE/SMPTE-red-greyscale.ppm");
    ImageInterface greenGImg = ImageUtil.readPPM("res/SMPTE/SMPTE-green-greyscale.ppm");
    ImageInterface blueGImg = ImageUtil.readPPM("res/SMPTE/SMPTE-blue-greyscale.ppm");
    ArrayList<ImageInterface> rgb = new ArrayList<>();
    rgb.add(greenGImg);
    rgb.add(blueGImg);
    rgb.add(redGImg);

    ImageInterface testResult = imgHelper.rgbCombine(rgb);
    assertEquals(false, compareImages(testResult, img));
  }

  @Test(expected = IllegalArgumentException.class)
  public void rgbCombineTestNotEnoughColor() {
    ImageInterface redGImg = ImageUtil.readPPM("res/SMPTE/SMPTE-red-greyscale.ppm");
    ImageInterface greenGImg = ImageUtil.readPPM("res/SMPTE/SMPTE-green-greyscale.ppm");
    ArrayList<ImageInterface> rgb = new ArrayList<>();
    rgb.add(redGImg);
    rgb.add(greenGImg);

    ImageInterface testResult = imgHelper.rgbCombine(rgb);
    assertEquals(false, compareImages(testResult, img));
  }

  @Test(expected = IllegalArgumentException.class)
  public void rgbCombineTestNotGreyscale() {
    ImageInterface redGImg = ImageUtil.readPPM("res/SMPTE/SMPTE-red-greyscale.ppm");
    ImageInterface greenGImg = ImageUtil.readPPM("res/SMPTE/SMPTE-green-greyscale.ppm");
    ImageInterface blueImg = ImageUtil.readPPM("res/SMPTE/SMPTE-original.ppm");
    ArrayList<ImageInterface> rgb = new ArrayList<>();
    rgb.add(redGImg);
    rgb.add(greenGImg);
    rgb.add(blueImg);

    ImageInterface testResult = imgHelper.rgbCombine(rgb);
  }

  @Test
  public void verticalFlipTest() {
    ImageInterface testResult = img.flipVertical();

    assertEquals(true,
            compareImages(
                    testResult,
                    ImageUtil.readPPM("res/SMPTE/SMPTE-vertical.ppm")
            )
    );
  }

  @Test
  public void doubleFlipPass() {
    ImageInterface testResult = img.flipVertical();
    ImageInterface testResult2 = testResult.flipVertical();
    assertTrue(compareImages(testResult2, img));
  }

  @Test
  public void onePixelImageFlip() {
    ImageInterface testPixelImage = createSolidImage(1, 1, 0);
    ImageInterface testResult2 = testPixelImage.flipVertical();
    assertTrue(testResult2 == testPixelImage);
  }

  @Test
  public void horizontalFlipTest() {
    ImageInterface testResult = img.flipHorizontal();

    assertEquals(true,
            compareImages(
                    testResult,
                    ImageUtil.readPPM("res/SMPTE/SMPTE-horizontal.ppm")
            )
    );
  }

  @Test
  public void doubleFlipHorizontalPass() {
    ImageInterface testResult = img.flipHorizontal();
    ImageInterface testResult2 = testResult.flipHorizontal();
    assertTrue(compareImages(testResult2, img));
  }

  @Test
  public void onePixelImageFlipHorizontal() {
    ImageInterface testPixelImage = createSolidImage(1, 1, 0);
    ImageInterface testResult2 = testPixelImage.flipHorizontal();
    assertTrue(testResult2 == testPixelImage);
  }

  @Test
  public void bothAxisFlipTest() {
    ImageInterface testResult = img.flipVertical().flipHorizontal();

    assertEquals(true,
            compareImages(
                    testResult,
                    ImageUtil.readPPM("res/SMPTE/SMPTE-vertical-horizontal.ppm")
            )
    );
  }

  @Test
  public void bothAxisFlipTest2() {
    ImageInterface testResult = img.flipHorizontal().flipVertical();

    assertEquals(true,
            compareImages(
                    testResult,
                    ImageUtil.readPPM("res/SMPTE/SMPTE-vertical-horizontal.ppm")
            )
    );
  }

  @Test
  public void bothAxisFlipCommutative() {
    ImageInterface testResult = img.flipVertical().flipHorizontal();
    ImageInterface testResult2 = img.flipHorizontal().flipVertical();
    assertTrue(compareImages(testResult, testResult2));
  }

  @Test
  public void doubleFlipBothAxisPass() {
    ImageInterface testResult = img.flipVertical().flipHorizontal();
    ImageInterface testResult2 = testResult.flipVertical().flipHorizontal();
    assertTrue(compareImages(testResult2, img));
  }

  @Test
  public void testBlur() {
    ImageInterface imgBlur = img.blur(new Blur().blur);
    assertEquals(imgBlur.getSize(), img.getSize());

    ImageInterface imgBlur2 = imgHelper.loadImage("res/SMPTE/SMPTE-blurred.ppm");

    assertTrue(imgBlur != null);
    assertTrue(imgBlur2 != null);
    assertTrue(compareImages(imgBlur, imgBlur2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBlurFailNullKernel() {
    ImageInterface imgBlur = img.blur(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBlurFailEmptyKernel() {
    ImageInterface imgBlur = img.blur(new Kernel(List.of()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBlurFailKernelNonRectangle() {
    ImageInterface imgBlur = img.blur(new Kernel(
          List.of(
                List.of(1.0, 2.0),
                List.of(1.0),
                List.of(3.0, 4.0)
          ))
    );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testBlurFailKernelNonSquareSize() {
    ImageInterface imgBlur = img.blur(new Kernel(
          List.of(
                List.of(1.0, 2.0),
                List.of(1.0, 3.0),
                List.of(3.0, 4.0)
          ))
    );
  }

  @Test
  public void testSharpen() {
    ImageInterface imgSharpen = img.sharpen(new Sharpen().sharpen);
    assertEquals(imgSharpen.getSize(), img.getSize());

    ImageInterface imgSharpen2 = imgHelper.loadImage("res/SMPTE/SMPTE-sharpened.ppm");

    assertTrue(imgSharpen != null);
    assertTrue(imgSharpen2 != null);
    assertTrue(compareImages(imgSharpen, imgSharpen2));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSharpenFailNullKernel() {
    ImageInterface imgSharpen = img.sharpen(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSharpenFailEmptyKernel() {
    ImageInterface imgSharpen = img.sharpen(new Kernel(List.of()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSharpenFailKernelNonRectangle() {
    ImageInterface imgSharpen = img.sharpen(new Kernel(
          List.of(
                List.of(1.0, 2.0),
                List.of(1.0),
                List.of(3.0, 4.0)
          ))
    );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSharpenFailKernelNonSquareSize() {
    ImageInterface imgSharpen = img.sharpen(new Kernel(
          List.of(
                List.of(1.0, 2.0),
                List.of(1.0, 3.0),
                List.of(3.0, 4.0)
          ))
    );
  }

  @Test
  public void ditherTest() {

    // Dither needs a different image to work on
    // The default does not show the dithering effect
    ImageInterface img = ImageUtil.readPPM("res/SMPTE/snow.ppm");

    ImageInterface imgDither = img.dither();
    assertEquals(imgDither.getSize(), img.getSize());

    ImageInterface imgDither2 = imgHelper.loadImage("res/SMPTE/snow-dithered.ppm");

    assertTrue(imgDither != null);
    assertTrue(imgDither2 != null);
    assertTrue(compareImages(imgDither, imgDither2));
  }

  @Test
  public void mosaicTest() {

    ImageInterface img = ImageUtil.readPPM("res/SMPTE/snow.ppm");

    ImageInterface imgMosaic = img.mosaic(1000);
    assertEquals(imgMosaic.getSize(), img.getSize());

    ImageInterface imgMosaic2 = imgHelper.loadImage("res/SMPTE/snow-mosaic.ppm");

    assertTrue(imgMosaic != null);
    assertTrue(imgMosaic2 != null);
    assertTrue(compareImages(imgMosaic, imgMosaic2));
  }

  @Test
  public void mosaicTestPPMToBMP() {

    ImageInterface img = ImageUtil.readPPM("res/SMPTE/snow.ppm");

    ImageInterface imgMosaic = img.mosaic(1000);
    assertEquals(imgMosaic.getSize(), img.getSize());

    ImageInterface imgMosaic2 = imgHelper.loadImage("res/SMPTE/snow-mosaic.bmp");

    assertTrue(imgMosaic != null);
    assertTrue(imgMosaic2 != null);
    assertTrue(compareImages(imgMosaic, imgMosaic2));
  }

  @Test
  public void mosaicTestPPMToPNG() {

    ImageInterface img = ImageUtil.readPPM("res/SMPTE/snow.ppm");

    ImageInterface imgMosaic = img.mosaic(1000);
    assertEquals(imgMosaic.getSize(), img.getSize());

    ImageInterface imgMosaic2 = imgHelper.loadImage("res/SMPTE/snow-mosaic.png");

    assertTrue(imgMosaic != null);
    assertTrue(imgMosaic2 != null);
    assertTrue(compareImages(imgMosaic, imgMosaic2));
  }

  @Test
  public void mosaicTestPPMToJPG() {

    ImageInterface img = ImageUtil.readPPM("res/SMPTE/snow.ppm");

    ImageInterface imgMosaic = img.mosaic(1500);
    assertEquals(imgMosaic.getSize(), img.getSize());

    ImageInterface imgMosaic2 = imgHelper.loadImage("res/SMPTE/snow-mosaic.jpeg");

    assertTrue(imgMosaic != null);
    assertTrue(imgMosaic2 != null);
    assertFalse(compareImages(imgMosaic, imgMosaic2));
  }

  @Test
  public void mosaicTestPNGToBMP() throws IOException {

    FileInputStream stream = new FileInputStream("res/SMPTE/snow.png");
    ImageInterface img = new PNGLoader().loadImage(stream);

    ImageInterface imgMosaic = img.mosaic(1000);
    assertEquals(imgMosaic.getSize(), img.getSize());

    ImageInterface imgMosaic2 = imgHelper.loadImage("res/SMPTE/snow-mosaic.bmp");

    assertTrue(imgMosaic != null);
    assertTrue(imgMosaic2 != null);
    assertTrue(compareImages(imgMosaic, imgMosaic2));
  }

  @Test
  public void mosaicTestPNGToPPM() throws IOException {

    FileInputStream stream = new FileInputStream("res/SMPTE/snow.png");
    ImageInterface img = new PNGLoader().loadImage(stream);

    ImageInterface imgMosaic = img.mosaic(1000);
    assertEquals(imgMosaic.getSize(), img.getSize());

    ImageInterface imgMosaic2 = imgHelper.loadImage("res/SMPTE/snow-mosaic.ppm");

    assertTrue(imgMosaic != null);
    assertTrue(imgMosaic2 != null);
    assertTrue(compareImages(imgMosaic, imgMosaic2));
  }

  @Test
  public void mosaicTestPNGToJPG() throws IOException {

    FileInputStream stream = new FileInputStream("res/SMPTE/snow.png");
    ImageInterface img = new PNGLoader().loadImage(stream);

    ImageInterface imgMosaic = img.mosaic(1000);
    assertEquals(imgMosaic.getSize(), img.getSize());

    ImageInterface imgMosaic2 = imgHelper.loadImage("res/SMPTE/snow-mosaic.jpeg");

    assertTrue(imgMosaic != null);
    assertTrue(imgMosaic2 != null);
    assertFalse(compareImages(imgMosaic, imgMosaic2));
  }

  @Test
  public void mosaicTestBMPToPPM() throws IOException {

    FileInputStream stream = new FileInputStream("res/SMPTE/snow.bmp");
    ImageInterface img = new BMPLoader().loadImage(stream);

    ImageInterface imgMosaic = img.mosaic(1000);
    assertEquals(imgMosaic.getSize(), img.getSize());

    ImageInterface imgMosaic2 = imgHelper.loadImage("res/SMPTE/snow-mosaic.ppm");

    assertTrue(imgMosaic != null);
    assertTrue(imgMosaic2 != null);
    assertTrue(compareImages(imgMosaic, imgMosaic2));
  }

  @Test
  public void mosaicTestBMPToPNG() throws IOException {

    FileInputStream stream = new FileInputStream("res/SMPTE/snow.bmp");
    ImageInterface img = new BMPLoader().loadImage(stream);

    ImageInterface imgMosaic = img.mosaic(1000);
    assertEquals(imgMosaic.getSize(), img.getSize());

    ImageInterface imgMosaic2 = imgHelper.loadImage("res/SMPTE/snow-mosaic.png");

    assertTrue(imgMosaic != null);
    assertTrue(imgMosaic2 != null);
    assertTrue(compareImages(imgMosaic, imgMosaic2));
  }

  @Test
  public void mosaicTestBMPToJPG() throws IOException {

    FileInputStream stream = new FileInputStream("res/SMPTE/snow.bmp");
    ImageInterface img = new BMPLoader().loadImage(stream);

    ImageInterface imgMosaic = img.mosaic(1000);
    assertEquals(imgMosaic.getSize(), img.getSize());

    ImageInterface imgMosaic2 = imgHelper.loadImage("res/SMPTE/snow-mosaic.jpeg");

    assertTrue(imgMosaic != null);
    assertTrue(imgMosaic2 != null);
    assertFalse(compareImages(imgMosaic, imgMosaic2));
  }

  @Test
  public void mosaicTestJPEGToPPM() throws IOException {

    FileInputStream stream = new FileInputStream("res/SMPTE/snow.jpeg");
    ImageInterface img = new BMPLoader().loadImage(stream);

    ImageInterface imgMosaic = img.mosaic(1000);
    assertEquals(imgMosaic.getSize(), img.getSize());

    ImageInterface imgMosaic2 = imgHelper.loadImage("res/SMPTE/snow-mosaic.ppm");

    assertTrue(imgMosaic != null);
    assertTrue(imgMosaic2 != null);
    assertFalse(compareImages(imgMosaic, imgMosaic2));
  }

  @Test
  public void mosaicTestJPEGToPNG() throws IOException {

    FileInputStream stream = new FileInputStream("res/SMPTE/snow.jpeg");
    ImageInterface img = new BMPLoader().loadImage(stream);

    ImageInterface imgMosaic = img.mosaic(1000);
    assertEquals(imgMosaic.getSize(), img.getSize());

    ImageInterface imgMosaic2 = imgHelper.loadImage("res/SMPTE/snow-mosaic.png");

    assertTrue(imgMosaic != null);
    assertTrue(imgMosaic2 != null);
    assertFalse(compareImages(imgMosaic, imgMosaic2));
  }

  /**
   * Helper method for testing file operations.
   */
  private boolean compareFiles(String file1, String file2) {
    try {
      BufferedReader br1 = new BufferedReader(new FileReader(file1));
      BufferedReader br2 = new BufferedReader(new FileReader(file2));
      String line1 = br1.readLine();
      String line2 = br2.readLine();
      while (line1 != null || line2 != null) {
        if (line1 == null || line2 == null) {
          return false;
        }
        if (!line1.equals(line2)) {
          return false;
        }
        line1 = br1.readLine();
        line2 = br2.readLine();
      }
      br1.close();
      br2.close();
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return true;
  }

  /**
   * Helper method for comparing two images. Returns true if the images are the same.
   *
   * @param img1 ImageInterface object a
   * @param img2 ImageInterface object b
   * @return true if the images are the same, false otherwise
   */
  public static boolean compareImages(ImageInterface img1, ImageInterface img2) {
    List<List<PixelInterface>> img1Arr = img1.getImageArray();
    List<List<PixelInterface>> img2Arr = img2.getImageArray();

    if (img1Arr.size() != img2Arr.size() || img1Arr.get(0).size() != img2Arr.get(0).size()) {
      System.out.println("Image size mismatch" + img1Arr.size() + " " + img2Arr.size());
      return false;
    }

    int n = img1Arr.size();
    int m = img1Arr.get(0).size();
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        int r1 = img1Arr.get(i).get(j).getRed();
        int g1 = img1Arr.get(i).get(j).getGreen();
        int b1 = img1Arr.get(i).get(j).getBlue();
        if (
                r1 != img2Arr.get(i).get(j).getRed()
                        || g1 != img2Arr.get(i).get(j).getGreen()
                        || b1 != img2Arr.get(i).get(j).getBlue()
        ) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * Helper method for comparing two images. Returns true if the images are within an acceptable
   * threshold of each other. Needed when working with lossy formats like JPEG.
   * @param img1 ImageInterface object a
   * @param img2 ImageInterface object b
   * @param threshold threshold for pixel difference
   * @return the boolean value if the two images are same or not.
   */
  public static boolean compareImages(ImageInterface img1, ImageInterface img2, double threshold) {
    List<List<PixelInterface>> img1Arr = img1.getImageArray();
    List<List<PixelInterface>> img2Arr = img2.getImageArray();

    if (img1Arr.size() != img2Arr.size() || img1Arr.get(0).size() != img2Arr.get(0).size()) {
      System.out.println("Image size mismatch" + img1Arr.size() + " " + img2Arr.size());
      return false;
    }

    int diffPixels = 0;

    int n = img1Arr.size();
    int m = img1Arr.get(0).size();
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < m; j++) {
        int r1 = img1Arr.get(i).get(j).getRed();
        int g1 = img1Arr.get(i).get(j).getGreen();
        int b1 = img1Arr.get(i).get(j).getBlue();
        if (
              (Math.abs(r1 - img2Arr.get(i).get(j).getRed()) > threshold)
                    || (Math.abs(g1 - img2Arr.get(i).get(j).getGreen()) > threshold)
                    || (Math.abs(b1 - img2Arr.get(i).get(j).getBlue()) > threshold)
        ) {
          diffPixels++;
        }
      }
    }

    return diffPixels < (0.1 * n * m);
  }

  private ImageInterface createSolidImage(int width, int height, int value) {
    List<PixelInterface> solidArray = new ArrayList<>();
    for (int i = 0; i < height * width; i++) {
      PixelInterface rgb = new RGBPixel(value, value, value);
      solidArray.add(rgb);
    }
    return new ImageImpl(solidArray, width, height, 255);
  }
}