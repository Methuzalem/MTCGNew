package Application.MTCG.service;

import Application.MTCG.entity.Card;
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
    private User user2;

    @BeforeEach
    void setUp() {
        card1 = new Card("ID1", "FireGoblin", "Fire", 20, "player1", "player1");
        card2 = new Card("ID2", "Dragon", "Unknown", 50, "player2", "player2");
        user = new User("UUID123", "TestUser", "password", "token123", 10, 0, "name", "bio", "image", 100, 1, 0);
        user2 = new User("UUID345", "TestUser2", "password2", "token345", 0, 0, "name2", "bio2", "image2", 95, 0, 1);
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



}