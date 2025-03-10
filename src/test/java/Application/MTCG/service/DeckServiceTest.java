package Application.MTCG.service;

import Application.MTCG.entity.Card;
import Application.MTCG.entity.Deck;
import Application.MTCG.entity.User;
import Application.MTCG.repositorys.CardRepo;
import Application.MTCG.repositorys.DeckRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeckServiceTest {
    @Mock
    private CardRepo cardRepo;
    @Mock
    private UserService userService;
    @Mock
    private DeckRepo deckRepo;

    @InjectMocks
    private DeckService deckservice;

    private Card card1;
    private Card card2;
    private User user;

    @BeforeEach
    void setUp() {
        card1 = new Card("ID1", "FireGoblin", "Fire", 20, "player1", "player1");
        card2 = new Card("ID2", "Dragon", "Unknown", 50, "player2", "player2");
        user = new User("UUID123", "TestUser", "password", "token123", 10, 0, "name", "bio", "image", 100, 1, 0);
    }

    @Test
    void testGetDeckCardsOfUserWithValidToken() {
        List<Card> expectedCards = Arrays.asList(card1, card2);

        when(userService.getUserByToken(user.getToken())).thenReturn(user);
        when(cardRepo.getDeckCardsByUser(user)).thenReturn(expectedCards);

        List<Card> result = deckservice.getDeckCardsOfUser(user.getToken());

        assertNotNull(result);
        assertEquals(expectedCards, result);
    }

    @Test
    void testGetDeckCardsOfUserWithNullToken() {

        NullPointerException e = assertThrows(NullPointerException.class, () -> deckservice.getDeckCardsOfUser(null));

        assertEquals("User cant be null", e.getMessage());
    }

    @Test
    void configureDeckByTokenAndUserIdShouldUpdateCardDeckIds() {
        Deck deck = new Deck();
        deck.setDeckId(UUID.randomUUID().toString());
        List<String> deckCardIds = Arrays.asList("Card1", "Card2", "Card3");

        assertDoesNotThrow(() -> deckservice.configureDeckByTokenAndUserId(deck, deckCardIds));
    }

    @Test
    void convertDeckToPlainText_ShouldReturnFormattedString() {
        List<Card> deck = Arrays.asList(card1, card2);
        String expectedOutput = "Name: FireGoblin, Element: Fire, Damage: 20.0" + System.lineSeparator() +
                                "Name: Dragon, Element: Unknown, Damage: 50.0" + System.lineSeparator();

        String result = deckservice.convertDeckToPlainText(deck);

        assertNotNull(result);
        assertEquals(expectedOutput, result);
    }
}