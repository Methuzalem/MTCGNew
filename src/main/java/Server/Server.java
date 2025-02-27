package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    // port opening

    private final Application application;

    private ServerSocket serverSocket;

    public Server(Application application) {
        this.application = application;
    }

    public void start() {
        try {
            this.serverSocket = new ServerSocket(10001);
            System.out.printf("Server started\n");
            System.out.printf("Listening on port: %s\n", serverSocket.getLocalPort());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while (true) {
            try {
                Socket socket = this.serverSocket.accept();

                RequestHandler requestHandler = new RequestHandler(
                        socket,
                        this.application
                );

                requestHandler.handle();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
