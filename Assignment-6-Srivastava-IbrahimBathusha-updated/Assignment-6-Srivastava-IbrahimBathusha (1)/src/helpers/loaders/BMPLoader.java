package helpers.loaders;

/**
 * This class implements the ImageLoader interface. It provides the basic functionality to load an
 * image from a BMP file.
 */
public class BMPLoader extends GeneralLoader {

  @Override
  public String getSupportedExtension() {
    return "bmp";
  }
}
