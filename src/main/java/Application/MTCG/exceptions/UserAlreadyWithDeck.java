package Application.MTCG.exceptions;

public class UserAlreadyWithDeck extends RuntimeException {
    public UserAlreadyWithDeck(String message) {
        super(message);
    }
}
