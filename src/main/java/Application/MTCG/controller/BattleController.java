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
                //looking for player in queue and save him as opponentToken
                String opponentToken = battleQueue.peek();

                //If oppenent exists, and opponent and user are not the same put opponent out of queue and start battle, return battleLog
                if (opponentToken != null && !opponentToken.equals(loginToken)) {
                    battleQueue.poll();

                    battleLog = battleService.startBattle(loginToken, opponentToken);

                    return text(Status.OK, battleLog);
                }

                //If opponent does not exist and player is not in queue
                if (!battleQueue.contains(loginToken)) {
                    battleQueue.add(loginToken);
                }

                //wait for opponent at least for 5 sec
                battleLock.wait(5000);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return json(Status.INTERNAL_SERVER_ERROR, "An error occurred while processing the request.");
            }
            return text(Status.OK, battleLog);
        }
    }
}
