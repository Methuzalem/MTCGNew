package Application.MTCG.controller;

import Application.MTCG.entity.Card;
import Application.MTCG.exceptions.InvalidUserData;
import Application.MTCG.exceptions.UserAlreadyWithDeck;
import Application.MTCG.service.DeckService;
import Application.MTCG.entity.Deck;
import Application.MTCG.entity.HttpErrorResponse;
import Application.MTCG.exceptions.InvalidDeckData;

import Server.http.Request;
import Server.http.Response;
import Server.http.Method;
import Server.http.Status;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;

public class DeckController extends Controller {
    private final DeckService deckService;

    public DeckController(DeckService deckService) {
        this.deckService = deckService;
    }

    @Override
    public Response handle(Request request) {
        if(request.getMethod() == Method.GET && request.getPath().contains("plain")) {
            return displayPlainDeck(request);
        } else if (request.getMethod().equals(Method.GET)) {
            return displayDeck(request);
        } else if (request.getMethod().equals(Method.PUT)) {
            return configureDeck(request);
        }
        return json(Status.NOT_FOUND, "Couldn't handle HTTP request");
    }

    private Response displayDeck(Request request) {
        try {
            String loginToken = getLoginToken(request);
            List<Card> cardsToDisplay = deckService.getDeckCardsOfUser(loginToken);
            return json(Status.OK, cardsToDisplay);
        } catch (InvalidUserData e) {
            return json(Status.NOT_FOUND, e);
        } catch (Exception e) {
            return json(Status.INTERNAL_SERVER_ERROR, e);
        }
    }

    private Response configureDeck(Request request) {
        try {
            String loginToken = getLoginToken(request);
            List<String> cardIds = arrayFromBody(request.getBody(), new TypeReference<List<String>>() {});
            if (cardIds.size() != 4) {
                throw new InvalidDeckData("Deck size must be 4");
            }
            Deck newDeck = deckService.createDeck(loginToken);
            deckService.configureDeckByTokenAndUserId(newDeck, cardIds);
            return json(Status.OK, null);
        } catch (InvalidDeckData e) {
            return json(Status.BAD_REQUEST, new HttpErrorResponse(e.getMessage()));
        } catch (UserAlreadyWithDeck e) {
            return json(Status.CONFLICT, new HttpErrorResponse(e.getMessage()));
        }
    }

    private Response displayPlainDeck(Request request) {
        try {
            String loginToken = getLoginToken(request);
            String plainText;
            List<Card> cardsToDisplay = deckService.getDeckCardsOfUser(loginToken);
            plainText = deckService.convertDeckToPlainText(cardsToDisplay);
            return text(Status.OK, plainText);
        } catch (InvalidUserData e) {
            return json(Status.NOT_FOUND, new HttpErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return json(Status.INTERNAL_SERVER_ERROR, new HttpErrorResponse(e.getMessage()));
        }
    }
}
