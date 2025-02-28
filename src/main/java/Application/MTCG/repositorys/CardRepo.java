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
    private final static String ALL_CARDS = "SELECT * FROM cards";
    private final static String CARDS_BELONGING_TO_DECK = "SELECT * from cards where deck_owner_uuid = ?";
    private final static String CARDS_BELONGING_TO_USER = "SELECT * FROM cards WHERE owner_uuid = ?";
    private final static String CARDS_NOT_BELONGING_TO_ANY_USER = "SELECT * FROM cards WHERE owner_uuid is NULL";
    private final static String CARDS_BELONGING_TO_CARD_ID = "SELECT * from cards where id = ?";
    private final static String UPDATED_CARD = "UPDATE cards SET owner_uuid = ?, deck_owner_uuid = ? where id = ?";

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
}
