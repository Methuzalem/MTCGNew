package Application.MTCG.entity;

public class HttpErrorResponse {
    private final String error;

    public HttpErrorResponse(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
