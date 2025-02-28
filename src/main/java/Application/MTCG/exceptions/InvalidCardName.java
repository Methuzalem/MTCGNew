package Application.MTCG.exceptions;

public class InvalidCardName extends RuntimeException {
    public InvalidCardName(String message) {
        super(message);
    }
}
