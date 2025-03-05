package Server;

import java.io.IOException;
import java.net.Socket;
import java.time.LocalDateTime;
import Server.http.Request;
import Server.http.Response;
import Server.util.HttpRequestParser;
import Server.util.HttpResponseFormatter;
import Server.util.HttpSocket;

public class RequestHandler implements Runnable {

    private final Socket socket;
    private final Application application;

    public RequestHandler(
            Socket socket,
            Application application
    ) {
        this.socket = socket;
        this.application = application;
    }

    @Override
    public void run() {
        this.handle();
    }

    public void handle() {
        HttpRequestParser httpRequestParser = new HttpRequestParser();
        HttpResponseFormatter httpResponseFormatter = new HttpResponseFormatter();

        try (HttpSocket httpSocket = new HttpSocket(this.socket)) {
            String http = httpSocket.read();
            Request request = httpRequestParser.parse(http);

            System.out.printf(
                    "%s %s %s\n", LocalDateTime.now() , request.getMethod(), request.getPath()
            );

            Response response = this.application.handle(request);

            http = httpResponseFormatter.format(response);
            httpSocket.write(http);
        } catch (IOException e) {

            // send standard error response

            throw new RuntimeException(e);
        }
    }
}

