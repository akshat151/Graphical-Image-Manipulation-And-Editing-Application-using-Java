package imagemodel;

public interface ImageInterfaceExtension extends  ImageInterface{

  /**
   * Mosaics the image by the given seed_value.
   * @param num_of_seeds by which to mosaic this image.
   * @return a new image that is the changed version of the original.
   */
  ImageInterface mosaic(int num_of_seeds);
}
