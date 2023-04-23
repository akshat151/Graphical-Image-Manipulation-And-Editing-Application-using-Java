package imagecontroller;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import helpers.ImageImplHelper;
import helpers.Response;
import imagemodel.ColorTransform;
import imagemodel.ImageImpl;
import imagemodel.ImageInterface;
import imagemodel.ImageStore;
import imagemodel.Kernel;
import imageview.GraphicalView;

import static org.junit.Assert.assertEquals;

/**
 * Test class for GraphicalController. This class tests the functionality of GraphicalController
 * class.
 */
public class GraphicalControllerTest {

  private class MockImgImplHelper extends ImageImplHelper {

    @Override
    public ImageInterface loadImagePPM(String filePath) {
      return new MockImageImpl();
    }

    @Override
    public ImageInterface loadImage(String filePath) {
      return new MockImageImpl();
    }

    @Override
    public ImageInterface rgbCombine(List<ImageInterface> rgbSplit) {
      return new MockImageImpl();
    }
  }

  private class MockImageImpl extends ImageImpl implements ImageInterface {

    public MockImageImpl() {
      super();
    }

    @Override
    public ImageInterface flipHorizontal() {
      return this;
    }

    @Override
    public ImageInterface flipVertical() {
      return this;
    }

    @Override
    public ImageInterface brighten(int value) {
      return this;
    }

    @Override
    public ImageInterface greyscale(String color) throws IllegalArgumentException {
      return this;
    }

    @Override
    public ImageInterface greyscale(ColorTransform greyscale) {
      return this;
    }

    @Override
    public List<ImageInterface> rgbSplit() {
      List<ImageInterface> res = new ArrayList<>();
      res.add(this);
      res.add(this);
      res.add(this);
      return res;
    }

    @Override
    public ImageInterface blur(Kernel blur) {
      return this;
    }

    @Override
    public ImageInterface sharpen(Kernel sharpen) {
      return this;
    }

    @Override
    public ImageInterface sepia(ColorTransform sepia) {
      return this;
    }

    @Override
    public ImageInterface dither() {
      return this;
    }
  }

  MockImgImplHelper imgHelper = new MockImgImplHelper();

  @Test
  public void testLoad_success() {
    GraphicalController controller = getController();
    List<String> arguments = new ArrayList<>();
    arguments.add("res/SMPTE/SMPTE-original.ppm");
    arguments.add("img-dest");
    Response output = controller.operationHandler("load", arguments);
    String actualOutput = output.getResponse();
    String expectedOutput = getExpectedOutput(new boolean[]{true}, new String[]{"load"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testLoad_fail() {
    GraphicalController controller = getController();
    List<String> arguments = new ArrayList<>();
    arguments.add("res/SMPTE/SMPTE");
    arguments.add("img-dest");
    Response output = controller.operationHandler("load", arguments);
    String actualOutput = output.getResponse();
    String expectedOutput = getExpectedOutput(new boolean[]{false}, new String[]{"load"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testGreyScale_success() {
    GraphicalController controller = getController();
    List<String> arguments = new ArrayList<>();
    Response output = controller.operationHandler("greyscale", arguments);
    String actualOutput = output.getResponse();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true}, new String[]{"greyscale"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testHorizontal_success() {

    GraphicalController controller = getController();
    List<String> arguments = new ArrayList<>();
    Response output = controller.operationHandler("horizontal-flip", arguments);
    String actualOutput = output.getResponse();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true}, new String[]{"horizontal-flip"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testVertical_success() {

    GraphicalController controller = getController();
    List<String> arguments = new ArrayList<>();
    Response output = controller.operationHandler("vertical-flip", arguments);
    String actualOutput = output.getResponse();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true}, new String[]{"vertical-flip"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testBrighten_success() {

    GraphicalController controller = getController();
    List<String> arguments = new ArrayList<>();
    Response output = controller.operationHandler("brighten", arguments);
    String actualOutput = output.getResponse();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true}, new String[]{"brighten"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testBlur_success() {

    GraphicalController controller = getController();
    List<String> arguments = new ArrayList<>();
    Response output = controller.operationHandler("blur", arguments);
    String actualOutput = output.getResponse();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true}, new String[]{"blur"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testSharpen_success() {

    GraphicalController controller = getController();
    List<String> arguments = new ArrayList<>();
    Response output = controller.operationHandler("sharpen", arguments);
    String actualOutput = output.getResponse();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true}, new String[]{"sharpen"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testSepia_success() {

    GraphicalController controller = getController();
    List<String> arguments = new ArrayList<>();
    Response output = controller.operationHandler("sepia", arguments);
    String actualOutput = output.getResponse();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true}, new String[]{"sepia"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testDither_success() {

    GraphicalController controller = getController();
    List<String> arguments = new ArrayList<>();
    Response output = controller.operationHandler("dither", arguments);
    String actualOutput = output.getResponse();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true}, new String[]{"dither"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  private String getExpectedOutput(boolean[] successArr, String[] commands) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < commands.length; i++) {
      sb.append(getCommandOutput(successArr[i], commands[i]));
    }
    return sb.toString();
  }

  private boolean compareOutputs(String actual, String expected) {

    // System.out.println("A:"+actual);
    // System.out.println("E:"+expected);

    String[] ac1 = actual.split(" ");
    String[] ex1 = expected.split(" ");
    int index1 = 0;
    int index2 = 0;
    while (index1 < ac1.length && index2 < ex1.length) {
      if (ac1[index1].equals("Failure:") && ex1[index2].equals(ac1[index1])) {
        return true;
      }
      if (index1 >= ac1.length || !ac1[index1].equals(ex1[index2])) {
        return false;
      }
      index1++;
      index2++;
    }
    return true;
  }

  private String getCommandOutput(boolean success, String command) {
    if (success) {
      return "Success: " + command + "\n ";
    } else {
      return "Failure: ";
    }
  }

  private GraphicalController getController() {
    GraphicalView gView = new GraphicalView("GRIME");
    ImageStore imageStore = new ImageStore();
    imageStore.getStack().push("img-src");
    imageStore.getImages().put("img-src", new MockImageImpl());
    return new GraphicalController(gView, imgHelper, imageStore);
  }

}
