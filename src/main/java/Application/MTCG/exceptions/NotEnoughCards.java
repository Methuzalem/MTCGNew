package Application.MTCG.exceptions;

public class NotEnoughCards extends RuntimeException {
    public NotEnoughCards(String message) {
        super(message);
    }
}
