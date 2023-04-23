package imagemodel;

import java.util.ArrayList;
import java.util.List;

public class ImageExtensionImpl extends ImageImpl implements ImageInterfaceExtension{
  @Override
  public ImageInterface mosaic(int num_of_seeds) {
    int n = this.getImageArray().size();
    int m = this.getImageArray().get(0).size();

    List<List<PixelInterface>> newImageArray = new ArrayList<>();
    for (int i = 0; i < n; i++) {
      List<PixelInterface> row = new ArrayList<>();
      for (int j = 0; j < m; j++) {
        /*int r = makeValueAcceptable(this.getImageArray().get(i).get(j).getRed() + num_of_seeds);
        int g = makeValueAcceptable(this.getImageArray().get(i).get(j).getGreen() + num_of_seeds);
        int b = makeValueAcceptable(this.getImageArray().get(i).get(j).getBlue() + num_of_seeds);*/
        //row.add(new RGBPixel(r, g, b));
      }
      newImageArray.add(row);
    }
    return new ImageImpl(newImageArray);
  }
}
