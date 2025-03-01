package Application.MTCG.exceptions;

public class NotEnoughCoins extends RuntimeException {
    public NotEnoughCoins(String message) {
        super(message);
    }
}
