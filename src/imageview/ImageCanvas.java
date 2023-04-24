package imageview;

import java.awt.Canvas;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * This class represents the canvas that displaysthe image. It extends Canvas class.
 */
public class ImageCanvas extends Canvas {

  private BufferedImage image;
  private Graphics2D graphics;

  /**
   * Creates an object on type ImageCanvas.
   *
   * @param image BufferedImage to be displayed.
   */
  public ImageCanvas(BufferedImage image) {
    this.image = image;
  }

  /**
   * sets the canvas to display an image.
   *
   * @param image BufferedImage to be displayed.
   */
  public void setImage(BufferedImage image) {
    this.image = image;
    // Force a repaint
    repaint();
  }

  @Override
  public void update(Graphics g) {
    graphics = (Graphics2D) g;
    clear();
    paint(g);
  }

  /**
   * Clears the canvas.
   */
  public void clear() {
    graphics.clearRect(0, 0, this.getWidth(), this.getHeight());
  }

  @Override
  public void paint(Graphics g) {
    g.drawImage(image, 0, 0, this);
  }
}