package Application.MTCG.service;

import Application.MTCG.entity.Card;
import Application.MTCG.entity.Deck;
import Application.MTCG.entity.User;
import Application.MTCG.repositorys.CardRepo;
import Application.MTCG.repositorys.DeckRepo;
import Application.MTCG.repositorys.UserRepo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BattleServiceTest {
    @Mock private UserService userService;
    @Mock private UserRepo userRepo;
    @Mock private CardRepo cardRepo;
    @Mock private DeckService deckService;
    @Mock
    private DeckRepo deckRepo;

    @InjectMocks
    private BattleService battleService;

    private User player1;
    private User player2;

    @BeforeEach
    void setUp() {
        player1 = new User("UUID123", "User1", "Passwort1", "Token1", 5, 0, "Name1", "Bio1", "Image1", 100, 1, 0);
        player2 = new User("UUID345", "User2", "Passwort2", "Token2", 5, 0, "Name2", "Bio2", "Image2", 100, 0, 1);
    }

    @Test
    void deckOfPlayer1IsEmpty() {
        Card card2 = new Card("ID1", "FireGoblin", "Fire", 20, "player1", "player1");

        List<Card> deckPlayer2 = new ArrayList<>(List.of(card2));

        when(deckService.getDeckCardsOfUser("Token1")).thenReturn(new ArrayList<>());
        when(deckService.getDeckCardsOfUser("Token2")).thenReturn(deckPlayer2);
        when(userService.getUserByToken("Token1")).thenReturn(player1);
        when(userService.getUserByToken("Token2")).thenReturn(player2);

        String finalLog = battleService.startBattle("Token1", "Token2");

        assertTrue(finalLog.contains("wins the epic battle"), "Player 2 should win if Player 1 has no cards");
    }

    @Test
    void decksOfBothPlayersAreEmpty() {
        when(deckService.getDeckCardsOfUser("Token1")).thenReturn(new ArrayList<>());
        when(deckService.getDeckCardsOfUser("Token2")).thenReturn(new ArrayList<>());
        when(userService.getUserByToken("Token1")).thenReturn(player1);
        when(userService.getUserByToken("Token2")).thenReturn(player2);

        String finalLog = battleService.startBattle("Token1", "Token2");

        assertTrue(finalLog.contains("Its a draw"), "Should be a draw if both decks are empty");
    }

    @Test
    void calculateDamageGoblinAgainstDragon() {
        StringBuilder log = new StringBuilder();
        Card goblin = new Card("ID1", "FireGoblin", "Fire", 20, "player1", "player1");
        Card dragon = new Card("ID2", "Dragon", "Unknown", 50, "player2", "player2");
        double damage = battleService.calculateDamage(goblin, dragon, log);

        assertEquals(0, damage, "Goblins should not be able to attack Dragons");
    }

    @Test
    void calculateEffectivenessWaterAgainstFire() {
        Card water = new Card("ID1", "WaterSpell", "Water", 50, "player1", "player1");
        Card fire = new Card("ID2", "FireSpell", "Fire", 50, "player2", "player2");
        double effectiveness = battleService.calculateEffectiveness(water, fire);

        assertEquals(2.00, effectiveness, "Water should be 2x effective against Fire");
    }

    @Test
    void calculateEloPlayer1Wins() {
        User player1 = new User("UUID123", "User1", "Passwort1", "Token1", 5, 0, "Name1", "Bio1", "Image1", 100, 1, 0);
        User player2 = new User("UUID345", "User2", "Passwort2", "Token2", 5, 0, "Name2", "Bio2", "Image2", 100, 0, 1);

        StringBuilder log = new StringBuilder();
        battleService.calculateElo(player1, player2, log);

        assertEquals(103, player1.getElo(), "Winner should gain 3 Elo points");
        assertEquals(95, player2.getElo(), "Loser should lose 5 Elo points");
    }

    @Test
    void cardsGotTheSameAmountOfDamage () {
        User player1 = new User("UUID123", "User1", "Passwort1", "Token1", 5, 0, "Name1", "Bio1", "Image1", 100, 1, 0);
        User player2 = new User("UUID345", "User2", "Passwort2", "Token2", 5, 0, "Name2", "Bio2", "Image2", 100, 0, 1);

        Card card1 = new Card("ID1", "WaterGoblin", "Water", 20, "player1", "player1");
        Card card2 = new Card("ID2", "FireGoblin", "Fire", 20, "player2", "player2");

        List<Card> deckPlayer1 = new ArrayList<>(List.of(card2));
        List<Card> deckPlayer2 = new ArrayList<>(List.of(card2));

        when(deckService.getDeckCardsOfUser("Token1")).thenReturn(deckPlayer1);
        when(deckService.getDeckCardsOfUser("Token2")).thenReturn(deckPlayer2);
        when(userService.getUserByToken("Token1")).thenReturn(player1);
        when(userService.getUserByToken("Token2")).thenReturn(player2);

        String finalLog = battleService.startBattle("Token1", "Token2");

        assertTrue(finalLog.contains("Its a draw"), "Should be a draw if both Cards got the same amount of Damage");
    }
}
