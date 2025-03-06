package Application.MTCG.service;

import Application.MTCG.entity.Card;
import Application.MTCG.entity.Trade;
import Application.MTCG.entity.User;
import Application.MTCG.repositorys.CardRepo;
import Application.MTCG.repositorys.DeckRepo;
import Application.MTCG.repositorys.TradingRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TradingServiceTest {
    @Mock
    private CardRepo cardRepo;
    @Mock
    private UserService userService;
    @Mock
    private TradingRepo tradingRepo;

    @InjectMocks
    private TradingService tradingService;

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
    void showTradingDealsWithExistingDealsShouldReturnFormattedString() {
        Trade trade1 = new Trade("TID1", "Card1", "Fire", 30, "Open", "Owner1");
        Trade trade2 = new Trade("TID2", "Card2", "Water", 40, "Open", "Owner2");
        List<Trade> tradingDeals = Arrays.asList(trade1, trade2);

        when(tradingRepo.getTradingDeals()).thenReturn(tradingDeals);

        String expectedOutput = "2 trading deals:\n" +
                "ID: TID1\n" +
                "Card to Trade: Card1\n" +
                "Wanted type: Fire\n" +
                "Minimum Damage: 30.0\n" +
                "Trade Status: Open\n" +
                "Owner ID: Owner1\n" +
                "----------------------------\n" +
                "ID: TID2\n" +
                "Card to Trade: Card2\n" +
                "Wanted type: Water\n" +
                "Minimum Damage: 40.0\n" +
                "Trade Status: Open\n" +
                "Owner ID: Owner2\n" +
                "----------------------------\n";

        String result = tradingService.showTradingDeals();

        assertNotNull(result);
        assertEquals(expectedOutput, result);
    }

    @Test
    void showTradingDealsWithNoDealsShouldReturnEmptyListString() {
        when(tradingRepo.getTradingDeals()).thenReturn(List.of());
        String result = tradingService.showTradingDeals();
        assertEquals("[]", result);
    }

    @Test
    void createTradingDealWithValidDataShouldReturnSuccessMessage() {
        Trade trade = new Trade("TID3", "Card3", "Regular", 25, "Open", "Owner3");
        when(userService.getUserByToken("token123")).thenReturn(user);
        doNothing().when(tradingRepo).createTradingDeal(user, trade); //bc of void do nothing

        String result = tradingService.createTradingDeal("token123", trade);

        assertEquals("Trading Deal created!", result);
    }



}



