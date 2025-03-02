package Server.http;

public enum Status {

    OK(200, "OK"),
    CREATED(201, "CREATED"),
    NO_CONTENT(204, "No Content"),
    NOT_FOUND(404, "Not Found"),
    CONFLICT(409, "Conflict"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    BAD_REQUEST(400, "Bad Request"),
    BAD_REQUEST_MONEY(400, "Bad Request Money Problem"),
    BAD_REQUEST_CARD(400, "Bad Request Card Problem");

    private final int code;
    private final String message;

    Status(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}

