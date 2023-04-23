package imageview;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.List;

import helpers.Response;

import static org.junit.Assert.assertEquals;

/**
 * Test class for TerminalView. This class tests the basic framework of the TerminalView class.
 */
public class TerminalViewTest {

  /**
   * This class represents a readable input stream. It is used to test the TerminalView class.
   */
  public static class ReadableInputStream extends InputStream {
    protected String input;
    protected int index;

    public ReadableInputStream(String input) {
      this.input = input;
      this.index = 0;
    }

    @Override
    public int read() throws IOException {
      if (index >= input.length()) {
        return -1;
      }

      return input.charAt(index++);
    }

    @Override
    public int available() throws IOException {
      return input.length() - index;
    }

    @Override
    public String toString() {
      return input + " " + index;
    }
  }

  /**
   * This class represents a writable output stream. It is used to test the TerminalView class.
   */
  public static class WritableOutputStream extends OutputStream {
    private String output;

    public WritableOutputStream() {
      this.output = "";
    }

    @Override
    public void write(int b) throws IOException {
      output += (char) b;
    }

    public String getOutput() {
      return output;
    }
  }

  @Test
  public void basicFrameWorkTest() {
    int a = 1;
    int b = 2;

    assertEquals(3, a + b);
  }

  @Test
  public void InputTest() throws IOException {
    String input = "load test.txt\n";
    ReadableInputStream in = new ReadableInputStream(input);
    WritableOutputStream out = new WritableOutputStream();
    TerminalView view = new TerminalView(in, out);
    List<String> parsedInput = view.getNextInput();
    assertEquals("load", parsedInput.get(0));
    assertEquals("test.txt", parsedInput.get(1));
  }

  @Test
  public void OutputTest() throws IOException {
    String input = "load test.txt\n";
    ReadableInputStream in = new ReadableInputStream(input);
    WritableOutputStream out = new WritableOutputStream();
    TerminalView view = new TerminalView(in, out);
    Response response = new Response("test");
    view.sendOutput(response);
    assertEquals("test", out.getOutput());
  }
}
