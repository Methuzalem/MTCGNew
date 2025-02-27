package Application.MTCG.repositorys;

import Application.MTCG.data.ConnectionPool;
import Application.MTCG.entity.User;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Optional;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;


public class UserRepo {
    private final static String NEW_USER = "INSERT INTO users (uuid, username, password, credit) VALUES (?, ?, ?, ?)";
    private final static String FIND_USER_BY_USERNAME = "SELECT * FROM users WHERE username = ?";
    private final static String UPDATE_USER_BY_UUID = "UPDATE users SET username = ?, password = ?, bio = ?, image = ?, elo = ?, wins = ?, losses = ?, token = ?, credit = ? WHERE uuid = ?";
    private final static String FIND_ALL_USERS = "SELECT * FROM users";
    private final static String FIND_USER_BY_TOKEN = "SELECT * FROM users WHERE token = ?";
    private final ConnectionPool connectionPool;

    public UserRepo(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public User save(User user) {
        try(
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(NEW_USER);
        )
        {
            preparedStatement.setString(1, user.getUuid());
            preparedStatement.setString(2, user.getUsername());
            preparedStatement.setString(3, user.getPassword());
            preparedStatement.setInt(4, 20);
            preparedStatement.execute();
            user.setToken("%s-mtcgToken".formatted(user.getUsername()));
            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Optional<User> findUserByName(String name) {
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_USERNAME)
        ) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new User(resultSet.getString("uuid"), resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("bio"), resultSet.getString("image"), resultSet.getInt("elo"), resultSet.getInt("wins"), resultSet.getInt("losses"), resultSet.getString("token"), resultSet.getInt("credit")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        return Optional.empty();
    }

    public void updateUserByUuid(User user){
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_BY_UUID)
        ) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getBio());
            preparedStatement.setString(4, user.getImage());
            preparedStatement.setInt(5, user.getElo());
            preparedStatement.setInt(6, user.getWins());
            preparedStatement.setInt(7, user.getLosses());
            preparedStatement.setString(8, user.getToken());
            preparedStatement.setInt(9, user.getCredit());
            preparedStatement.setString(10, user.getUuid());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
