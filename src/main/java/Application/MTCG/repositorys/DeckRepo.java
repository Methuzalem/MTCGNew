package Application.MTCG.repositorys;

import Application.MTCG.data.ConnectionPool;
import Application.MTCG.entity.Card;
import Application.MTCG.entity.Deck;
import Application.MTCG.entity.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DeckRepo {

    private final ConnectionPool connectionPool;

    private final static String NEW_DECK = "INSERT INTO decks VALUES (?, ?)";
    private final static String USER_ALREADY_EXISTS = "SELECT * FROM decks WHERE owner_id = ?";

    public DeckRepo(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public void save(Deck deck) {
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(NEW_DECK);
        ) {
            preparedStatement.setString(1, deck.getDeckId());
            preparedStatement.setString(2, deck.getUserId());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public boolean userAlreadyWithDeck(User user) {
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(USER_ALREADY_EXISTS);
        ) {
            preparedStatement.setString(1, user.getUuid());
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
