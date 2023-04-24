package imageview;

import java.io.IOException;
import java.util.List;

import helpers.Response;

/**
 * This interface represents the view for the Image Processing application. It is responsible for
 * displaying information to the user and getting the input from the user. It is implemented by
 * the TerminalView class for the text input modality.
 */
public interface View {

  /**
   * Gets the input from the user, performs basic input parsing, and structures it for the
   * controller.
   *
   * @throws IOException if there is an error writing to the output modality.
   */
  List<String> getNextInput() throws IOException;

  /**
   * Sends the Output contained in the Response object to the output modality.
   *
   * @param response Response object containing the output to be sent to the user.
   * @throws IOException if there is an error writing to the output modality.
   */
  void sendOutput(Response response) throws IOException;

  /**
   * Clears the view.
   */
  void clearView();
}
