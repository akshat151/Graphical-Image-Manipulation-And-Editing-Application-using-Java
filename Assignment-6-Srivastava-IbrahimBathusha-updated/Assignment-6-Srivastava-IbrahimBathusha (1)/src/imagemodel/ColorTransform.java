package imagemodel;

import java.util.List;

/**
 * This class represents a color transform.
 */
public class ColorTransform {
  final List<List<Double>> transform;

  /**
   * This constructor creates a color transform from a list of lists of doubles.
   * @param transform the list of lists of doubles that represent the color transform
   * @throws IllegalArgumentException if the list of lists of doubles is not a valid color transform
   *     kernel
   */
  public ColorTransform(List<List<Double>> transform) throws IllegalArgumentException {
    if (transform.size() != transform.get(0).size() || transform.size() != 3) {
      throw new IllegalArgumentException("This is not a valid color transform");
    }
    this.transform = transform;
  }
}