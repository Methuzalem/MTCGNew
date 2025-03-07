package Application.MTCG.service;

import Application.MTCG.entity.Card;
import Application.MTCG.entity.User;
import Application.MTCG.exceptions.InvalidTradingDeal;
import Application.MTCG.repositorys.TradingRepo;
import Application.MTCG.entity.Trade;
import Application.MTCG.repositorys.CardRepo;

import java.util.List;

public class TradingService {
    private final TradingRepo tradingRepo;
    private final UserService userService;
    private final CardRepo cardRepo;

    public TradingService(TradingRepo tradingRepo, UserService userService, CardRepo cardRepo) {
        this.tradingRepo = tradingRepo;
        this.userService = userService;
        this.cardRepo = cardRepo;
    }

    public String showTradingDeals() {
        List<Trade> tradingDeals = tradingRepo.getTradingDeals();
        if (tradingDeals == null || tradingDeals.isEmpty()) {
            return "[]";
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(tradingDeals.size()).append(" trading deals:\n");

        for (Trade trade : tradingDeals) {
            stringBuilder.append("ID: ").append(trade.getId()).append("\n")
                    .append("Card to Trade: ").append(trade.getCardToTrade()).append("\n")
                    .append("Wanted type: ").append(trade.getType()).append("\n")
                    .append("Minimum Damage: ").append(trade.getMinimumDamage()).append("\n")
                    .append("Trade Status: ").append(trade.getTradeStatus()).append("\n")
                    .append("Owner ID: ").append(trade.getOwnerId()).append("\n")
                    .append("----------------------------\n");
        }
        return stringBuilder.toString();
    }

    public String createTradingDeal(String loginToken, Trade trade) {
        try {
            User user = userService.getUserByToken(loginToken);
            tradingRepo.createTradingDeal(user, trade);
            return "Trading Deal created!";
        } catch (Exception e) {
            throw new InvalidTradingDeal("Trading Deal not created! Invalid Data.");
        }
    }

    public String deleteTradingById(String loginToken, String tradingId) {
        User user = userService.getUserByToken(loginToken);
        Trade trade = tradingRepo.getTradeById(tradingId);

        if (user.getUuid().equals(trade.getOwnerId())) {
            tradingRepo.deleteTradeById(tradingId);
            return "Trading Deal deleted!";
        } else {
            throw new InvalidTradingDeal("Trading Deal not deleted! User is not owner of Trading Deal!");
        }
    }

    public String tradeCard(String loginToken, String tradingId, String cardIdToTrade) {
        User user = userService.getUserByToken(loginToken);
        Trade trade = tradingRepo.getTradeById(tradingId);

        if (user.getUuid().equals(trade.getOwnerId())) {
            throw new InvalidTradingDeal("User cant trade with himself.");
        } else {
            String uuidTradeHolder = trade.getOwnerId();
            String uuidTradeExecuter = user.getUuid();
            String cardIdForTradeExecuter = trade.getCardToTrade();
            Card tradeCardHolder = cardRepo.getCardById(cardIdToTrade);
            String cardTypeToTrade = convertNameToMonsterOrSpell(tradeCardHolder.getCardName());

            if (trade.getMinimumDamage() < tradeCardHolder.getDamage() && trade.getType().equals(cardTypeToTrade)) {
                cardRepo.updateOwnerByIds(cardIdToTrade, uuidTradeHolder);
                cardRepo.updateOwnerByIds(cardIdForTradeExecuter, uuidTradeExecuter);
                tradingRepo.closeTradeById(tradingId);
                return "Cards were successfully traded!";
            } else {
                throw new InvalidTradingDeal("Trading requirments are not fullfilled!");
            }
        }
    }

    public String convertNameToMonsterOrSpell(String name) {
        if (name.contains("Goblin") ||
                name.contains("Dragon") ||
                name.contains("Knight") ||
                name.contains("Kraken") ||
                name.contains("Elf") ||
                name.contains("Ork") ||
                name.contains("Wizzard")) {
            return "monster";
        } else {
            return "spell";
        }
    }
}
