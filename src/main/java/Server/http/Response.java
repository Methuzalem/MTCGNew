package Server.http;

import java.util.HashMap;
import java.util.Map;

public class Response {

    private Status status;

    private final Map<String, String> headers;

    private String body;

    public Response() {
        this.headers = new HashMap<>();
    }

    public String getHeader(String name) {
        return this.headers.get(name);
    }

    public void setHeader(String name, String value) {
        this.headers.put(name, value);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
