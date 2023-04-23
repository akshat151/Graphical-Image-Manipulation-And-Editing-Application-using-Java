package imageview;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import helpers.Response;

/**
 * This class represents the view for the Image Processing application via the terminal.
 * It is responsible for displaying information to the user and getting the input from the user.
 * It implements the ImageViewInterface.
 */
public class TerminalView implements View {
  private final InputStream in;
  private final OutputStream out;

  /**
   * Parameterized Constructor for the TerminalView class.
   * @param in the input stream to read from.
   * @param out the output stream to write to.
   */
  public TerminalView(InputStream in, OutputStream out) {
    this.in = in;
    this.out = out;
  }

  @Override
  public List<String> getNextInput() throws IOException {

    // Read from the input line from the input stream
    String input = "";
    int nextByte = in.read();
    while (nextByte != '\n') {
      input += (char) nextByte;
      nextByte = in.read();
    }

    // Strip the leading and trailing whitespaces. An empty string on its own line is causing
    // problems in the controller.
    input = input.trim();

    if (input.equals("")) {
      return new ArrayList<String>();
    }

    // Parse the input into a list of strings
    ArrayList<String> parsedInput = new ArrayList<>();
    String[] inputArray = input.split(" ");
    Collections.addAll(parsedInput, inputArray);

    // Return the parsed input
    return parsedInput;
  }

  @Override
  public void sendOutput(Response response) throws IOException {
    out.write(response.getResponse().getBytes());
  }

  @Override
  public void clearView() {
    try {
      out.write("\033[H\033[2J".getBytes());
    } catch (IOException e) {
      // Do Nothing. This is a best effort attempt to clear the screen.
    }
  }
}
