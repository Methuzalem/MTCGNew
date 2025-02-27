package Server.http;

import java.util.HashMap;
import java.util.Map;

public class Request {

    private Method method;

    private String path;

    private final Map<String, String> header;

    private String body;

    private String http;

    public Method getMethod() {
        return method;
    }

    public Request() {
        this.header = new HashMap<>();
    }

    public String getHeader(String name) {
        return this.header.get(name);
    }

    public void setHeader(String name, String value) {
        this.header.put(name, value);
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getHttp() {
        return http;
    }

    public void setHttp(String http) {
        this.http = http;
    }
}

