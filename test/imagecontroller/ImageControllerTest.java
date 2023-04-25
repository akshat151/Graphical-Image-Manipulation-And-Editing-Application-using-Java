package imagecontroller;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import helpers.ImageImplHelper;
import imagemodel.ColorTransform;
import imagemodel.ImageImpl;
import imagemodel.ImageInterface;

import imagemodel.ImageStore;
import imagemodel.Kernel;
import imageview.TerminalView;
import imageview.TerminalViewTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test class for ImageController. This class tests the functionality of the ImageController
 * class.
 */
public class ImageControllerTest {

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

    @Override
    public ImageInterface mosaic(int seeds) {
      return this;
    }
  }

  /**
   * Convenience class for testing. This class will append "quit\n" to the end of the input if it
   * does not contain "quit\n". This is to make sure that the controller will quit after the input
   * is processed.
   */
  private class ReadableInputStreamQuitDefault extends TerminalViewTest.ReadableInputStream {
    public ReadableInputStreamQuitDefault(String inputEdit) {
      super(inputEdit);

      if (!this.input.contains("quit\n")) {
        if (this.input.endsWith("\n")) {
          input += "quit\n";
        } else {
          input += "\nquit\n";
        }
      }
    }
  }

  MockImgImplHelper imgHelper = new MockImgImplHelper();

  @Test
  public void testLoad_success() {
    TerminalViewTest.ReadableInputStream inputStream =
            new ReadableInputStreamQuitDefault("load res/SMPTE/SMPTE-original.ppm SMPTE");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(new boolean[]{true}, new String[]{"load"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testLoad_fail_wrongArgs() {
    TerminalViewTest.ReadableInputStream inputStream =
            new ReadableInputStreamQuitDefault("load res/SMPTE/SMPTE-original.ppm");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(new boolean[]{false}, new String[]{"load"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testLoad_fail_wrongFile() {
    TerminalViewTest.ReadableInputStream inputStream =
            new ReadableInputStreamQuitDefault("load res/wrongSMPTE/SMPTE-original.ppm SMPTE");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(new boolean[]{false}, new String[]{"load"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testLoadLoaderDispatch() {
    TerminalViewTest.ReadableInputStream inputStream =
            new ReadableInputStreamQuitDefault("load res/SMPTE/SMPTE-original.ppm SMPTE");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(new boolean[]{true}, new String[]{"load"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testSave_success() {
    TerminalViewTest.ReadableInputStream inputStream =
            new ReadableInputStreamQuitDefault(
                    "load res/SMPTE/SMPTE-original.ppm SMPTE\n save res/SMPTETest.ppm SMPTE");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getTrueController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, true}, new String[]{"load", "save"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testSave_fail_noDir() {
    TerminalViewTest.ReadableInputStream inputStream =
            new ReadableInputStreamQuitDefault(
                    "load res/SMPTE/SMPTE-original.ppm SMPTE\n save images/SMPTETest.ppm SMPTE");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, false}, new String[]{"load", "save"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testSave_fail_wrongArgs() {
    TerminalViewTest.ReadableInputStream inputStream =
            new ReadableInputStreamQuitDefault(
                    "load res/SMPTE/SMPTE-original.ppm SMPTE\n save images/SMPTETest.ppm");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, false}, new String[]{"load", "save"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testSave_fail_noImage() {
    TerminalViewTest.ReadableInputStream inputStream =
            new ReadableInputStreamQuitDefault(
                    "load res/SMPTE/SMPTE-original.ppm SMPTE\n save images/SMPTETest.ppm");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, false}, new String[]{"load", "save"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testGreyScale_success() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/SMPTE-original.ppm SMPTE\n"
                    + " greyscale red-component SMPTE SMPTE-greyscale");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, true}, new String[]{"load", "greyscale"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testGreyScale_fail_wrongImage() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/SMPTE-original.ppm SMPTE\n"
                    + " greyscale red-component wrongSMPTE SMPTE-greyscale");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, false}, new String[]{"load", "greyscale"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testGreyScale_fail_wrongArgs() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/SMPTE-original.ppm SMPTE\n"
                    + " greyscale red-component SMPTE");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, false}, new String[]{"load", "greyscale"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testHorizontal_success() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/SMPTE-original.ppm SMPTE\n"
                    + " horizontal-flip SMPTE SMPTE-horizontal");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, true}, new String[]{"load", "horizontal-flip"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testHorizontal_fail_wrongArgs() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/SMPTE-original.ppm SMPTE\n horizontal-flip SMPTE");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, false}, new String[]{"load", "horizontal-flip"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testHorizontal_fail_wrongImage() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/SMPTE-original.ppm SMPTE\n horizontal-flip wrongSMPTE SMPTE-h");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, false}, new String[]{"load", "horizontal-flip"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testVertical_success() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/SMPTE-original.ppm SMPTE\n vertical-flip SMPTE SMPTE-vertical");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, true}, new String[]{"load", "vertical-flip"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testVertical_fail_wrongArgs() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/SMPTE-original.ppm SMPTE\n vertical-flip SMPTE");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, false}, new String[]{"load", "vertical-flip"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testVertical_wrongImage() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/SMPTE-original.ppm SMPTE\n"
                    + " vertical-flip wrongSMPTE SMPTE-vertical");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, false}, new String[]{"load", "vertical-flip"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testBrighten_success() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/SMPTE-original.ppm SMPTE\n brighten 10 SMPTE SMPTE-brighter");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, true}, new String[]{"load", "brighten"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testBrighten_fail_wrongArgs() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/SMPTE-original.ppm SMPTE\n brighten SMPTE SMPTE-brighter");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, false}, new String[]{"load", "brighten"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testBrighten_fail_wrongImage() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/SMPTE-original.ppm SMPTE\n brighten 10 wrongSMPTE SMPTE-brighter");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, false}, new String[]{"load", "brighten"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testRGBSplit_success() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/SMPTE-original.ppm SMPTE\n"
                    + " rgb-split SMPTE SMPTE-red SMPTE-green SMPTE-blue");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, true}, new String[]{"load", "rgb-split"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testRGBSplit_fail_wrongArgs() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/SMPTE-original.ppm SMPTE\n rgb-split SMPTE SMPTE-red SMPTE-green");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, false}, new String[]{"load", "rgb-split"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testRGBSplit_fail_wrongImage() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/SMPTE-original.ppm SMPTE\n"
                    + " rgb-split wrongSMPTE SMPTE-red SMPTE-green SMPTE-blue");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, false}, new String[]{"load", "rgb-split"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testRGBCombine_success() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/SMPTE-original.ppm SMPTE_r\n"
                    + "load res/SMPTE/SMPTE-original.ppm SMPTE_g\n"
                    + "load res/SMPTE/SMPTE-original.ppm SMPTE_b\n"
                    + " rgb-combine SMPTE SMPTE_r SMPTE_g SMPTE_b");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, true, true, true},
            new String[]{"load", "load", "load", "rgb-combine"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testRGBCombine_fail_wrongArgs() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/SMPTE-original.ppm SMPTE_r\n"
                    + "load res/SMPTE/SMPTE-original.ppm SMPTE_g\n"
                    + "load res/SMPTE/SMPTE-original.ppm SMPTE_b\n"
                    + " rgb-combine SMPTE SMPTE_r SMPTE_g");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, true, true, false},
            new String[]{"load", "load", "load", "rgb-combine"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testRGBCombine_fail_wrongImage() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/SMPTE-original.ppm SMPTE_r\n"
                    + "load res/SMPTE/SMPTE-original.ppm SMPTE_g\n"
                    + "load res/SMPTE/SMPTE-original.ppm SMPTE_b\n"
                    + " rgb-combine SMPTE SMPTE_red SMPTE_g SMPTE_b");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, true, true, false},
            new String[]{"load", "load", "load", "rgb-combine"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testRunScript_success() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "run res/scripts/script1");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getTrueController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    boolean[] operationResArr = new boolean[21];
    Arrays.fill(operationResArr, true);

    assertEquals("\u001B[H\u001B[2JAvailable commands:\n"
            + "\n"
            + "brighten value img-src img-dest\n"
            + "sharpen img-src img-dest\n"
            + "dither img-src img-dest\n"
            + "Save file-path image-name\n"
            + "blur img-src img-dest\n"
            + "greyscale component img-src img-dest\n"
            + "greyscale img-src img-dest\n"
            + "horizontal-flip img-src img-dest\n"
            + "rgb-split img-src img-red img-green img-blue\n"
            + "sepia img-src img-dest\n"
            + "rgb-combine img-dest img-red img-green img-blue\n"
            + "Load file-path image-name\n"
            + "vertical-flip img-src img-dest\n"
            + "mosaic num-seeds img-src img-dest\n"
            + "\n"
            + "\n"
            + "To view the available commands again, type \"help\". To quit the program, "
            + "type \"quit\".\n"
            + "Enter a command: Success: load\n"
            + " Success: load\n"
            + " Success: load\n"
            + " Success: brighten\n"
            + " Success: vertical-flip\n"
            + " Success: blur\n"
            + " Success: sharpen\n"
            + " Success: sepia\n"
            + " Success: dither\n"
            + " Success: horizontal-flip\n"
            + " Success: greyscale\n"
            + " Success: greyscale\n"
            + " Success: save\n"
            + " Success: save\n"
            + " Success: rgb-split\n"
            + " Success: brighten\n"
            + " Success: rgb-combine\n"
            + " Success: save\n"
            + " Success: save\n"
            + " Success: save\n"
            + " Success: load\n"
            + " Success: mosaic\n"
            + " Success: save\n"
            + " Success: run\n"
            + " Enter a command: ", actualOutput);
  }

  @Test
  public void testRunScript_failsInluded() {
    TerminalViewTest.ReadableInputStream inputStream =
            new ReadableInputStreamQuitDefault("run res/scripts/scriptFail");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getTrueController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    boolean[] operationResArr = new boolean[]{true, false, true, false, true, true, true, true};
    String[] commands = new String[]{"load", "brighten", "brighten", "greyscale",
        "greyscale", "save", "save", "run"};

    String expectedOutput = getExpectedScriptOutput(operationResArr, commands);

    assertEquals(true, compareScriptOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void invalidOperationTest_fail() {
    TerminalViewTest.ReadableInputStream inputStream =
            new ReadableInputStreamQuitDefault("lopes");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(new boolean[]{false}, new String[]{"load"});
    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void multipleOperationsInOneLineTest_fail() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load images/SMPTE-original.ppm SMPTE "
                    + "brighten 10 SMPTE SMPTE-brighter \n"
    );
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(new boolean[]{false}, new String[]{"load"});
    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void commentInOperationLineTest_fail() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load images/SMPTE-original.ppm SMPTE "
                    + "# brighten 10 SMPTE SMPTE-brighter \n"
    );
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(new boolean[]{false}, new String[]{"load"});
    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }


  @Test
  public void missingScriptTest_fail() {
    TerminalViewTest.ReadableInputStream inputStream =
            new ReadableInputStreamQuitDefault("run res/scripts/missing.txt");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(new boolean[]{false}, new String[]{"run"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testNoObjectName_fail() {
    TerminalViewTest.ReadableInputStream inputStream =
            new ReadableInputStreamQuitDefault("load "
                    + "res/SMPTE/SMPTE-original.ppm\n");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(new boolean[]{false}, new String[]{"load"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testBlur_success() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/SMPTE-original.ppm SMPTE\n"
                    + " blur SMPTE SMPTE-blur");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, true}, new String[]{"load", "blur"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testBlur_fail_wrongArgs() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/SMPTE-original.ppm SMPTE\n blur SMPTE");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, false}, new String[]{"load", "blur"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testBlur_fail_wrongImage() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/SMPTE-original.ppm SMPTE\n blur wrongSMPTE SMPTE-b");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, false}, new String[]{"load", "blur"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testSharpen_success() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/SMPTE-original.ppm SMPTE\n"
                    + " sharpen SMPTE SMPTE-sharpen");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, true}, new String[]{"load", "sharpen"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testSharpen_fail_wrongArgs() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/SMPTE-original.ppm SMPTE\n sharpen SMPTE");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, false}, new String[]{"load", "sharpen"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testSharpen_fail_wrongImage() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/SMPTE-original.ppm SMPTE\n sharpen wrongSMPTE SMPTE-s");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, false}, new String[]{"load", "sharpen"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testSepia_success() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/SMPTE-original.ppm SMPTE\n"
                    + " sepia SMPTE SMPTE-sepia");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, true}, new String[]{"load", "sepia"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testSepia_fail_wrongArgs() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/SMPTE-original.ppm SMPTE\n sepia SMPTE");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, false}, new String[]{"load", "sepia"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testSepia_fail_wrongImage() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/SMPTE-original.ppm SMPTE\n sepia wrongSMPTE SMPTE-s");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, false}, new String[]{"load", "sepia"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testGreyscale_transform_success() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/SMPTE-original.ppm SMPTE\n"
                    + " greyscale SMPTE SMPTE-greyscale");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, true}, new String[]{"load", "greyscale"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testGreyscale_transform_fail_wrongArgs() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/SMPTE-original.ppm SMPTE\n greyscale SMPTE");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, false}, new String[]{"load", "greyscale"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testGreyscale_transform_fail_wrongImage() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/SMPTE-original.ppm SMPTE\n greyscale wrongSMPTE SMPTE-s");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, false}, new String[]{"load", "greyscale"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testDither_success() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/SMPTE-original.ppm SMPTE\n"
                    + " dither SMPTE SMPTE-dither");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, true}, new String[]{"load", "dither"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testDither_fail_wrongArgs() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/SMPTE-original.ppm SMPTE\n dither SMPTE");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, false}, new String[]{"load", "dither"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testDither_fail_wrongImage() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/SMPTE-original.ppm SMPTE\n dither wrongSMPTE SMPTE-s");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, false}, new String[]{"load", "dither"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testMosaic_success() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/snow.ppm snow\n"
                    + " mosaic 500 snow snow-mosaic");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, true}, new String[]{"load", "mosaic"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testMosaic_fail_wrongArgs() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/snow.ppm snow\n mosaic snow");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, false}, new String[]{"load", "mosaic"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testMosaic_fail_wrongImage() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/snow.ppm snow\n mosaic 10 wrongSMPTE snow-s");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();
    String expectedOutput = getExpectedOutput(
            new boolean[]{true, false}, new String[]{"load", "mosaic"});

    assertEquals(true, compareOutputs(actualOutput, expectedOutput));
  }

  @Test
  public void testMosaic_fail_NegativeSeed() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/snow.ppm snow\n mosaic -1000 snow snow-s");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();

    assertEquals("\u001B[H\u001B[2JAvailable commands:\n"
            + "\n"
            + "brighten value img-src img-dest\n"
            + "sharpen img-src img-dest\n"
            + "dither img-src img-dest\n"
            + "Save file-path image-name\n"
            + "blur img-src img-dest\n"
            + "greyscale component img-src img-dest\n"
            + "greyscale img-src img-dest\n"
            + "horizontal-flip img-src img-dest\n"
            + "rgb-split img-src img-red img-green img-blue\n"
            + "sepia img-src img-dest\n"
            + "rgb-combine img-dest img-red img-green img-blue\n"
            + "Load file-path image-name\n"
            + "vertical-flip img-src img-dest\n"
            + "mosaic num-seeds img-src img-dest\n"
            + "\n"
            + "\n"
            + "To view the available commands again, type \"help\". To quit the program, "
            + "type \"quit\".\n"
            + "Enter a command: Success: load\n"
            + " Enter a command: Failure: Please enter positive number of seeds "
            + "Enter a command: ", actualOutput);
  }

  @Test
  public void testMosaic_fail_CharacterSeed() {
    TerminalViewTest.ReadableInputStream inputStream = new ReadableInputStreamQuitDefault(
            "load res/SMPTE/snow.ppm snow\n mosaic gth snow snow-s");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();

    assertEquals("\u001B[H\u001B[2JAvailable commands:\n"
            + "\n"
            + "brighten value img-src img-dest\n"
            + "sharpen img-src img-dest\n"
            + "dither img-src img-dest\n"
            + "Save file-path image-name\n"
            + "blur img-src img-dest\n"
            + "greyscale component img-src img-dest\n"
            + "greyscale img-src img-dest\n"
            + "horizontal-flip img-src img-dest\n"
            + "rgb-split img-src img-red img-green img-blue\n"
            + "sepia img-src img-dest\n"
            + "rgb-combine img-dest img-red img-green img-blue\n"
            + "Load file-path image-name\n"
            + "vertical-flip img-src img-dest\n"
            + "mosaic num-seeds img-src img-dest\n"
            + "\n"
            + "\n"
            + "To view the available commands again, type \"help\". To quit the program, "
            + "type \"quit\".\n"
            + "Enter a command: Success: load\n"
            + " Enter a command: Failure: For input string: \"gth\" "
            + "Enter a command: ", actualOutput);
  }

  @Test
  public void scriptTest() {
    TerminalViewTest.ReadableInputStream inputStream =
            new ReadableInputStreamQuitDefault("run res/SMPTE/script1");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();

    assertEquals("\u001B[H\u001B[2JAvailable commands:\n"
            + "\n"
            + "brighten value img-src img-dest\n"
            + "sharpen img-src img-dest\n"
            + "dither img-src img-dest\n"
            + "Save file-path image-name\n"
            + "blur img-src img-dest\n"
            + "greyscale component img-src img-dest\n"
            + "greyscale img-src img-dest\n"
            + "horizontal-flip img-src img-dest\n"
            + "rgb-split img-src img-red img-green img-blue\n"
            + "sepia img-src img-dest\n"
            + "rgb-combine img-dest img-red img-green img-blue\n"
            + "Load file-path image-name\n"
            + "vertical-flip img-src img-dest\n"
            + "mosaic num-seeds img-src img-dest\n"
            + "\n"
            + "\n"
            + "To view the available commands again, type \"help\". To quit the program, "
            + "type \"quit\".\n"
            + "Enter a command: Success: load\n"
            + " Success: load\n"
            + " Success: load\n"
            + " Success: brighten\n"
            + " Success: vertical-flip\n"
            + " Success: blur\n"
            + " Success: sharpen\n"
            + " Success: sepia\n"
            + " Success: dither\n"
            + " Success: horizontal-flip\n"
            + " Success: greyscale\n"
            + " Success: greyscale\n"
            + " Success: rgb-split\n"
            + " Success: brighten\n"
            + " Success: rgb-combine\n"
            + " Success: load\n"
            + " Success: mosaic\n"
            + " Success: run\n"
            + " Enter a command: ", actualOutput);
  }

  // This test will more often fail with a stack overflow error than a timeout error. This is
  // because the test script for this is relatively small and the processing is fast enough that
  // the stack limit is reached via recursion.
  @Test(timeout = 1000)
  public void testControllerInfiniteLoopScriptIncludesSelf() {
    TerminalViewTest.ReadableInputStream inputStream =
            new ReadableInputStreamQuitDefault("run res/scripts/infiniteLoop.txt");
    TerminalViewTest.WritableOutputStream outputStream =
            new TerminalViewTest.WritableOutputStream();
    getController(inputStream, outputStream).beginApp();
    String actualOutput = outputStream.getOutput();

    assertTrue(actualOutput.contains("Failure: Infinite recursion detected. "
            + "Please check the script"));
  }

  private ImageController getController(TerminalViewTest.ReadableInputStream inputStream,
                                        TerminalViewTest.WritableOutputStream outputStream) {
    TerminalView terminalView = new TerminalView(
            inputStream,
            outputStream
    );

    ImageStore imageStore = new ImageStore();
    return new ImageController(terminalView, imgHelper, imageStore);
  }

  private ImageController getTrueController(TerminalViewTest.ReadableInputStream inputStream,
                                            TerminalViewTest.WritableOutputStream outputStream) {
    TerminalView terminalView = new TerminalView(
            inputStream,
            outputStream
    );
    return new ImageController(
            terminalView,
            new ImageImplHelper(),
            new ImageStore()
    );
  }

  private String getExpectedOutput(boolean[] successArr, String[] commands) {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < commands.length; i++) {
      sb.append("Enter a command: ");
      sb.append(getCommandOutput(successArr[i], commands[i]));
    }
    sb.append("Enter a command: ");
    return sb.toString();
  }

  private String getExpectedScriptOutput(boolean[] successArr, String[] commands) {
    StringBuilder sb = new StringBuilder();
    sb.append("Enter a command: ");
    for (int i = 0; i < commands.length; i++) {
      sb.append(getCommandOutput(successArr[i], commands[i]));
    }
    sb.append("Enter a command: ");
    return sb.toString();
  }

  private String getCommandOutput(boolean success, String command) {
    if (success) {
      return "Success: " + command + "\n ";
    } else {
      return "Failure: ";
    }
  }

  private boolean compareOutputs(String actual, String expected) {
    actual = actual.substring(actual.indexOf("Enter"), actual.length());

    String[] ac1 = actual.split(" ");
    String[] ex1 = expected.split(" ");
    int index1 = 0;
    int index2 = 0;
    while (index1 < ac1.length && index2 < ex1.length) {
      if (ac1[index1].equals("Available")) {
        while (index1 < ac1.length && !ac1[index1].equals("Enter")) {
          index1++;
        }
      } else if (ac1[index1].equals("Failure:") && ex1[index2].equals(ac1[index1])) {
        while (index1 < ac1.length && !ac1[index1].equals("Enter")) {
          index1++;
        }
        index2++;
      }
      if (index1 >= ac1.length || !ac1[index1].equals(ex1[index2])) {
        return false;
      }
      index1++;
      index2++;
    }
    return true;
  }

  private boolean compareScriptOutputs(String actual, String expected) {
    actual = actual.substring(actual.indexOf("Enter"), actual.length());

    String[] ac1 = actual.split(" ");
    String[] ex1 = expected.split(" ");
    int index1 = 0;
    int index2 = 0;
    while (index1 < ac1.length && index2 < ex1.length) {
      if (ac1[index1].equals("Failure:") && ex1[index2].equals(ac1[index1])) {
        while (index1 < ac1.length && !ac1[index1].equals("Success:")) {
          index1++;
        }
        index2++;
      }
      if (index1 >= ac1.length || !ac1[index1].equals(ex1[index2])) {
        return false;
      }
      index1++;
      index2++;
    }
    return true;
  }
}