package Application.MTCG.controller;

import Application.MTCG.exceptions.InvalidBodyException;
import Application.MTCG.exceptions.JsonParserException;
import com.fasterxml.jackson.core.JsonProcessingException;
import Application.MTCG.exceptions.MissingLoginTokenException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import Application.MTCG.dto.ScoreboardDTO;
import Application.MTCG.dto.ShowStatsDTO;


import Server.http.Request;
import Server.http.Response;
import Server.http.Status;

public abstract class Controller {

    private final ObjectMapper objectMapper;

    public Controller() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
    }

    public abstract Response handle(Request request);


    protected <T> T fromBody(String body, Class<T> type) {
        try {
            return objectMapper.readValue(body, type);
        } catch (JsonProcessingException e) {
            throw new InvalidBodyException(e);
        }
    }

    protected Response json(Status status, Object object) {
        Response response = new Response();
        response.setStatus(status);
        response.setHeader("Content-Type", "application/json");
        try {
            response.setBody(objectMapper.writeValueAsString(object));
        } catch (JsonProcessingException e) {
            throw new JsonParserException(e);
        }
        return response;
    }

    protected String getLoginToken(Request request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new MissingLoginTokenException("Login Token is missing");
        }
            String token = header.split(" ")[1];
            String name = token.split("-")[0];
            return token;

    }

    protected <T> T arrayFromBody(String body, TypeReference<T> typeReference) {
        try {
            return objectMapper.readValue(body, typeReference);
        } catch (JsonProcessingException e) {
            throw new InvalidBodyException(e);
        }
    }

    protected Response text(Status status, String plaintext) {
        Response response = new Response();
        response.setStatus(status);
        response.setHeader("Content-Type", "application/json");
        response.setBody(plaintext);
        return response;
    }

    protected Response scoreboardText(Status status, ScoreboardDTO scoreboard) {
        StringBuilder formattedText = new StringBuilder();

        formattedText.append("Scoreboard:").append(System.lineSeparator());;
        formattedText.append("-----------------------------").append(System.lineSeparator());;

        for (ShowStatsDTO stats : scoreboard.getScoreboard()) {
            formattedText.append("  Name: ").append(stats.getName()).append(System.lineSeparator());
            formattedText.append("   - Elo: ").append(stats.getElo()).append(System.lineSeparator());
            formattedText.append("   - Wins: ").append(stats.getWins()).append(System.lineSeparator());
            formattedText.append("   - Losses: ").append(stats.getLosses()).append(System.lineSeparator());
            formattedText.append("-----------------------------").append(System.lineSeparator());;
        }

        return text(status, formattedText.toString());
    }

}