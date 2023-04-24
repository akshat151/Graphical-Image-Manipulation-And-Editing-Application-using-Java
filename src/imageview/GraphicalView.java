package imageview;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import helpers.HistogramHelper;
import helpers.Response;
import imagecontroller.GraphicHelper;
import imagecontroller.GraphicalController;

import static javax.swing.JOptionPane.showMessageDialog;

/**
 * This class represents the graphical view for the Image Processing application.
 * It implements the View interface and extends JFrame.
 */
public class GraphicalView extends JFrame implements View {

  private final JButton load;
  private final JButton save;
  private final JButton undo;
  private final JButton combine;
  private final ImageCanvas imageCanvas;
  private final HistogramGraphPanel histogramGraphPanel;
  private JComboBox operationDrop;
  private String[] commands = {"Loading commands..."};


  /**
   * Constructor for the GraphicalView class.
   *
   * @param caption the caption to be displayed on the frame
   */
  public GraphicalView(String caption) {
    super(caption);
    setSize(1280, 820);
    setLocation(50, 50);
    this.getContentPane().setLayout(new GridBagLayout());

    GridBagConstraints lc = new GridBagConstraints();
    load = new JButton("Load");
    lc.weightx = 0.5;
    lc.fill = GridBagConstraints.HORIZONTAL;
    lc.gridx = 0;
    lc.gridy = 0;
    lc.gridwidth = 1;
    this.getContentPane().add(load, lc);

    GridBagConstraints sc = new GridBagConstraints();
    save = new JButton("Save");
    sc.fill = GridBagConstraints.HORIZONTAL;
    sc.weightx = 0.5;
    sc.gridx = 1;
    sc.gridy = 0;
    this.getContentPane().add(save, sc);

    GridBagConstraints ac = new GridBagConstraints();
    undo = new JButton("Undo");
    ac.fill = GridBagConstraints.HORIZONTAL;
    ac.weightx = 0.0;
    ac.gridx = 1;
    ac.gridy = 1;
    this.getContentPane().add(undo, ac);

    ImageCanvas img = createImage();
    this.imageCanvas = img;
    img.setPreferredSize(new Dimension(800, 800));
    ScrollPane scrollPane = new ScrollPane(ScrollPane.SCROLLBARS_AS_NEEDED);
    scrollPane.add(img);
    GridBagConstraints ic = new GridBagConstraints();
    ic.fill = GridBagConstraints.BOTH;
    ic.weightx = 0.5;
    ic.weighty = 1.0;
    ic.gridwidth = 2;
    ic.gridx = 0;
    ic.gridy = 2;
    this.getContentPane().add(scrollPane, ic);

    GridBagConstraints hc = new GridBagConstraints();
    // List of score with 0 of length 256
    //List<Double> scores = ;
    histogramGraphPanel = new HistogramGraphPanel();

    hc.fill = GridBagConstraints.BOTH;
    hc.anchor = GridBagConstraints.PAGE_END;
    hc.weightx = 0.5;
    hc.gridwidth = 1;
    hc.gridheight = 1;
    hc.gridx = 2;
    hc.gridy = 2;
    histogramGraphPanel.setPreferredSize(new Dimension(400, 400));
    this.getContentPane().add(histogramGraphPanel, hc);

    // Combine
    GridBagConstraints cc = new GridBagConstraints();
    combine = new JButton("RGB - Combine");
    cc.fill = GridBagConstraints.HORIZONTAL;
    cc.gridx = 2;
    cc.gridy = 0;
    this.getContentPane().add(combine, cc);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  private JComboBox createDropDown() {
    String[] operations = commands;

    //noinspection unchecked
    JComboBox operationList = new JComboBox(operations);
    operationList.setSelectedIndex(0);

    return operationList;
  }

  private ImageCanvas createImage() {
    BufferedImage image = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_RGB);
    // Set image to checkered pattern
    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        if ((x + y) % 2 == 0) {
          image.setRGB(x, y, Color.WHITE.getRGB());
        } else {
          image.setRGB(x, y, Color.BLACK.getRGB());
        }
      }
    }

    return new ImageCanvas(image);
  }

  /**
   * Sets up the view.
   *
   * @param commandSet the set of commands to be displayed
   */
  public void setup(Set<String> commandSet) {
    commandSet.remove("load");
    commandSet.remove("save");
    commandSet.remove("rgb-split");
    commandSet.remove("rgb-combine");
    commandSet.add("greyscale red-component");
    commandSet.add("greyscale green-component");
    commandSet.add("greyscale blue-component");
    commandSet.add("rgb-split red");
    commandSet.add("rgb-split green");
    commandSet.add("rgb-split blue");
    commandSet.add("mosaic");
    commands = new String[commandSet.size()];
    commands = commandSet.toArray(commands);

    // Sort the commands
    List<String> commandList = new ArrayList<>();
    Collections.addAll(commandList, commands);
    Collections.sort(commandList);
    commands = commandList.toArray(commands);

    GridBagConstraints oc = new GridBagConstraints();
    operationDrop = createDropDown();
    oc.fill = GridBagConstraints.HORIZONTAL;
    oc.weightx = 0.0;
    oc.gridx = 0;
    oc.gridy = 1;
    operationDrop.setActionCommand("DROPDOWN");
    this.getContentPane().add(operationDrop, oc);

    this.setResizable(false);
    //this.histogramCanvas.setVisible(true);
    this.histogramGraphPanel.setVisible(true);
    //this.pack();
    this.setVisible(true);
  }

  /**
   * Initializes the listeners for the view. This method should be called after the view is set up.
   *
   * @param controller appropriate controller being passed.
   */
  public void manageListeners(GraphicalController controller) {
    this.undo.addActionListener(e -> {
      GraphicHelper.undo(controller);
      try {
        GraphicHelper.getRecentImage(controller);
      } catch (IOException ex) {
        showMessageDialog(
                this,
                "Unable to get previous image"
        );
      }
    });
    this.load.addActionListener(e -> {
      JFileChooser chooser = new JFileChooser();
      chooser.setCurrentDirectory(new java.io.File("."));
      chooser.setDialogTitle("Choose File to Load");
      chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      chooser.setAcceptAllFileFilterUsed(false);
      chooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "ppm",
              "bmp"));

      File loadFile = null;

      if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
        loadFile = chooser.getSelectedFile();
      } else {
        System.out.println("No Selection ");
        showMessageDialog(this, "This is even shorter");
      }

      // check if file is valid
      if (loadFile != null && loadFile.exists() && loadFile.canRead()) {
        // Update the image on the controller stack
        try {
          GraphicHelper.loadImage(loadFile.getAbsolutePath(), controller);
        } catch (IOException ex) {
          showMessageDialog(
                  this,
                  "Unable to load Image : " + ex.getMessage()
          );
        }
      } else {
        // Throw a popup saying that the file is invalid
        System.out.println("Invalid Selection");
        showMessageDialog(this, "This is even shorter");
      }
    });
    this.save.addActionListener(e -> {
      JFileChooser chooser = new JFileChooser();
      chooser.setCurrentDirectory(new java.io.File("."));
      chooser.setDialogTitle("Choose File to Save");
      chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      chooser.setAcceptAllFileFilterUsed(false);

      if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {

        if (chooser.getSelectedFiles().length > 1) {
          showMessageDialog(this, "Please select only one file");
          return;
        }
        List<String> arguments = new ArrayList<>();
        arguments.add(chooser.getSelectedFile().getAbsolutePath());

        // Save the image
        controller.operationHandler("save", arguments);

      } else {
        System.out.println("No Selection ");
      }
    });
    this.operationDrop.addActionListener(e -> {
      String command = (String) operationDrop.getSelectedItem();
      List<String> arguments = new ArrayList<>();
      if (command.contains("greyscale")) {
        String[] arr = command.split(" ");
        if (arr.length > 1) {
          arguments.add(arr[1]);
          command = arr[0];
        }
      } else if (command.contains("rgb-split")) {
        String[] arr = command.split(" ");
        if (arr.length < 2) {
          command = arr[0];
        } else {
          command = arr[0];
          arguments.add(GraphicHelper.getRecentImageName(controller));
          switch (arr[1]) {
            case "red":
              arguments.addAll(Arrays.asList("red", "_", "_"));
              break;
            case "green":
              arguments.addAll(Arrays.asList("_", "green", "_"));
              break;
            case "blue":
              arguments.addAll(Arrays.asList("_", "_", "blue"));
              break;
            default:
              showMessageDialog(
                      this,
                      "Invalid color component for split"
              );
              return;
          }
        }
      } else if (command.contains("mosaic")) {
        arguments.add(showInputDialog());
      }
      try {
        Response r = controller.operationHandler(command, arguments);
        if (r.getResponseType() == Response.ResponseType.ERROR) {
          this.sendOutput(r);
        } else {
          GraphicHelper.getRecentImage(controller);
        }
      } catch (IOException ex) {
        showMessageDialog(
                this,
                "Unable to get recent image"
        );
      }
    });
    this.combine.addActionListener(e -> {
      // Operations to be done
      // 1. Open a File Chooser
      // 2. Get 3 files. Check if the number of files match. Else throw an error and ignore
      // 3. Run Combine Operation using the Controller
      // 4. Run getRecentImage

      JFileChooser chooser = new JFileChooser();
      chooser.setCurrentDirectory(new java.io.File("."));
      chooser.setDialogTitle("Choose files to combine");
      chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      chooser.setAcceptAllFileFilterUsed(false);
      chooser.setFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png", "ppm",
              "bmp"));

      chooser.setMultiSelectionEnabled(true);

      if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
        if (chooser.getSelectedFiles().length == 3) {
          try {
            File[] inputFiles = chooser.getSelectedFiles();

            // Generate name for the combined image as a combination of the basename of the images
            StringBuilder combinedName = new StringBuilder();
            for (int i = 0; i < inputFiles.length; i++) {
              combinedName.append(inputFiles[i].getName().split("\\.")[0]);
            }

            List<String> fileNames = new ArrayList<>();
            fileNames.add(combinedName.toString());

            for (int i = 0; i < inputFiles.length; i++) {
              fileNames.add(inputFiles[i].getAbsolutePath());
            }

            // Load into Image Store and get the names
            GraphicHelper.loadMultipleImages(fileNames.subList(1, fileNames.size()), controller);

            Response r = controller.operationHandler("rgb-combine", fileNames);

            System.out.println("Response in View : " + r.toString());

            System.out.println("OVER");

            if (r.getResponseType() == Response.ResponseType.ERROR) {
              this.sendOutput(r);
            } else {
              GraphicHelper.getRecentImage(controller);
            }
          } catch (IOException ex) {
            showMessageDialog(
                    this,
                    "Unable to load Image."
            );
            System.err.println("Unable to load Image : " + ex.getMessage());
          }
        } else {
          showMessageDialog(this, "Combine Operation needs exactly 3 images");
        }
      } else {
        System.out.println("No Selection ");
        showMessageDialog(this, "No Selection has been made");
      }
    });
  }

  @Override
  public List<String> getNextInput() {
    return null;
  }

  @Override
  public void sendOutput(Response response) throws IOException {
    if (response.getResponseType() == Response.ResponseType.SUCCESS) {
      this.imageCanvas.setImage(response.getImage());
      Graphics2D g = (Graphics2D) this.imageCanvas.getGraphics();
      Map<String, List<Double>> allChannelHistogram =
              HistogramHelper.getAllChannelsHistogram(response.getImage());
      this.histogramGraphPanel.setHistogram(allChannelHistogram);
      g.dispose();
      this.setVisible(true);
    } else {
      showMessageDialog(
              this,
              "Unable to perform operation:" + response.getResponse()
      );
    }
  }

  @Override
  public void clearView() {
    System.out.println("Unable to clear view at this moment");
  }

  private String showInputDialog() {
    String inputValue = JOptionPane.showInputDialog(this, "Enter number of Seeds",
            JOptionPane.INFORMATION_MESSAGE);
    if (inputValue != null) {
      return inputValue;
    }
    return "";
  }

}
