package Server.util;

import Server.http.Method;
import Server.http.Request;

public class HttpRequestParser {

    // http req string to java obj
    public Request parse(String http) {
        Request request = new Request();
        request.setHttp(http);

        String[] lines = http.split("\r\n");
        String requestLine = lines[0];
        String[] requestLineParts = requestLine.split(" ");

        request.setMethod(Method.valueOf(requestLineParts[0]));
        request.setPath(requestLineParts[1]);

        // parse headers
        int emptyLine = 0;
        for (int i = 1; i < lines.length; i++) {
            String line = lines[i];

            if (line.isEmpty()) {
                emptyLine = i;
                break;
            }

            String[] headerParts = line.split(":", 2);
            request.setHeader(headerParts[0], headerParts[1].trim());
        }

        if (emptyLine == 0 || lines.length - 1 == emptyLine) {
            return request;
        }

        // parse body
        StringBuilder bodyBuilder = new StringBuilder();
        for (int i = emptyLine + 1; i < lines.length; i++) {
            bodyBuilder.append(lines[i]);

            if (lines.length - 1 != i) {
                bodyBuilder.append("\r\n");
            }
        }

        request.setBody(bodyBuilder.toString());

        return request;
    }
}

