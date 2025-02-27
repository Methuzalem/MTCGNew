package Application.MTCG.exceptions;

public class JsonParserException extends RuntimeException {
    public JsonParserException(String message) {
        super(message);
    }

    public JsonParserException(Throwable cause) {
        super(cause);
    }
}
