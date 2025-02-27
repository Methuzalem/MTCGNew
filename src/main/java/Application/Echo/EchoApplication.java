package Application.Echo;


import Server.Application;
import Server.http.Request;
import Server.http.Response;
import Server.http.Status;

public class EchoApplication implements Application {

    @Override
    public Response handle(Request request) {

        Response response = new Response();
        response.setStatus(Status.OK);
        response.setHeader("Content-Type", "text/plain");
        response.setBody(request.getHttp());

        return response;
    }
}

