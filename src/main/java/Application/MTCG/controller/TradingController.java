package Application.MTCG.controller;

import Application.MTCG.entity.HttpErrorResponse;
import Application.MTCG.exceptions.InvalidTradingDeal;
import Application.MTCG.exceptions.InvalidUserData;
import Application.MTCG.service.TradingService;
import Application.MTCG.entity.Trade;

import Server.http.Method;
import Server.http.Request;
import Server.http.Response;
import Server.http.Status;

public class TradingController extends Controller {
    TradingService tradingService;

    public TradingController(TradingService tradingService) {
        this.tradingService = tradingService;
    }

    @Override
    public Response handle(Request request) {
        if (request.getMethod() == Method.GET) {
            return displayTrades(request);
        } else if (request.getMethod() == Method.POST) {
            return createTradingDeal(request);
        } else if (request.getMethod() == Method.DELETE) {
            return deleteTradingDeal(request);
        }
        return json(Status.NOT_FOUND, "Couldn't handle HTTP request");
    }

    public Response displayTrades(Request request) {
        String tradingDeals = tradingService.showTradingDeals();
        return text(Status.OK, tradingDeals);
    }

    public Response createTradingDeal(Request request) {
        try {
            String loginToken = getLoginToken(request);
            if (request.getPath().equals("/tradings")) {
                Trade tradingDeal = fromBody(request.getBody(), Trade.class);
                String tradeCreated = tradingService.createTradingDeal(loginToken, tradingDeal);
                return json(Status.OK, tradeCreated);
            } else {
                String path = request.getPath();
                String tradingId = getTradingId(path);
                String cardIdToTrade = fromBody(request.getBody(), String.class);
                String tradeDone = tradingService.tradeCard(loginToken, tradingId, cardIdToTrade);
                return json(Status.OK, tradeDone);
            }
        } catch (InvalidUserData e) {
            return json(Status.NOT_FOUND, new HttpErrorResponse(e.getMessage()));
        } catch (InvalidTradingDeal e) {
            return json(Status.CONFLICT, new HttpErrorResponse(e.getMessage()));
        }
    }

    public Response deleteTradingDeal(Request request) {
        try {
            String loginToken = getLoginToken(request);
            String path = request.getPath();
            String tradingID = getTradingId(path);

            String deletedTrading = tradingService.deleteTradingById(loginToken, tradingID);

            return json(Status.OK, deletedTrading);
        } catch (InvalidUserData e) {
            return json(Status.NOT_FOUND, new HttpErrorResponse(e.getMessage()));
        } catch (InvalidTradingDeal e) {
            return json(Status.CONFLICT, new HttpErrorResponse(e.getMessage()));
        }
    }
}
