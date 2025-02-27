package Application.MTCG.exceptions;

public class UserAlreadyExisting extends RuntimeException {
    public UserAlreadyExisting(String message) {
        super(message);
    }
}
