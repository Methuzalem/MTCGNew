package Application.MTCG.service;

import Application.MTCG.entity.Card;
import Application.MTCG.entity.User;
import Application.MTCG.exceptions.PermissionDenied;
import Application.MTCG.repositorys.CardRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CardServiceTest {
    @Mock private CardRepo cardRepo;
    @Mock private UserService userService;

    @InjectMocks
    private CardService cardService;

    private Card card1;
    private Card card2;
    private User user;

    @BeforeEach
    void setUp() {
         card1 = new Card("ID1", "FireGoblin", "Fire", 20, "player1", "player1");
         card2 = new Card("ID2", "Dragon", "Unknown", 50, "player2", "player2");
         user = new User("UUID123", "TestUser", "password", "token123", 10, 0, "name", "bio", "image", 100, 0, 0);
    }

    @Test
    void loadCardsInShop_WithAdminToken_ShouldReturnNewCards() {
        List<Card> inputCards = List.of(card1, card2);

        when(cardRepo.save(any(Card.class))).thenReturn(card1);

        List<Card> newCards = cardService.loadCardsInShop("admin-mtcgToken", inputCards);

        assertNotNull(newCards);
        assertEquals(2, newCards.size(), "Should return a list with the same number of cards");
    }

    @Test
    void loadCardsInShop_WithNonAdminToken_ShouldThrowExceptionPermissionDenied() {
        List<Card> inputCards = List.of(card1, card2);

        Exception exception = assertThrows(PermissionDenied.class, () -> cardService.loadCardsInShop("notAdmin-token", inputCards));

        assertEquals("Packages can only be created by admin-mtcgToken", exception.getMessage());
    }

    @Test
    void createCard_ShouldReturnSavedCard() {
        when(cardRepo.save(any(Card.class))).thenReturn(card1);

        Card savedCard = cardService.createCard(card1);

        assertNotNull(savedCard);
        assertEquals(card1.getCardID(), savedCard.getCardID());
    }

    @Test
    void checkCoinsAquirePackage_WithEnoughCoins_ShouldReturnPackage() {
        List<Card> availableCards = new ArrayList<>(List.of(card1, card2, card1, card2, card1));
        when(userService.getUserByToken("token123")).thenReturn(user);
        when(cardRepo.findFreeCards(user)).thenReturn(availableCards);

        List<Card> packageCards = cardService.checkCoinsAquirePackage("token123");

        assertNotNull(packageCards);
        assertEquals(5, packageCards.size(), "Should return 5 cards");
    }


}