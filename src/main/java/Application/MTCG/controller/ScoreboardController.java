package Application.MTCG.controller;

import Application.MTCG.dto.ShowStatsDTO;
import Application.MTCG.entity.Deck;
import Application.MTCG.entity.HttpErrorResponse;
import Application.MTCG.exceptions.InvalidDeckData;
import Application.MTCG.exceptions.UserAlreadyWithDeck;
import Application.MTCG.service.ScoreboardService;
import Application.MTCG.dto.ScoreboardDTO;

import Server.http.Method;
import Server.http.Request;
import Server.http.Response;
import Server.http.Status;
import com.fasterxml.jackson.core.type.TypeReference;

import java.sql.SQLException;
import java.util.List;


public class ScoreboardController extends Controller{
    ScoreboardService scoreboardService;

    public ScoreboardController(ScoreboardService service) {
        this.scoreboardService = service;
    }

    @Override
    public Response handle(Request request) {
        if(request.getMethod() == Method.GET) {
            return create(request);
        }
        return json(Status.NOT_FOUND, "Couldn't handle HTTP request");
    }

    public Response create(Request request) {
        try {
            ScoreboardDTO scoreboard = new ScoreboardDTO(scoreboardService.buildScoreBoard());
            return scoreboardText(Status.OK, scoreboard);
        } catch (RuntimeException e) {
            return json(Status.INTERNAL_SERVER_ERROR, new HttpErrorResponse(e.getMessage()));
        }
    }
}
