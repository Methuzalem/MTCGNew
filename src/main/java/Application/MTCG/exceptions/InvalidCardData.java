package Application.MTCG.exceptions;

public class InvalidCardData extends RuntimeException {
    public InvalidCardData(String message) {
        super(message);
    }
}
