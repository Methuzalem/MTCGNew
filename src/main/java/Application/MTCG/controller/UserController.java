package Application.MTCG.controller;

import Application.MTCG.dto.CreateUserDTO;
import Application.MTCG.dto.UpdateUserDTO;
import Application.MTCG.entity.User;
import Application.MTCG.exceptions.InvalidUserData;
import Application.MTCG.exceptions.NullPointerException;
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
        } else if (request.getMethod().equals(Method.GET)) {
            return displayUserData(request);
        } else if (request.getMethod().equals(Method.PUT)) {
            return updateUserData(request);
        } else {
            return json(Status.NOT_FOUND, "Couldn't handle HTTP request.");
        }
    }

    private Response create(Request request) {
        try {
            User user = fromBody(request.getBody(), User.class);
            CreateUserDTO userDTO = userService.create(user);
            return json(Status.CREATED, userDTO);
        } catch (UserAlreadyExisting e) {
            return json(Status.CONFLICT, new HttpErrorResponse("User is already existing."));
        } catch (Exception e) {
            return json(Status.INTERNAL_SERVER_ERROR, new HttpErrorResponse(e.getMessage()));
        }
    }

    private Response displayUserData(Request request) {
        try {
            String loginToken = getLoginToken(request);
            String pathName = request.getPath();
            if (userService.matchTokenWithPath(loginToken, pathName)) {
                User user = userService.getUserByToken(loginToken);
                UpdateUserDTO update = userService.modelUpdateDTO(user);
                return json(Status.OK, update);
            } else {
                return json(Status.NOT_FOUND, new HttpErrorResponse("User data invalid."));
            }
        } catch (NullPointerException e) {
            return json(Status.CONFLICT, new HttpErrorResponse(e.getMessage()));
        } catch (InvalidUserData e) {
            return json(Status.BAD_REQUEST, new HttpErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return json(Status.INTERNAL_SERVER_ERROR, new HttpErrorResponse(e.getMessage()));
        }
    }

    public Response updateUserData(Request request) {
        try {
            String loginToken = getLoginToken(request);
            String pathName = request.getPath();
            if (userService.matchTokenWithPath(loginToken, pathName)) {
                User user = userService.getUserByToken(loginToken);
                UpdateUserDTO update = userService.modelUpdateDTOFromRequest(request);
                userService.updateUserNameBioImage(user, update);
                return json(Status.OK, null);
            } else {
                return json(Status.NOT_FOUND, new HttpErrorResponse("User data invalid."));
            }
        } catch (NullPointerException e) {
            return json(Status.CONFLICT, new HttpErrorResponse(e.getMessage()));
        } catch (InvalidUserData e) {
            return json(Status.BAD_REQUEST, new HttpErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return json(Status.INTERNAL_SERVER_ERROR, new HttpErrorResponse(e.getMessage()));
        }
    }
}
