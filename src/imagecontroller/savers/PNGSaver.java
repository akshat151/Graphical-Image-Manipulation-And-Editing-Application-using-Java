package imagecontroller.savers;

/**
 * PNGSaver class implements ImageSaver. PNGSaver class is used to save an image as a PNG.
 */
public class PNGSaver extends GenericSaver {
  @Override
  public String getExtension() {
    return "png";
  }
}
