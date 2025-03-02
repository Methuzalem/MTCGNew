package Application.MTCG.service;

import Application.MTCG.entity.Deck;
import Application.MTCG.exceptions.InvalidDeckData;
import Application.MTCG.exceptions.NullPointerException;
import Application.MTCG.exceptions.UserAlreadyWithDeck;
import Application.MTCG.repositorys.CardRepo;
import Application.MTCG.repositorys.DeckRepo;
import Application.MTCG.entity.Card;
import Application.MTCG.entity.User;

import java.util.List;
import java.util.UUID;

public class DeckService {
    CardRepo cardRepo;
    UserService userService;
    DeckRepo deckRepo;

    public DeckService(CardRepo cardRepo, UserService userService, DeckRepo deckRepo) {
        this.cardRepo = cardRepo;
        this.userService = userService;
        this.deckRepo = deckRepo;
    }

    public List<Card> getDeckCardsOfUser(String loginToken) {
        User user = userService.getUserByToken(loginToken);
        return cardRepo.getDeckCardsByUser(user);
    }

    public Deck createDeck(String loginToken) {
        Deck deck = new Deck();
        User user = userService.getUserByToken(loginToken);
        boolean userGotAlreadyADeck = deckRepo.userAlreadyWithDeck(user);

        if(userGotAlreadyADeck) {
            throw new UserAlreadyWithDeck("User got already a deck");
        } else {
            deck.setDeckId(UUID.randomUUID().toString());
            deck.setUserId(user.getUuid());
            deckRepo.save(deck);
            return deck;
        }
    }

    public void configureDeckByTokenAndUserId(Deck newDeck, List<String> deckCardIds) {
        try {
            cardRepo.updateCardWithDeckId(newDeck, deckCardIds);
        } catch (InvalidDeckData e) {
            throw new InvalidDeckData(e.getMessage());
        }

    }

    public String convertDeckToPlainText(List<Card> deck) {
        StringBuilder plainTextDeck = new StringBuilder();

        for (Card card : deck) {
            plainTextDeck.append("Name: ").append(card.getCardName())
                    .append(", Element: ").append(card.getElementType())
                    .append(", Damage: ").append(card.getDamage())
                    .append(System.lineSeparator());
        }
        return plainTextDeck.toString();
    }
}
