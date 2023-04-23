package imagecontroller;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import helpers.ImageImplHelper;
import helpers.Response;
import imagecontroller.savers.GenericSaver;
import imagemodel.ImageStore;
import imageview.GraphicalView;

/**
 * This class is used to handle the operations that are performed on the image. It extends the
 * AbstractController class.
 */
public class GraphicalController extends AbstractController {

  private final GraphicalView view;

  /**
   * Constructor for the GraphicalController class.
   *
   * @param view       the view
   * @param imgHelper  the image helper
   * @param imageStore the image store
   */
  public GraphicalController(GraphicalView view, ImageImplHelper imgHelper, ImageStore imageStore) {
    super(imgHelper, imageStore);
    this.view = view;
  }

  @Override
  public Response operationHandler(String command, List<String> arguments) {
    Response operationResp = super.operationHandler(command, arguments);
    if (command.equalsIgnoreCase("rgb-split")) {
      this.imageStore.getStack().push(arguments.get(1));
    }
    if (operationResp.getResponseType() == Response.ResponseType.SUCCESS) {
      if (command.equalsIgnoreCase("rgb-combine")) {
        this.imageStore.getStack().push(arguments.get(0));
        operationResp = new Response(
                "Image loaded successfully.",
                Response.ResponseType.SUCCESS,
                GenericSaver.getBufferedImage(this.imageStore.getImages().get(arguments.get(0))
                ));
        try {
          this.getRecentImage();
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    }

    return operationResp;
  }

  @Override
  public void beginApp() {
    Set<String> commands = new HashSet<>(this.knownCommands.keySet());
    this.view.setup(commands);
    this.view.manageListeners(this);
  }

  protected void loadImage(String imageFile) throws IOException {
    this.imageStore.getImages().put(imageFile, this.imgHelper.loadImage(imageFile));
    this.imageStore.getStack().push(imageFile);
    this.view.sendOutput(
            new Response(
                    "Image loaded successfully.",
                    Response.ResponseType.SUCCESS,
                    GenericSaver.getBufferedImage(this.imageStore.getImages().get(imageFile))
            ));
  }

  protected void getRecentImage() throws IOException {
    String current = this.imageStore.getStack().peek();
    this.view.sendOutput(
            new Response(
                    "Image update.",
                    Response.ResponseType.SUCCESS,
                    GenericSaver.getBufferedImage(this.imageStore.getImages().get(current))
            ));
  }

  protected void undo() {
    if (this.imageStore.getStack().size() > 1) {
      imageStore.getStack().pop();
    }
  }
}
