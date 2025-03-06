package Application.MTCG.service;

import Application.MTCG.entity.Card;
import Application.MTCG.entity.User;
import Application.MTCG.exceptions.PermissionDenied;
import Application.MTCG.exceptions.NoCardsFound;
import Application.MTCG.exceptions.NotEnoughCoins;
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
    private User user2;

    @BeforeEach
    void setUp() {
         card1 = new Card("ID1", "FireGoblin", "Fire", 20, "player1", "player1");
         card2 = new Card("ID2", "Dragon", "Unknown", 50, "player2", "player2");
         user = new User("UUID123", "TestUser", "password", "token123", 10, 0, "name", "bio", "image", 100, 1, 0);
         user2 = new User("UUID345", "TestUser2", "password2", "token345", 0, 0, "name2", "bio2", "image2", 95, 0, 1);
    }

    @Test
    void loadCardsInShopWithAdminTokenShouldReturnNewCards() {
        List<Card> inputCards = List.of(card1, card2);

        when(cardRepo.save(any(Card.class))).thenReturn(card1);

        List<Card> newCards = cardService.loadCardsInShop("admin-mtcgToken", inputCards);

        assertNotNull(newCards);
        assertEquals(2, newCards.size(), "Should return a list with the same number of cards");
    }

    @Test
    void loadCardsInShopWithNonAdminTokenShouldThrowExceptionPermissionDenied() {
        List<Card> inputCards = List.of(card1, card2);

        Exception exception = assertThrows(PermissionDenied.class, () -> cardService.loadCardsInShop("notAdmin-token", inputCards));

        assertEquals("Packages can only be created by admin-mtcgToken", exception.getMessage());
    }

    @Test
    void createCardShouldReturnSavedCard() {
        when(cardRepo.save(any(Card.class))).thenReturn(card1);

        Card savedCard = cardService.createCard(card1);

        assertNotNull(savedCard);
        assertEquals(card1.getCardID(), savedCard.getCardID());
    }

    @Test
    void checkCoinsAquirePackageWithEnoughCoinsShouldReturnPackage() {
        List<Card> availableCards = new ArrayList<>(List.of(card1, card2, card1, card2, card1));
        when(userService.getUserByToken("token123")).thenReturn(user);
        when(cardRepo.findFreeCards(user)).thenReturn(availableCards);

        List<Card> packageCards = cardService.checkCoinsAquirePackage("token123");

        assertNotNull(packageCards);
        assertEquals(5, packageCards.size(), "Should return 5 cards");
    }

    @Test
    void checkCoinsAquirePackageWithNotEnoughCoinsShouldThrowException() {
        when(userService.getUserByToken("token345")).thenReturn(user2);

        Exception exception = assertThrows(NotEnoughCoins.class, () ->
                cardService.checkCoinsAquirePackage("token345")
        );

        assertEquals("Not enough coins left", exception.getMessage());
    }

    @Test
    void readCardsOfUserWithExistingCardsShouldReturnCards() {
        List<Card> userCards = List.of(card1, card2);
        when(cardRepo.findCardsbyUserId(user)).thenReturn(userCards);

        List<Card> retrievedCards = cardService.readCardsOfUser(user);

        assertNotNull(retrievedCards);
        assertEquals(2, retrievedCards.size(), "Should return 2 cards");
    }

    @Test
    void readCardsOfUserWithNoCardsShouldThrowException() {
        when(cardRepo.findCardsbyUserId(user)).thenReturn(new ArrayList<>());

        Exception exception = assertThrows(NoCardsFound.class, () ->
                cardService.readCardsOfUser(user)
        );

        assertEquals("No cards found for this user", exception.getMessage());
    }
}