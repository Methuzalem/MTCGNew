package Server;

import Server.http.Response;
import Server.http.Request;

public interface Application {
    Response handle(Request request);
}
