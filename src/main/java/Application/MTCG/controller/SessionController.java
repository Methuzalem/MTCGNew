package Application.MTCG.controller;

import Application.MTCG.dto.CreateTokenDTO;
import Application.MTCG.entity.HttpErrorResponse;
import Application.MTCG.entity.TokenAuthenticator;
import Application.MTCG.repositorys.UserRepo;
import Application.MTCG.service.SessionService;
import Application.MTCG.exceptions.InvalidUserData;

import Server.http.Request;
import Server.http.Response;
import Server.http.Method;
import Server.http.Status;

public class SessionController extends Controller {

    private final SessionService sessionService;

    public SessionController(UserRepo userRepo) {
        this.sessionService = new SessionService(userRepo);
    }

    @Override
    public Response handle(Request request) {
        if (request.getMethod() == Method.POST) {
            return authenticate(request);
        } else {
            return json(Status.NOT_FOUND, "Couldn't handle HTTP request.");
        }
    }

    private Response authenticate(Request request) {
        try {
            TokenAuthenticator authenticator = fromBody(request.getBody(), TokenAuthenticator.class);
            CreateTokenDTO newToken = sessionService.createTokenInitializeStats(authenticator);
            return json(Status.OK, newToken);
        } catch (InvalidUserData e) {
            return json(Status.CONFLICT, new HttpErrorResponse("The given User data is invalid"));
        }
    }
}
