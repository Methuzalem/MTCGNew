package Application.MTCG.exceptions;

public class PermissionDenied extends RuntimeException {
  public PermissionDenied(String message) {
    super(message);
  }
}
