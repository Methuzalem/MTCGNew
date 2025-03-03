package Application.MTCG.controller;

import Application.MTCG.dto.ShowStatsDTO;
import Application.MTCG.entity.Card;
import Application.MTCG.exceptions.InvalidUserData;
import Application.MTCG.service.StatService;

import Server.http.Request;
import Server.http.Response;
import Server.http.Method;
import Server.http.Status;

import java.util.List;

public class StatsController extends Controller {
    private final StatService statService;

    public StatsController(StatService statService) {
        this.statService = statService;
    }

    @Override
    public Response handle(Request request) {
        if(request.getMethod() == Method.GET) {
            return displayStats(request);
        }
        return json(Status.NOT_FOUND, "Couldn't handle HTTP request");
    }

    private Response displayStats(Request request) {
        try {
            String loginToken = getLoginToken(request);
            ShowStatsDTO showStats = statService.getStatsOfUser(loginToken);
            return json(Status.OK, showStats);
        } catch (InvalidUserData e) {
            return json(Status.NOT_FOUND, e);
        } catch (Exception e) {
            return json(Status.INTERNAL_SERVER_ERROR, e);
        }
    }
}
