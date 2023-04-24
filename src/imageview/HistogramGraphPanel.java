package imageview;

import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.RenderingHints;
import java.awt.Point;
import java.awt.FontMetrics;
import java.awt.Font;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

/**
 * This class represents the panel that displays the histogram of the image. It extends the JPanel
 * class.
 */
public class HistogramGraphPanel extends JPanel {

  private static final Stroke GRAPH_STROKE = new BasicStroke(1f);
  private final Color gridColor = new Color(200, 200, 200, 200);
  private final int pointWidth = 4;
  private Map<String, List<Double>> histogram;

  /**
   * Constructor for the HistogramGraphPanel class.
   */
  public HistogramGraphPanel() {
    this.histogram = new HashMap<>();
    this.histogram.put("red", new ArrayList<>(Collections.nCopies(256, (double) 0)));
    this.histogram.put("green", new ArrayList<>(Collections.nCopies(256, (double) 0)));
    this.histogram.put("blue", new ArrayList<>(Collections.nCopies(256, (double) 0)));
    this.histogram.put("intensity", new ArrayList<>(Collections.nCopies(256, (double) 0)));
  }

  /**
   * Constructor for the HistogramGraphPanel class.
   *
   * @param histogram the histogram to be displayed at the start of the program
   */
  public HistogramGraphPanel(Map<String, List<Double>> histogram) {
    this.histogram = histogram;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    double allChannelMax = Math.max(getMaxHistogram("red"),
            Math.max(getMaxHistogram("green"), getMaxHistogram("blue")));
    double allChannelMin = Math.min(getMinHistogram("red"),
            Math.min(getMinHistogram("green"), getMinHistogram("blue")));

    int padding = 40;
    int labelPadding = 40;
    double xScale = ((double) getWidth() - (2 * padding) - labelPadding)
            / (this.histogram.get("red").size() - 1);
    double yScale = ((double) getHeight() - 2 * padding - labelPadding)
            / (allChannelMax - allChannelMin);

    // Red Channel Histogram
    List<Point> redGraphPoints = new ArrayList<>();
    for (int i = 0; i < this.histogram.get("red").size(); i++) {
      int x1 = (int) (i * xScale + padding + labelPadding);
      int y1 = (int) ((allChannelMax - this.histogram.get("red").get(i)) * yScale + padding);
      redGraphPoints.add(new Point(x1, y1));
    }

    // Green Channel Histogram
    List<Point> greenGraphPoints = new ArrayList<>();
    for (int i = 0; i < this.histogram.get("green").size(); i++) {
      int x1 = (int) (i * xScale + padding + labelPadding);
      int y1 = (int) ((allChannelMax - this.histogram.get("green").get(i)) * yScale + padding);
      greenGraphPoints.add(new Point(x1, y1));
    }

    // Blue Channel Histogram
    List<Point> blueGraphPoints = new ArrayList<>();
    for (int i = 0; i < this.histogram.get("blue").size(); i++) {
      int x1 = (int) (i * xScale + padding + labelPadding);
      int y1 = (int) ((allChannelMax - this.histogram.get("blue").get(i)) * yScale + padding);
      blueGraphPoints.add(new Point(x1, y1));
    }

    // Intensity Histogram
    List<Point> intensityGraphPoints = new ArrayList<>();
    for (int i = 0; i < this.histogram.get("intensity").size(); i++) {
      int x1 = (int) (i * xScale + padding + labelPadding);
      int y1 = (int) ((allChannelMax - this.histogram.get("intensity").get(i)) * yScale + padding);
      intensityGraphPoints.add(new Point(x1, y1));
    }

    // draw white background
    g2.setColor(Color.WHITE);
    g2.fillRect(padding + labelPadding, padding, getWidth() - (2 * padding) - labelPadding,
            getHeight() - 2 * padding - labelPadding);
    g2.setColor(Color.BLACK);

    // create hatch marks and grid lines for y-axis.
    int numberYDivisions = 10;
    for (int i = 0; i < numberYDivisions + 1; i++) {
      int x0 = padding + labelPadding;
      int x1 = pointWidth + padding + labelPadding;
      int y0 = getHeight() - ((i * (getHeight() - padding * 2 - labelPadding)) /
              numberYDivisions + padding + labelPadding);
      if (this.histogram.get("red").size() > 0) {
        g2.setColor(gridColor);
        g2.drawLine(padding + labelPadding + 1 + pointWidth, y0, getWidth() - padding, y0);
        g2.setColor(Color.BLACK);
        String yLabel = (int) (((allChannelMin + (allChannelMax - allChannelMin) *
                ((i * 1.0) / numberYDivisions)) * 100)) / 100.0 + "";
        FontMetrics metrics = g2.getFontMetrics();
        int labelWidth = metrics.stringWidth(yLabel);
        g2.drawString(yLabel, x0 - labelWidth - 5, y0 + (metrics.getHeight() / 2) - 3);
      }
      g2.drawLine(x0, y0, x1, y0);
    }

    // and for x-axis
    for (int i = 0; i < this.histogram.get("red").size(); i++) {
      if (this.histogram.get("red").size() > 1) {
        int x0 = i * (getWidth() - padding * 2 - labelPadding) /
                (this.histogram.get("red").size() - 1) + padding + labelPadding;
        int y0 = getHeight() - padding - labelPadding;
        int y1 = y0 - pointWidth;
        if ((i % ((int) ((this.histogram.get("red").size() / 20.0)) + 1)) == 0) {
          g2.setColor(gridColor);
          g2.drawLine(x0, getHeight() - padding - labelPadding - 1 - pointWidth, x0, padding);
          g2.setColor(Color.BLACK);
          String xLabel = i + "";
          FontMetrics metrics = g2.getFontMetrics();
          int labelWidth = metrics.stringWidth(xLabel);
          g2.drawString(xLabel, x0 - labelWidth / 2, y0 + metrics.getHeight() + 3);
        }
        g2.drawLine(x0, y0, x0, y1);
      }
    }

    // create x and y axes
    g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding,
            padding + labelPadding, padding);
    g2.drawLine(padding + labelPadding, getHeight() - padding - labelPadding,
            getWidth() - padding, getHeight() - padding - labelPadding);


    // Red Channel Histogram
    g2.setColor(Color.red);
    plotGraphLines(g2, redGraphPoints);
    plotGraphPoints(g2, redGraphPoints, g2.getStroke());

    // Green Channel Histogram
    g2.setColor(Color.green);
    plotGraphLines(g2, greenGraphPoints);
    plotGraphPoints(g2, greenGraphPoints, g2.getStroke());

    // Blue Channel Histogram
    g2.setColor(Color.blue);
    plotGraphLines(g2, blueGraphPoints);
    plotGraphPoints(g2, blueGraphPoints, g2.getStroke());

    // Intensity Channel Histogram
    g2.setColor(Color.black);
    plotGraphLines(g2, intensityGraphPoints);
    plotGraphPoints(g2, intensityGraphPoints, g2.getStroke());

    // Red Channel Points

    // Green Channel Points

    // Blue Channel Points

    // Intensity Channel Points


    // Draw legend in the top left corner of the graph and not the panel
    int yPad = pointWidth + padding + labelPadding;
    int xPad = padding + labelPadding;
    g2.setColor(Color.red);
    g2.drawString("Red", 10 + xPad, 10 + yPad);
    g2.setColor(Color.green);
    g2.drawString("Green", 10 + xPad, 25 + yPad);
    g2.setColor(Color.blue);
    g2.drawString("Blue", 10 + xPad, 40 + yPad);
    g2.setColor(Color.black);
    g2.drawString("Intensity", 10 + xPad, 55 + yPad);

    // X-axis and Y-axis labels
    g2.setColor(Color.BLACK);
    g2.setFont(new Font("Arial", Font.BOLD, 14));
    g2.drawString("Freq", 10, getHeight() / 2 - 30);

    g2.drawString("Value", getWidth() / 2 - 40, getHeight() - 30);
  }

  private void plotGraphPoints(Graphics2D g2, List<Point> graphPoints, Stroke oldStroke) {
    // Red Channel Points
    g2.setStroke(oldStroke);
    for (int i = 0; i < graphPoints.size(); i++) {
      int x = graphPoints.get(i).x - pointWidth / 2;
      int y = graphPoints.get(i).y - pointWidth / 2;
      g2.fillOval(x, y, pointWidth, pointWidth);
    }
  }

  private void plotGraphLines(Graphics2D g2, List<Point> graphPoints) {
    g2.setStroke(GRAPH_STROKE);
    for (int i = 0; i < graphPoints.size() - 1; i++) {
      int x1 = graphPoints.get(i).x;
      int y1 = graphPoints.get(i).y;
      int x2 = graphPoints.get(i + 1).x;
      int y2 = graphPoints.get(i + 1).y;
      g2.drawLine(x1, y1, x2, y2);
    }
  }

  private double getMinHistogram(String channel) {
    if (channel == null) {
      return getMinHistogram("red");
    }

    double minScore = Double.MAX_VALUE;
    for (Double score : this.histogram.get(channel)) {
      minScore = Math.min(minScore, score);
    }
    return minScore;
  }

  private double getMaxHistogram(String channel) {
    if (channel == null) {
      return getMaxHistogram("red");
    }
    double maxScore = Double.MIN_VALUE;
    for (Double score : this.histogram.get(channel)) {
      maxScore = Math.max(maxScore, score);
    }
    return maxScore;
  }

  /**
   * Sets the histogram to be displayed in the graph.
   */
  public void setHistogram(Map<String, List<Double>> histogram) {
    this.histogram = histogram;
    invalidate();
    this.repaint();
  }
}
