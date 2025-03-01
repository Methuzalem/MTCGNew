package Application.MTCG.controller;

import Application.MTCG.dto.CreateUserDTO;
import Application.MTCG.entity.User;
import Application.MTCG.exceptions.UserAlreadyExisting;
import Application.MTCG.service.UserService;
import Application.MTCG.entity.HttpErrorResponse;

import Server.http.Request;
import Server.http.Response;
import Server.http.Method;
import Server.http.Status;

public class UserController extends Controller {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Response handle(Request request) {
        if (request.getMethod().equals(Method.POST)) {
            return create(request);
        } else {
            return json(Status.NOT_FOUND, "Couldn't handle HTTP request.");
        }
    }

    private Response create(Request request) {
        try{
            User user = fromBody(request.getBody(), User.class);
            CreateUserDTO userDTO = userService.create(user);
            return json(Status.CREATED, userDTO);
        } catch (UserAlreadyExisting e) {
            return json(Status.CONFLICT, new HttpErrorResponse("User is already existing."));
        } catch (Exception e) {
            return json(Status.INTERNAL_SERVER_ERROR, new HttpErrorResponse(e.getMessage()));
        }
    }
}
