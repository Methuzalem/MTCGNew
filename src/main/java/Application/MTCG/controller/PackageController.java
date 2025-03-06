package Application.MTCG.controller;

import Application.MTCG.exceptions.MissingLoginTokenException;
import Application.MTCG.service.CardService;
import Application.MTCG.entity.Card;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

import Server.http.Request;
import Server.http.Response;
import Server.http.Method;
import Server.http.Status;

public class PackageController extends Controller {

    private final CardService cardService;

    public PackageController(CardService cardService) {
        this.cardService = cardService;
    }

    @Override
    public Response handle(Request request) {
        if (request.getMethod().equals(Method.POST)) {
            return create(request);
        }
        return json(Status.NOT_FOUND, "Couldn't handle HTTP request.");
    }

    public Response create(Request request) {
        try {
            String loginToken = getLoginToken(request);
            List<Card> cards = arrayFromBody(request.getBody(), new TypeReference<List<Card>>() {});
            cards = cardService.loadCardsInShop(loginToken, cards);
            return json(Status.CREATED, cards);
        } catch (MissingLoginTokenException e) {
            return json(Status.CONFLICT, e.getMessage());
        } catch (Exception e) {
            return json(Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
