package Application.MTCG.controller;

import Application.MTCG.dto.ScoreboardDTO;
import Application.MTCG.dto.ShowStatsDTO;
import Application.MTCG.exceptions.InvalidBodyException;
import Application.MTCG.exceptions.MissingLoginTokenException;
import Server.http.Request;
import Server.http.Response;
import Server.http.Status;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ControllerTest {

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private Controller controller = new Controller() {
        @Override
        public Response handle(Request request) {
            return null;
        }
    };

    @Mock
    private Request request;

    private ShowStatsDTO showStatsDTO;

    @BeforeEach
    void setUp() {
        showStatsDTO = new ShowStatsDTO("TestUser", "100", "10", "5");
    }

    @Test
    void fromBodyShouldReturnObjectWhenValidJson() throws JsonProcessingException {
        String json = "{\"name\":\"TestUser\", \"elo\":100, \"wins\":10, \"losses\":5}";

        ShowStatsDTO result = controller.fromBody(json, ShowStatsDTO.class);

        assertEquals("TestUser", result.getName());
        assertEquals("100", result.getElo());
        assertEquals("10", result.getWins());
        assertEquals("5", result.getLosses());
    }

    @Test
    void jsonShouldReturnResponseWithJsonBody() {
        Response response = controller.json(Status.OK, "Test Message");

        assertEquals(Status.OK, response.getStatus());
        assertEquals("application/json", response.getHeaders().get("Content-Type"));
    }

    @Test
    void getLoginTokenShouldReturnTokenWhenValidHeader() {
        when(request.getHeader("Authorization")).thenReturn("Bearer token123");

        String token = controller.getLoginToken(request);

        assertEquals("token123", token);
    }

    @Test
    void getLoginTokenShouldThrowExceptionWhenHeaderIsMissing() {
        when(request.getHeader("Authorization")).thenReturn(null);

        assertThrows(MissingLoginTokenException.class, () -> controller.getLoginToken(request));
    }

    @Test
    void arrayFromBodyShouldReturnObjectWhenValidJsonArray() throws JsonProcessingException {
        String jsonArray = "[{\"name\":\"TestUser\"}]";

        List<ShowStatsDTO> result = controller.arrayFromBody(jsonArray, new TypeReference<List<ShowStatsDTO>>() {});

        assertEquals(1, result.size());
        assertEquals("TestUser", result.get(0).getName());
    }

    @Test
    void textShouldReturnResponseWithPlainText() {
        Response response = controller.text(Status.OK, "Plain text message");

        assertEquals(Status.OK, response.getStatus());
        assertEquals("Plain text message", response.getBody());
    }

    @Test
    void scoreboardTextShouldReturnFormattedScoreboard() {
        ScoreboardDTO scoreboard = new ScoreboardDTO(List.of(showStatsDTO));
        Response response = controller.scoreboardText(Status.OK, scoreboard);

        assertTrue(response.getBody().contains("Scoreboard:"));
        assertTrue(response.getBody().contains("TestUser"));
    }

    @Test
    void getTradingIdShouldReturnIdWhenPathIsValid() {
        String tradingId = controller.getTradingId("/tradings/12345");

        assertEquals("12345", tradingId);
    }

    @Test
    void getTradingIdShouldThrowExceptionWhenPathIsInvalid() {
        assertThrows(IllegalArgumentException.class, () -> controller.getTradingId("/invalid/12345"));
    }
}
