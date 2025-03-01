package Application.MTCG.repositorys;

import Application.MTCG.data.ConnectionPool;
import Application.MTCG.entity.Card;
import Application.MTCG.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CardRepo {

    private final ConnectionPool connectionPool;

    private final static String NEW_CARD = "INSERT INTO cards VALUES (?, ?, ?, ?, ?)";
    private final static String FIND_FREE_CARDS = "SELECT * FROM cards WHERE owner_uuid is NULL";
    private final static String UPDATE_CARD = "UPDATE cards SET owner_uuid = ? where id = ?";
    private final static String FIND_CARDS_BY_USER_ID = "SELECT * from cards where owner_uuid = ?";

    private final static String ALL_CARDS = "SELECT * FROM cards";
    private final static String CARDS_BELONGING_TO_DECK = "SELECT * from cards where deck_owner_uuid = ?";
    private final static String CARDS_BELONGING_TO_USER = "SELECT * FROM cards WHERE owner_uuid = ?";




    public CardRepo(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public Card save(Card card) {
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(NEW_CARD)
        ) {
            preparedStatement.setString(1, card.getCardID());
            preparedStatement.setString(2, card.getCardName());
            preparedStatement.setFloat(3, card.getDamage());
            preparedStatement.setString(4, card.getOwnerID());
            preparedStatement.setString(5, card.getElementType());
            preparedStatement.execute();
            return card;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Card> findFreeCards(User user){
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_FREE_CARDS)
        ) {
            List<Card> cards = new ArrayList<>();
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Card card = new Card(resultSet.getString("id"), resultSet.getString("name"), resultSet.getFloat("damage"), resultSet.getString("owner_uuid"), resultSet.getString("elementType"));
                cards.add(card);
            }
            return cards;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void updateOwner(Card card){
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CARD)
        ) {
            preparedStatement.setString(1, card.getOwnerID());
            preparedStatement.setString(2, card.getCardID());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Card> findCardsbyUserId(User user){
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_CARDS_BY_USER_ID)
        ) {
            List<Card> cards = new ArrayList<>();
            preparedStatement.setString(1, user.getUuid());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Card card = new Card(resultSet.getString("id"), resultSet.getString("name"), resultSet.getFloat("damage"), resultSet.getString("owner_uuid"), resultSet.getString("elementType"));
                cards.add(card);
            }
            return cards;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
