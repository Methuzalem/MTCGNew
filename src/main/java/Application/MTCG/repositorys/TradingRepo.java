package Application.MTCG.repositorys;

import Application.MTCG.data.ConnectionPool;
import Application.MTCG.entity.Card;
import Application.MTCG.entity.User;
import Application.MTCG.entity.Trade;
import java.util.List;
import java.util.ArrayList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class TradingRepo {
    private final ConnectionPool connectionPool;

    private final static String TRADES_TO_DISPLAY = "SELECT * from trades";
    private final static String CREATE_TRADING_DEAL = "INSERT INTO trades VALUES (?, ?, ?, ?, ?, ?)";
    private final static String TRADE_BY_ID = "SELECT * from trades WHERE id = ?";
    private final static String DELETE_TRADE_BY_ID = "DELETE from trades WHERE id = ?";



    public TradingRepo(ConnectionPool connectionPool) {this.connectionPool = connectionPool;}


    public List<Trade> getTradingDeals() {
        List<Trade> trades = new ArrayList<>();

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(TRADES_TO_DISPLAY);
                ResultSet resultSet = preparedStatement.executeQuery();
        ) {
            while (resultSet.next()) {
                Trade trade = new Trade(
                        resultSet.getString("id"),
                        resultSet.getString("card_to_trade"),
                        resultSet.getString("type"),
                        resultSet.getFloat("minimum_damage"),
                        resultSet.getString("trade_status"),
                        resultSet.getString("owner_id")
                );
                trades.add(trade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return trades;
    }

    public void createTradingDeal(User user, Trade trade) {
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(CREATE_TRADING_DEAL)
        ) {
                preparedStatement.setString(1, trade.getId());
                preparedStatement.setString(2, trade.getCardToTrade());
                preparedStatement.setString(3, trade.getType());
                preparedStatement.setFloat(4, trade.getMinimumDamage());
                preparedStatement.setString(5, trade.getTradeStatus());
                preparedStatement.setString(6, user.getUuid());
                preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Trade getTradeById(String id) {
        Trade trade = null;

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(TRADE_BY_ID);
        ) {
            preparedStatement.setString(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    trade = new Trade(
                            resultSet.getString("id"),
                            resultSet.getString("card_to_trade"),
                            resultSet.getString("type"),
                            resultSet.getFloat("minimum_damage"),
                            resultSet.getString("trade_status"),
                            resultSet.getString("owner_id")
                    );
                }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return trade;
    }

    public void deleteTradeById(String id) {
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(DELETE_TRADE_BY_ID)
        ) {
            preparedStatement.setString(1, id);
            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
