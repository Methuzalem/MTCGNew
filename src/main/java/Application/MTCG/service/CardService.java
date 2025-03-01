package Application.MTCG.service;

import Application.MTCG.entity.Card;
import Application.MTCG.entity.User;
import Application.MTCG.exceptions.*;
import Application.MTCG.exceptions.NullPointerException;
import Application.MTCG.repositorys.CardRepo;


import java.util.List;
import java.util.ArrayList;

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
        } else if (card.getCardName() == null) {
            throw new NullPointerException("Card name can't,od be null");
        }
        card = cardRepo.save(card);
        return card;
    }

    public List<Card> checkCoinsAquirePackage(String loginToken){
        User user = userService.getUserByToken(loginToken);
        final int packageCosts = 5;
        final int amountOfCardsInPackage = 5;

        if(user.getCoins() >= packageCosts){
            List<Card> packageCards = new ArrayList<>();
            List<Card> cardsWithoutOwner = cardRepo.findFreeCards(user);
            if(cardsWithoutOwner == null || cardsWithoutOwner.size() < amountOfCardsInPackage){
                throw new NotEnoughCards("Not enough Cards for package available");
            }
            for(int i = 0; i < amountOfCardsInPackage; i++){
                cardsWithoutOwner.get(i).setOwnerID(user.getUuid());
                cardRepo.updateOwner(cardsWithoutOwner.get(i));
                packageCards.add(cardsWithoutOwner.get(i));
            }
            if(user.getPackageCount() == 10){
                user.setPackageCount(0);
            } else {
                user.setCoins(user.getCoins()-5);
                user.setPackageCount(user.getPackageCount()+1);
            }
            userService.updateUserByUuid(user);
            return packageCards;

        } else {
            throw(new NotEnoughCoins("Not enough coins left"));
        }
    }

    public List<Card> readCardsOfUser(User user) {
        try {
            List<Card> cards = cardRepo.findCardsbyUserId(user);
            if(cards == null || cards.isEmpty()){
                throw new NoCardsFound("No cards found for this user");
            }
            return cards;
        } catch (NoCardsFound e) {
            throw(new NoCardsFound("No cards found for this user"));
        }
    }
}