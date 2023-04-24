package imagecontroller.savers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import helpers.Response;
import imagecontroller.ImageSaver;
import imagemodel.ImageInterface;


/**
 * PPMSaver class implements ImageSaver. PPMSaver class is used to save an image as a PPM.
 */
public class PPMSaver implements ImageSaver {

  @Override
  public Response save(ImageInterface image, String path) {

    // PPM format attributes
    String magicNumber = "P3";
    int width = image.getImageArray().get(0).size();
    int height = image.getImageArray().size();

    // Write the PPM file
    try {
      PrintWriter writer = new PrintWriter(path, StandardCharsets.UTF_8);
      writer.println(magicNumber);
      writer.println(width + " " + height);
      writer.println(255);
      for (int i = 0; i < image.getImageArray().size(); i++) {
        for (int j = 0; j < image.getImageArray().get(i).size(); j++) {
          writer.print(image.getImageArray().get(i).get(j).getRed() + " ");
          writer.print(image.getImageArray().get(i).get(j).getGreen() + " ");
          writer.print(image.getImageArray().get(i).get(j).getBlue() + " ");
        }
        writer.println();
      }
      writer.close();
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("The file is not found at path : " + path);
    } catch (IOException e) {
      throw new RuntimeException("Error while writing to file : "
              + path + " : " + e.getMessage());
    }

    return new Response("Image saved successfully");
  }

  @Override
  public String getExtension() {
    return "ppm";
  }
}
