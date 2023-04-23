package helpers.loaders;

/**
 * This class implements the ImageLoader interface. It provides the basic functionality to load a
 * JPEG image from a file.
 */
public class JpegLoader extends GeneralLoader {

  @Override
  public String getSupportedExtension() {
    return "jpeg";
  }
}
