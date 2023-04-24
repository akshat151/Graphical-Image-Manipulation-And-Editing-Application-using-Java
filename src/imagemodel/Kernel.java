package imagemodel;

import java.util.List;

/**
 * This class represents a kernel. This is a 2D array of integers. It is used to represent a filter
 * that can be applied to an image.
 */
public class Kernel {
  final List<List<Double>> filter;

  /**
   * This constructor creates a kernel from a list of lists of doubles.
   *
   * @param filter the list of lists of doubles that represent the kernel
   * @throws IllegalArgumentException if the kernel is not valid
   */
  public Kernel(List<List<Double>> filter) throws IllegalArgumentException {
    if ((filter.size() % 2) == 0) {
      throw new IllegalArgumentException("This is not a valid kernel");
    }
    int rows = filter.size();
    for (int i = 0; i < rows; i++) {
      if (filter.get(i).size() != rows) {
        throw new IllegalArgumentException("This is not a valid kernel");
      }
    }
    this.filter = filter;
  }
}
