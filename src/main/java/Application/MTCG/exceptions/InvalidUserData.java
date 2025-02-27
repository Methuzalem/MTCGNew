package Application.MTCG.exceptions;

public class InvalidUserData extends RuntimeException {
    public InvalidUserData(String message) {
        super(message);
    }
}
