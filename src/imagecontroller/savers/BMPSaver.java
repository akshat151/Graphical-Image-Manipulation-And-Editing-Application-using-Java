package imagecontroller.savers;

/**
 * This class implements the ImageSaver interface. It provides the functionality to save an
 * image to a BMP file.
 */
public class BMPSaver extends GenericSaver {
  @Override
  public String getExtension() {
    return "bmp";
  }
}
