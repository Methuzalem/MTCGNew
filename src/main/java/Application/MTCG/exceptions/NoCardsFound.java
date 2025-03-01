package Application.MTCG.exceptions;

public class NoCardsFound extends RuntimeException {
    public NoCardsFound(String message) {
        super(message);
    }
}
