package Application.MTCG.controller;

import Application.MTCG.entity.HttpErrorResponse;
import Application.MTCG.entity.User;
import Application.MTCG.entity.Card;
import Application.MTCG.exceptions.InvalidUserData;
import Application.MTCG.exceptions.MissingLoginTokenException;
import Application.MTCG.exceptions.NoCardsFound;
import Application.MTCG.service.CardService;
import Application.MTCG.service.UserService;

import Server.http.Request;
import Server.http.Response;
import Server.http.Method;
import Server.http.Status;

import java.util.List;

public class CardController extends Controller{
    private final CardService cardService;
    private final UserService userService;

    public CardController(CardService cardService, UserService userService) {
        this.cardService = cardService;
        this.userService = userService;
    }

    @Override
    public Response handle(Request request) {
        if (request.getMethod().equals(Method.GET)) {
            return displayUserCards(request);
        }
        return json(Status.NOT_FOUND, "Couldn't handle HTTP request.");
    }

    private Response displayUserCards(Request request) {
        try {
            String loginToken = getLoginToken(request);
            User user = userService.getUserByToken(loginToken);
            List<Card> cardsToDisplay = cardService.readCardsOfUser(user);
            return json(Status.OK, cardsToDisplay);
        } catch (InvalidUserData e) {
            return json(Status.CONFLICT, new HttpErrorResponse("The given User data is invalid"));
        } catch (MissingLoginTokenException e) {
            return json(Status.NOT_FOUND, new HttpErrorResponse("Missing login token"));
        } catch (NoCardsFound e) {
            return json(Status.NOT_FOUND, new HttpErrorResponse("The given User has no cards"));
        }
    }
}
