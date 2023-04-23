package imagecontroller.savers;

/**
 * JPEGSaver class implements ImageSaver. JPEGSaver class is used to save an image as a JPEG.
 */
public class JPEGSaver extends GenericSaver {

  @Override
  public String getExtension() {
    return "jpeg";
  }
}