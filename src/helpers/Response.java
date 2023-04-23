package helpers;

import java.awt.image.BufferedImage;

/**
 * This class represents the response from the controller to the view.
 * It is a simple wrapper class that contains a string.
 */
public class Response {
  private final String response;

  /**
   * Enum representing possible response types.
   */
  public enum ResponseType {
    ERROR,
    SUCCESS
  }

  private ResponseType responseType;

  private BufferedImage image;

  /**
   * Constructor for the Response class.
   *
   * @param resp         the response string
   * @param responseType the type of response
   */
  public Response(String resp, ResponseType responseType) {
    this.response = resp;
    this.responseType = responseType;
    this.image = null;
  }

  /**
   * Constructor for the Response class.
   *
   * @param resp         the response string
   * @param responseType the type of response
   * @param image        the image to be displayed
   */
  public Response(String resp, ResponseType responseType, BufferedImage image) {
    this.response = resp;
    this.responseType = responseType;
    this.image = image;
  }

  /**
   * Constructor for the Response class.
   *
   * @param resp the response string
   */
  public Response(String resp) {
    this.response = resp;
  }

  /**
   * Get the response string.
   *
   * @return the response string
   */
  public String getResponse() {
    return this.response;
  }

  /**
   * Get the response type.
   *
   * @return the response type
   */
  public ResponseType getResponseType() {
    return this.responseType;
  }

  /**
   * Get the image.
   *
   * @return the image
   */
  public BufferedImage getImage() {
    return this.image;
  }

  @Override
  public String toString() {
    return this.response + " " + this.responseType;
  }
}
