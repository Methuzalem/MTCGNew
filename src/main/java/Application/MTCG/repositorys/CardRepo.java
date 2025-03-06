package Application.MTCG.repositorys;

import Application.MTCG.data.ConnectionPool;
import Application.MTCG.entity.Card;
import Application.MTCG.entity.User;
import Application.MTCG.entity.Deck;
import Application.MTCG.exceptions.InvalidCardData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardRepo {

    private final ConnectionPool connectionPool;

    private final static String NEW_CARD = "INSERT INTO cards VALUES (?, ?, ?, ?, ?, ?)";
    private final static String FIND_FREE_CARDS = "SELECT * FROM cards WHERE owner_uuid is NULL";
    private final static String SET_OWNER_BY_CARD = "UPDATE cards SET owner_uuid = ? where id = ?";
    private final static String FIND_CARDS_BY_USER_ID = "SELECT * from cards where owner_uuid = ?";
    private final static String DISPLAY_CARDS_DECK_OF_USER = "SELECT c.* FROM cards c JOIN decks d ON c.deck_id = d.id WHERE d.owner_id = ?";
    private final static String PUT_CARD_IN_PLAYER_DECK = "UPDATE cards SET deck_id = ? where id = ?";
    private final static String UPDATE_DECK_ID_AND_OWNER = "UPDATE cards SET deck_id = ?, owner_uuid = ? where id = ?";
    private final static String UPDATE_OWNER_BY_ID = "UPDATE cards SET owner_uuid = ? where id = ?";
    private final static String GET_CARD_BY_ID = "SELECT * from cards WHERE id = ?";


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
            preparedStatement.setString(3, card.getElementType());
            preparedStatement.setFloat(4, card.getDamage());
            preparedStatement.setString(5, card.getOwnerID());
            preparedStatement.setString(6, card.getDeckId());
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
                Card card = new Card(resultSet.getString("id"), resultSet.getString("name"), resultSet.getString("elementType"), resultSet.getFloat("damage"), resultSet.getString("owner_uuid"), resultSet.getString("deck_Id"));
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
                PreparedStatement preparedStatement = connection.prepareStatement(SET_OWNER_BY_CARD)
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
                Card card = new Card(resultSet.getString("id"), resultSet.getString("name"), resultSet.getString("elementType"), resultSet.getFloat("damage"), resultSet.getString("owner_uuid"), resultSet.getString("deck_Id"));
                cards.add(card);
            }
            return cards;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<Card> getDeckCardsByUser(User user){
        List<Card> cards = new ArrayList<>();
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(DISPLAY_CARDS_DECK_OF_USER)
        ) {
            preparedStatement.setString(1, user.getUuid());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Card card = new Card(resultSet.getString("id"), resultSet.getString("name"), resultSet.getString("elementType"), resultSet.getFloat("damage"), resultSet.getString("owner_uuid"), resultSet.getString("deck_Id"));
                cards.add(card);
            }
            return cards;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void updateCardWithDeckIds(Deck newDeck, List<String> deckCardIds){
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(PUT_CARD_IN_PLAYER_DECK)
        ) {
            for (int i = 0; i < 4; i++) {
                preparedStatement.setString(1, newDeck.getDeckId());
                preparedStatement.setString(2, deckCardIds.get(i));
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void updateDeckIdAndOwner(String deckId, User user, Card card){
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_DECK_ID_AND_OWNER)
        ) {
            //if decksize should always stay 4 then deckId should be NULL and just the owner should change
            for (int i = 0; i < 4; i++) {
                preparedStatement.setString(1, deckId);
                preparedStatement.setString(2, user.getUuid());
                preparedStatement.setString(3, card.getCardID());
                preparedStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void updateOwnerByIds(String cardId, String ownerId){
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_OWNER_BY_ID)
        ) {
            preparedStatement.setString(1, ownerId);
            preparedStatement.setString(2, cardId);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Card getCardById(String cardId){
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(GET_CARD_BY_ID)
        ) {
            preparedStatement.setString(1, cardId);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Card card = new Card(resultSet.getString("id"), resultSet.getString("name"), resultSet.getString("elementType"), resultSet.getFloat("damage"), resultSet.getString("owner_uuid"), resultSet.getString("deck_Id"));
                return card;
            } else {
                throw new InvalidCardData("Cant resolve card ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
