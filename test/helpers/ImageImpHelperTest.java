package helpers;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import imagemodel.ImageImpl;
import imagemodel.ImageInterface;
import imagemodel.PixelInterface;
import imagemodel.RGBPixel;

import static imagemodel.ImageImplTest.compareImages;
import static org.junit.Assert.assertTrue;

/**
 * Test class for ImageImpHelper.
 */
public class ImageImpHelperTest {

  @Test
  public void testLoadImagePPM() {
    ImageImplHelper imgHelper = new ImageImplHelper();

    ImageInterface img = imgHelper.loadImagePPM(
            "res/SMPTE/SMPTE-original.ppm"
    );

    assertTrue(img != null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoadImagePPMNull() {
    ImageImplHelper imgHelper = new ImageImplHelper();

    ImageInterface img = imgHelper.loadImagePPM(
          (String) null
    );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoadImagePPMEmpty() {
    ImageImplHelper imgHelper = new ImageImplHelper();

    ImageInterface img = imgHelper.loadImagePPM(
            ""
    );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoadImagePPMInvalid() {
    ImageImplHelper imgHelper = new ImageImplHelper();

    ImageInterface img = imgHelper.loadImagePPM(
            "test/resources/flower.txt"
    );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoadImagePPMEmptyFile() {
    ImageImplHelper imgHelper = new ImageImplHelper();

    ImageInterface img = imgHelper.loadImagePPM(
            "res/helperRes/empty.ppm"
    );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoadImagePPMInvalidFile() {
    ImageImplHelper imgHelper = new ImageImplHelper();

    ImageInterface img = imgHelper.loadImagePPM(
            "res/helperRes/empty.png"
    );
  }

  @Test
  public void testLoadJPEG() {
    ImageImplHelper imgHelper = new ImageImplHelper();

    ImageInterface img = imgHelper.loadImage(
            "res/SMPTE/SMPTE-original.jpeg"
    );

    assertTrue(img != null);
  }

  @Test
  public void testLoadPNG() {
    ImageImplHelper imgHelper = new ImageImplHelper();

    ImageInterface img = imgHelper.loadImage(
            "res/SMPTE/SMPTE-original.png"
    );

    assertTrue(img != null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testLoadFailNoExtension() {
    ImageImplHelper imgHelper = new ImageImplHelper();

    ImageInterface img = imgHelper.loadImage(
            "res/SMPTE/SMPTE-original"
    );

    assertTrue(img == null);
  }

  @Test
  public void testRGBCombine() {
    ImageImplHelper imgHelper = new ImageImplHelper();

    ImageInterface blue = imgHelper.loadImagePPM(
            "res/SMPTE/SMPTE-blue-greyscale.ppm"
    );

    ImageInterface red = imgHelper.loadImagePPM(
            "res/SMPTE/SMPTE-red-greyscale.ppm"
    );

    ImageInterface green = imgHelper.loadImagePPM(
            "res/SMPTE/SMPTE-green-greyscale.ppm"
    );

    List<ImageInterface> rgb = new ArrayList<>();
    rgb.add(red);
    rgb.add(green);
    rgb.add(blue);

    ImageInterface img = imgHelper.rgbCombine(rgb);

    assertTrue(img != null);
    assertTrue(img.getSize().equals(red.getSize()));

    // img.saveImage("res/SMPTE/SMPTE-combined.ppm");
    assertTrue(compareImages(
            img,
            imgHelper.loadImagePPM("res/SMPTE/SMPTE-original.ppm")
    ));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRGBCombineInvalidArraySize() {
    ImageImplHelper imgHelper = new ImageImplHelper();

    ImageInterface blue = imgHelper.loadImagePPM(
            "res/SMPTE/SMPTE-blue-greyscale.ppm"
    );

    ImageInterface red = imgHelper.loadImagePPM(
            "res/SMPTE/SMPTE-red-greyscale.ppm"
    );

    ImageInterface green = imgHelper.loadImagePPM(
            "res/SMPTE/SMPTE-green-greyscale.ppm"
    );

    List<ImageInterface> rgb = new ArrayList<>();
    rgb.add(red);
    rgb.add(green);

    ImageInterface img = imgHelper.rgbCombine(rgb);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRGBCombineEmptyInputArray() {
    ImageImplHelper imgHelper = new ImageImplHelper();

    ImageInterface img = imgHelper.rgbCombine(
            new ArrayList<ImageInterface>()
    );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRGBCombineNullInputArray() {
    ImageImplHelper imgHelper = new ImageImplHelper();

    ImageInterface img = imgHelper.rgbCombine(
            null
    );
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRGBCombineNullImage() {
    ImageImplHelper imgHelper = new ImageImplHelper();

    ImageInterface blue = imgHelper.loadImagePPM(
            "res/SMPTE/SMPTE-blue-greyscale.ppm"
    );

    ImageInterface red = imgHelper.loadImagePPM(
            "res/SMPTE/SMPTE-red-greyscale.ppm"
    );

    ImageInterface green = imgHelper.loadImagePPM(
            "res/SMPTE/SMPTE-green-greyscale.ppm"
    );
    List<ImageInterface> rgb = new ArrayList<>();
    rgb.add(red);
    rgb.add(green);
    rgb.add(null);

    ImageInterface img = imgHelper.rgbCombine(rgb);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRGBCombineInvalidImageSize() {
    ImageImplHelper imgHelper = new ImageImplHelper();

    ImageInterface blue = imgHelper.loadImagePPM(
            "res/SMPTE/SMPTE-blue-greyscale.ppm"
    );

    ImageInterface red = imgHelper.loadImagePPM(
            "res/SMPTE/SMPTE-red-greyscale.ppm"
    );

    ImageInterface randomImage = createRandomImage(100, 100);

    List<ImageInterface> rgb = new ArrayList<>();
    rgb.add(red);
    rgb.add(randomImage);
    rgb.add(blue);

    ImageInterface img = imgHelper.rgbCombine(rgb);
  }

  private ImageInterface createRandomImage(int width, int height) {
    List<PixelInterface> randomArray = new ArrayList<>();
    Random rn = new Random();
    for (int i = 0; i < height * width; i++) {
      PixelInterface rgb = new RGBPixel(
              rn.nextInt(256), rn.nextInt(256), rn.nextInt(256)
      );
      randomArray.add(rgb);
    }
    return new ImageImpl(randomArray, width, height, 255);
  }
}
