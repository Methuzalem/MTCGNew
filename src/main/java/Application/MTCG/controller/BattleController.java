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
    private static final ExecutorService battleExecutor = Executors.newCachedThreadPool();
    private static final Object battleLock = new Object();
    private static final ConcurrentHashMap<String, String> battleResults = new ConcurrentHashMap<>();
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
                // Prüfe, ob bereits ein Spieler in der Warteschlange ist
                String opponentToken = battleQueue.peek();

                if (opponentToken != null && !opponentToken.equals(loginToken)) {
                    // Zweiter Spieler gefunden, entferne den Gegner aus der Warteschlange
                    battleQueue.poll();

                    // Starte das Battle asynchron
                    Future<String> battleResult = battleExecutor.submit(() -> {
                        return battleService.startBattle(loginToken, opponentToken);
                    });

                    // Warte auf das Ergebnis des Battles
                    battleLog = battleResult.get(10, TimeUnit.SECONDS);

                    // Wecke alle wartenden Spieler auf
                    battleLock.notifyAll();

                    return text(Status.OK, battleLog);
                }

                // Falls kein Gegner da ist, Spieler in die Warteschlange legen
                if (!battleQueue.contains(loginToken)) {
                    battleQueue.offer(loginToken);
                }

                // Warten auf einen Gegner (nicht sofort aus der Queue entfernen)
                battleLock.wait(10000); // Warte maximal 10 Sekunden auf einen Gegner

            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                Thread.currentThread().interrupt();
                return json(Status.INTERNAL_SERVER_ERROR, "An error occurred while processing your request.");
            }
            return text(Status.OK, battleLog);
        }
    }
}
