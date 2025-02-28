package Application.MTCG.service;

import Application.MTCG.entity.Card;
import Application.MTCG.entity.User;
import Application.MTCG.exceptions.NullPointerException;
import Application.MTCG.exceptions.PermissionDenied;
import Application.MTCG.service.UserService;
import Application.MTCG.repositorys.CardRepo;

import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

public class CardService {
    private final CardRepo cardRepo;
    private final UserService userService;

    public CardService(CardRepo cardRepo, UserService userService) {
        this.cardRepo = cardRepo;
        this.userService = userService;
    }

    public List<Card> createPackage(String loginToken, List<Card> cards) {
        if(loginToken.equals("admin-mtcgToken")){
            List<Card> newCards = new ArrayList<>();
            for (Card card : cards) {
                Card newCard = createCard(card);
                newCards.add(newCard);
            }
            return newCards;
        }
        throw new PermissionDenied("Packages can only be created by admin-mtcgToken");
    }

    public Card createCard(Card card) {
        if (card == null) {
            throw new NullPointerException("Card object can't be null");
        }

        if (card.getCardName() == null) {
            throw new NullPointerException("Card name can't,od be null");
        }

        card = cardRepo.save(card);

        return card;
    }
}
