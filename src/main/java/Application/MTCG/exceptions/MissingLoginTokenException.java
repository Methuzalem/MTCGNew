package Application.MTCG.exceptions;

public class MissingLoginTokenException extends RuntimeException {
    public MissingLoginTokenException(String message) {
        super(message);
    }
}
