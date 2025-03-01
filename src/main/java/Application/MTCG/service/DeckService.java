package Application.MTCG.service;

import Application.MTCG.repositorys.CardRepo;
import Application.MTCG.entity.Card;
import Application.MTCG.entity.User;

import java.util.List;

public class DeckService {
    CardRepo cardRepo;
    UserService userService;

    public DeckService(CardRepo cardRepo, UserService userService) {
        this.cardRepo = cardRepo;
        this.userService = userService;
    }

    public List<Card> displayDeckCardsOfUser(String loginToken) {
        User user = userService.getUserByToken(loginToken);
        return cardRepo.displayDeckUser(user);
    }
}
