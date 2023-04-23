package imagecontroller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import helpers.ImageImplHelper;
import imagecontroller.commands.CommandUtil;
import imagemodel.ImageStore;
import helpers.Response;
import imageview.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class represents the controller for the Image Processing application. It is responsible for
 * handling the user input and delegating the operations to the appropriate model and view. It
 * implements the ImageControllerInterface.
 */
public class ImageController extends AbstractController {
  private final View view;

  // Helper field to keep track of scripts seen already
  // The presence of a script in this set means
  private List<String> scriptsSeen;

  /**
   * Parameterized Constructor for the ImageController class.
   *
   * @param view      the view for the application. Cannot be null.
   * @param imgHelper the image helper for the application. Cannot be null.
   */
  public ImageController(View view, ImageImplHelper imgHelper, ImageStore imageStore) {
    super(imgHelper, imageStore);
    if (view == null) {
      throw new IllegalArgumentException("View cannot be null");
    }

    this.view = view;
  }

  /**
   * This method is responsible for starting the application. It is responsible for
   * getting the input from the user, delegating the operation to the appropriate handler,
   * getting the result from the handler and sending the result to the view.
   */
  public void beginApp() {
    view.clearView();

    try {
      view.sendOutput(new Response("Available commands:\n\n"));
      view.sendOutput(allOperationsSyntax());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    List<String> inputSet = new ArrayList<>();

    while (true) {

      // 1. Get the input from the view
      try {
        view.sendOutput(new Response("Enter a command: "));
        inputSet = view.getNextInput();
      } catch (IOException e) {
        System.err.println("Error reading input from the user");
        System.exit(-10);
      }

      // Termination Condition
      // 1. Intentional termination
      // 2. Controller state has become untenable.
      // Need to restart the controller. Best left to the module
      // starting the controller.

      // Empty line without any characters
      if (inputSet.size() == 0) {
        continue;
      }

      if (inputSet.get(0).equals("help")) {
        try {
          view.sendOutput(new Response("Available commands:\n\n"));
          view.sendOutput(allOperationsSyntax());
        } catch (IOException e) {
          System.err.println("Error sending output to the user");
          System.exit(-11);
        }
        continue;
      }

      if (inputSet.get(0).equals("quit")) {
        break;
      }

      // 1.3. Delegate the operation to the operation handler
      // 1.4. Get the result from the operation handler
      Response resp;
      if (inputSet.get(0).equals("run")) {
        resp = runScript(inputSet.subList(1, inputSet.size()));
      } else {
        resp = this.operationHandler(inputSet.get(0), inputSet.subList(1, inputSet.size()));
      }

      // 1.5. Send the result to the view
      try {
        view.sendOutput(resp);
      } catch (IOException e) {
        System.err.println("Error sending output to the user");
        System.exit(-11);
      }

    }
  }

  private Response allOperationsSyntax() {
    StringBuilder sb = new StringBuilder();
    for (String key : knownCommands.keySet()) {
      sb.append(knownCommands.get(key).toString());
      sb.append("\n");
    }

    sb.append("\n\n");
    sb.append("To view the available commands again, type \"help\". "
            + "To quit the program, type \"quit\".\n");
    return new Response(sb.toString());
  }


  /**
   * This method handles the execution of the run script command. This acts similar to the main
   * method for the application.
   * It reads the file line by line and executes the commands in the file.
   *
   * @param arguments the arguments for the command.
   */
  private Response runScript(List<String> arguments) {
    CommandUtil.validateArgCount("RunScript", arguments, 1);

    // Add the path to the set of scripts seen
    // This is to prevent infinite recursion
    // If the script has already been seen, throw a runtime exception

    if (this.scriptsSeen == null) {
      this.scriptsSeen = new ArrayList<>();
    }

    try {
      this.scriptsSeen.add(arguments.get(0));
      if (containsRepeatedSublist(this.scriptsSeen)) {
        throw new IllegalArgumentException(
            "Infinite recursion detected. Please check the script\n");
      }
      File file = CommandUtil.validateFilePath(arguments.get(0));
      Scanner scanner = new Scanner(file);
      while (scanner.hasNextLine()) {

        String nextLine = scanner.nextLine();

        // Skip comment lines
        if (nextLine.startsWith("#")) {
          continue;
        }

        // Skip empty lines
        if (nextLine.trim().isEmpty()) {
          continue;
        }

        String[] line = nextLine.split(" ");

        String command = line[0].toLowerCase().trim();
        List<String> lineArguments = new ArrayList<>();
        for (int i = 1; i < line.length; i++) {
          lineArguments.add(line[i].trim());
        }

        Response resp;
        if (command.equals("run")) {
          resp = runScript(lineArguments);
        } else {
          resp = operationHandler(command, lineArguments);
        }

        try {
          view.sendOutput(resp);
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
      scanner.close();
    } catch (IllegalArgumentException | FileNotFoundException ile) {
      return new Response("Failure: " + ile.getMessage() + " ");
    }
    return new Response("Success: run\n ");
  }

  private boolean containsRepeatedSublist(List<String> scriptPaths) {
    int n = scriptPaths.size();
    for (int len = 2; len <= (n / 2); len++) {
      for (int i = 0; i <= (n - 2 * len); i++) {
        if (
                scriptPaths.subList(i, i + len).equals(
                        scriptPaths.subList(i + len, i + 2 * len))
        ) {
          // we have found a repeating sublist of length len
          return true;
        }
      }
    }
    return false;
  }
}
