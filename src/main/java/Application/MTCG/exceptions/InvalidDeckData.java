package Application.MTCG.exceptions;

public class InvalidDeckData extends RuntimeException {
    public InvalidDeckData(String message) {
        super(message);
    }
}
