package Application.MTCG.controller;

import Application.MTCG.entity.Card;
import Application.MTCG.exceptions.InvalidUserData;
import Application.MTCG.service.DeckService;

import Server.http.Request;
import Server.http.Response;
import Server.http.Method;
import Server.http.Status;

import java.util.List;

public class DeckController extends Controller{
    private final DeckService deckService;

    public DeckController(DeckService deckService) {
        this.deckService = deckService;
    }

    @Override
    public Response handle(Request request) {
        if(request.getMethod().equals(Method.GET)){
            return displayDeck(request);
        }
        return json(Status.NOT_FOUND, "Couldn't handle HTTP request");
    }

    private Response displayDeck(Request request){
        try {
            String loginToken = getLoginToken(request);
            List<Card> cardsToDisplay = deckService.displayDeckCardsOfUser(loginToken);
            return json(Status.OK, cardsToDisplay);
        } catch (InvalidUserData e) {
            return json(Status.NOT_FOUND, e);
        } catch (Exception e) {
            return json(Status.INTERNAL_SERVER_ERROR, e);
        }
    }
}
