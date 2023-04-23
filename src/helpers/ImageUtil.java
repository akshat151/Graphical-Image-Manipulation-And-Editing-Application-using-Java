package helpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileInputStream;

import imagemodel.ImageImpl;
import imagemodel.ImageInterface;
import imagemodel.PixelInterface;
import imagemodel.RGBPixel;


/**
 * This class contains utility methods to read a PPM image from file.
 * Feel free to change this method as required.
 */
public class ImageUtil {

  /**
   * Read an image file in the PPM format and print the colors.
   *
   * @param filename the path of the file.
   */
  public static ImageInterface readPPM(String filename) {
    Scanner sc;

    try {
      sc = new Scanner(new FileInputStream(filename));
    } catch (FileNotFoundException e) {
      System.out.println("File " + filename + " not found!");
      return null;
    }
    StringBuilder builder = new StringBuilder();
    //read the file line by line, and populate a string. This will throw away any comment lines
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.charAt(0) != '#') {
        builder.append(s).append(System.lineSeparator());
      }
    }
    return readString(builder.toString());
  }

  /**
   * Read an image file in the PPM format and print the colors.
   *
   * @param fileBytes the byte array of the file.
   */
  public static ImageInterface readPPM(byte[] fileBytes) {
    Scanner sc;

    try {
      sc = new Scanner(new String(fileBytes));
    } catch (Exception e) {
      System.out.println("File not found!");
      return null;
    }

    StringBuilder builder = new StringBuilder();
    //read the file line by line, and populate a string. This will throw away any comment lines
    while (sc.hasNextLine()) {
      String s = sc.nextLine();
      if (s.charAt(0) != '#') {
        builder.append(s).append(System.lineSeparator());
      }
    }
    return readString(builder.toString());
  }

  private static ImageInterface readString(String fileContent) {
    Scanner sc = new Scanner(fileContent);
    String token;

    token = sc.next();
    if (!token.equals("P3")) {
      System.out.println("Invalid PPM file: plain RAW file should begin with P3");
    }
    int width = sc.nextInt();
    //System.out.println("Width of image: "+width);
    int height = sc.nextInt();
    //System.out.println("Height of image: "+height);
    int maxColorValue = sc.nextInt();
    //System.out.println("Maximum value of a color in this file (usually 255): "+maxColorValue);
    List<PixelInterface> rgbArray = new ArrayList<>();
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int r = sc.nextInt();
        int g = sc.nextInt();
        int b = sc.nextInt();
        PixelInterface rgb = new RGBPixel(r, g, b);
        rgbArray.add(rgb);
      }
    }
    return new ImageImpl(rgbArray, width, height, maxColorValue);
  }

  /**
   * Demo main to read a PPM file.
   * @param args the command line arguments
   */
  //demo main
  public static void main(String[] args) {
    String filename;

    if (args.length > 0) {
      filename = args[0];
    } else {
      filename = "res\\koala.ppm";
    }

    ImageUtil.readPPM(filename);
  }
}

