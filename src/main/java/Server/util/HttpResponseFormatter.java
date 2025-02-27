package Server.util;

import java.util.Map;
import Server.http.Response;

public class HttpResponseFormatter {

    // java http res obj to string
    public String format(Response response) {
        StringBuilder responseBuilder = new StringBuilder();

        if (null == response.getStatus()) {
            throw new NoHttpStatusException("Response does not contain a status");
        }

        responseBuilder
                .append("HTTP/1.1 ")
                .append(response.getStatus().getCode()).append(" ")
                .append(response.getStatus().getMessage())
                .append("\r\n");

        if (response.getBody() == null || response.getBody().isEmpty()) {
            response.setHeader("Content-Length", "0");
        } else {
            response.setHeader("Content-Length", "%s".formatted(response.getBody().length()));
        }

        for (Map.Entry<String, String> header: response.getHeaders().entrySet()) {
            responseBuilder.append(header.getKey()).append(": ").append(header.getValue()).append("\r\n");
        }

        if (response.getBody() == null || response.getBody().isEmpty()) {
            return responseBuilder.toString();
        }

        responseBuilder.append("\r\n");
        responseBuilder.append(response.getBody());

        return responseBuilder.toString();
    }
}

