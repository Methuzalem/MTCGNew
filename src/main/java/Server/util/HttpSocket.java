package Server.util;

import java.io.*;
import java.net.Socket;

public class HttpSocket implements Closeable {

    private final Socket socket;
    private final BufferedReader reader;
    private final BufferedWriter writer;

    public HttpSocket(Socket socket) throws IOException {
        this.socket = socket;

        this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public String read() throws IOException {
        StringBuilder requestBuilder = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            requestBuilder.append(line).append("\r\n");
        }
        requestBuilder.append("\r\n");

        int contentLength = 0;
        String[] headers = requestBuilder.toString().split("\r\n");
        for (String header : headers) {
            if (header.startsWith("Content-Length:")) {
                contentLength = Integer.parseInt(header.split(":")[1].trim());
                break;
            }
        }

        if (0 == contentLength) {
            return requestBuilder.toString();
        }

        char[] body = new char[contentLength];
        reader.read(body, 0, contentLength);
        requestBuilder.append(new String(body));

        return requestBuilder.toString();
    }

    public void write(String http) throws IOException {
        writer.write(http);
        writer.flush();
    }

    @Override
    public void close() throws IOException {
        reader.close();
        writer.close();
        socket.close();
    }
}

