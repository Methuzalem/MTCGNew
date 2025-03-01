package Application.MTCG.controller;

import Application.MTCG.entity.HttpErrorResponse;
import Application.MTCG.exceptions.InvalidUserData;
import Application.MTCG.exceptions.NotEnoughCards;
import Application.MTCG.exceptions.NotEnoughCoins;
import Application.MTCG.service.CardService;
import Application.MTCG.entity.Card;
import Application.MTCG.service.UserService;

import Server.http.Request;
import Server.http.Response;
import Server.http.Method;
import Server.http.Status;

import java.util.List;

public class TransactionController extends Controller {
    CardService cardService;
    UserService userService;

    public TransactionController(CardService cardService, UserService userservice) {
        this.cardService = cardService;
        this.userService = userservice;
    }

    @Override
    public Response handle(Request request) {
        if (request.getMethod().equals(Method.POST)) {
            return modelPackage(request);
        }
        return json(Status.NOT_FOUND, "Endpoint not found");
    }

    public Response modelPackage(Request request) {
        try {
            String loginToken = getLoginToken(request);
            List<Card> packageCards = cardService.checkCoinsAquirePackage(loginToken);
            return json(Status.CREATED, packageCards);
        } catch (NotEnoughCoins e) {
            return json(Status.BAD_REQUEST_MONEY, new HttpErrorResponse(e.getMessage()));
        } catch (NotEnoughCards e) {
            return json(Status.BAD_REQUEST_CARD, new HttpErrorResponse(e.getMessage()));
        } catch (InvalidUserData e) {
            return json(Status.CONFLICT, new HttpErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return json(Status.INTERNAL_SERVER_ERROR, new HttpErrorResponse(e.getMessage()));
        }
    }
}






