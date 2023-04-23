package imagecontroller.commands;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import imagemodel.ImageInterface;

/**
 * Command class contains static methods that are used to validate the arguments of the commands.
 */
public class CommandUtil {

  /**
   * Validates that the number of arguments is equal to the expected length.
   *
   * @param command     the name of the command.
   * @param arguments   the list of arguments.
   * @param expectedLen the expected length of the arguments.
   * @throws IllegalArgumentException if the number of arguments is not equal to the expected
   *                                  length.
   */
  public static void validateArgCount(String command, List<String> arguments, int expectedLen)
          throws IllegalArgumentException {
    if (arguments == null || arguments.size() != expectedLen) {
      throw new IllegalArgumentException(command + " command needs " + expectedLen + " arguments");
    }
  }

  /**
   * Validates that the image exists.
   *
   * @param images  the map of images.
   * @param imgName the name of the image.
   * @throws IllegalArgumentException if the image does not exist.
   */
  public static void validateImageExists(Map<String, ImageInterface> images, String imgName)
          throws IllegalArgumentException {
    if (!images.containsKey(imgName)) {
      throw new IllegalArgumentException("Image: " + imgName + " does not exist");
    }
  }

  /**
   * Validates that the file exists and is not empty.
   *
   * @param filePath the path to the file.
   * @return the file.
   * @throws IllegalArgumentException if the file does not exist or is empty.
   */
  public static File validateFilePath(String filePath) throws IllegalArgumentException {
    if (filePath == null) {
      throw new IllegalArgumentException("The file path is null");
    }

    File file = new File(filePath);
    if (!file.exists()) {
      throw new IllegalArgumentException("The file does not exist : " + filePath + "\n");
    }

    if (file.length() == 0) {
      throw new IllegalArgumentException("The file is empty");
    }
    return file;
  }

  /**
   * Validates that the folder exists.
   *
   * @param folderPath the path to the folder.
   */
  public static void validateFolder(String folderPath) {
    if (folderPath == null) {
      throw new IllegalArgumentException("Folder path is null");
    }

    if (!folderPath.contains("/") && !folderPath.contains("\\")) {
      // Current folder operations
      folderPath = "./" + folderPath;
    }

    Path a = Paths.get(folderPath);

    if (folderPath == null) {
      throw new IllegalArgumentException("Folder path is null");
    }

    if (!Files.exists(a.getParent())) {
      throw new IllegalArgumentException("Folder does not exist: " + folderPath);
    }
  }

  /**
   * Adds appropriate args if no args are given.
   */
  public static List<String> fixArgs(Stack<String> workedOn, List<String> arguments) {
    if (arguments.size() == 0 && workedOn.size() > 0) {
      String source = workedOn.peek();
      arguments.add(source);
      arguments.add(source + Instant.now());
    }
    return arguments;
  }
}
