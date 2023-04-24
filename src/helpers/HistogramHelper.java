package helpers;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class contains utility methods to generate a histogram of an image.
 */
public class HistogramHelper {

  private static int getChannelVal(int rgb, String channel) {
    switch (channel) {
      case "red":
        return (rgb & 0x00ff0000) >> 16;
      case "green":
        return (rgb & 0x0000ff00) >> 8;
      case "blue":
        return rgb & 0x000000ff;
      case "intensity":
        return (int) Math.round((
                (double) ((rgb & 0x00ff0000) >> 16)
                        + (double) ((rgb & 0x0000ff00) >> 8)
                        + (double) (rgb & 0x000000ff)
        ) / 3.0);
      default:
        return 0;
    }
  }

  /**
   * Returns a histogram of the image for the given channel.
   *
   * @param image   - the image
   * @param channel - the channel for which the histogram is to be generated
   * @return a list of 256 elements, each element representing the number of pixels in the image
   */
  public static List<Double> getHistogram(BufferedImage image, String channel) {
    List<Double> histogram = new ArrayList<>();
    for (int i = 0; i < 256; i++) {
      histogram.add(0.0);
    }

    for (int i = 0; i < image.getWidth(); i++) {
      for (int j = 0; j < image.getHeight(); j++) {
        int pixelChannelVal = getChannelVal(image.getRGB(i, j), channel);
        histogram.set(pixelChannelVal, histogram.get(pixelChannelVal) + 1);
      }
    }

    return histogram;
  }

  /**
   * Returns a map of all the histograms of the image.
   *
   * @param image - the image
   * @return a map of all the histograms of the image
   */
  public static Map<String, List<Double>> getAllChannelsHistogram(BufferedImage image) {
    Map<String, List<Double>> histograms = new HashMap<>();
    histograms.put("red", getHistogram(image, "red"));
    histograms.put("green", getHistogram(image, "green"));
    histograms.put("blue", getHistogram(image, "blue"));
    histograms.put("intensity", getHistogram(image, "intensity"));
    return histograms;
  }
}
