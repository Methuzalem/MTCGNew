package Application.MTCG.controller;

import Application.MTCG.service.BattleService;
import Server.http.Method;
import Server.http.Request;
import Server.http.Response;
import Server.http.Status;

import java.util.concurrent.*;

public class BattleController extends Controller {
    private final BattleService battleService;
    private static final BlockingQueue<String> battleQueue = new LinkedBlockingQueue<>();
    private static final Object battleLock = new Object();
    private  String battleLog;

    public BattleController(BattleService battleService) {
        this.battleService = battleService;
    }

    @Override
    public Response handle(Request request) {
        if (request.getMethod() == Method.POST) {
            return startingBattle(request);
        }
        return json(Status.NOT_FOUND, "Couldn't handle HTTP request");
    }

    private Response startingBattle(Request request) {
        String loginToken = getLoginToken(request);

        synchronized (battleLock) {
            try {
                String opponentToken = battleQueue.peek();

                if (opponentToken != null && !opponentToken.equals(loginToken)) {
                    battleQueue.poll();

                    battleLog = battleService.startBattle(loginToken, opponentToken);

                    return text(Status.OK, battleLog);
                }

                if (!battleQueue.contains(loginToken)) {
                    battleQueue.add(loginToken);
                }

                battleLock.wait(5000);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return json(Status.INTERNAL_SERVER_ERROR, "An error occurred while processing the request.");
            }
            return text(Status.OK, battleLog);
        }
    }
}
