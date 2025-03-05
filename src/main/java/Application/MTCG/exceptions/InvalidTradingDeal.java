package Application.MTCG.exceptions;

public class InvalidTradingDeal extends RuntimeException {
    public InvalidTradingDeal(String message) {
        super(message);
    }
}
