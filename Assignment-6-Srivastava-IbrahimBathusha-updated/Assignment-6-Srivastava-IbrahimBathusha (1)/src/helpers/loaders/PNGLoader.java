package helpers.loaders;

/**
 * This interface represents an image loader. It provides the functionality to load an image from a
 * PNG file.
 */
public class PNGLoader extends GeneralLoader {

  @Override
  public String getSupportedExtension() {
    return "png";
  }
}
