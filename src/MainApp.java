import java.io.ByteArrayInputStream;
import java.io.InputStream;

import helpers.ImageImplHelper;
import imagecontroller.GraphicalController;
import imagecontroller.ImageController;
import imagecontroller.ImageControllerInterface;
import imagemodel.ImageStore;
import imageview.GraphicalView;
import imageview.TerminalView;
import imageview.View;

/**
 * This class is used to set up the view and controller and provide a way to run the program.
 */
public class MainApp {

  /**
   * Main method that runs the program.
   */
  public static void main(String[] args) {

    InputStream in = null;

    //Create a ImageHelper
    ImageImplHelper imgHelper = new ImageImplHelper();

    //Create a model for storing images
    ImageStore imageStore = new ImageStore();

    ImageControllerInterface controller;

    boolean textModeFlag = false;

    // -file filepath
    // -text
    if (args.length == 2 && args[0].equals("-file")) {
      in = new ByteArrayInputStream(("run" + " " + args[1] + "\n" + "quit\n").getBytes());
      textModeFlag = true;
    } else if (args.length == 1 && args[0].equals("-text")) {
      in = System.in;
      textModeFlag = true;
    } else if (args.length == 0) {
      GraphicalView gView = new GraphicalView("GRIME");
      controller = new GraphicalController(gView, imgHelper, imageStore);
      controller.beginApp();
    } else {
      System.out.println("Invalid arguments, please try with correct arguments");
    }

    if (textModeFlag) {
      System.out.println("Image Manipulation Program");
      View view = new TerminalView(
              in,
              System.out
      );
      controller = new ImageController(view, imgHelper, imageStore);
      controller.beginApp();
    }
  }
}
