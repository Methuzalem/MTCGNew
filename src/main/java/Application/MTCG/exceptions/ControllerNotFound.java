package Application.MTCG.exceptions;

public class ControllerNotFound extends RuntimeException {
  public ControllerNotFound(String message) {
    super(message);
  }
}
