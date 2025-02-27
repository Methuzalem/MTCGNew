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
    private final static String NEW_USER = "INSERT INTO users (uuid, username, password, credit, token) VALUES (?, ?, ?, ?, ?)";
    private final static String FIND_USER_BY_USERNAME = "SELECT * FROM users WHERE username = ?";
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
            user.setToken("%s-mtcgToken".formatted(user.getUsername()));
            preparedStatement.setString(5, user.getToken());
            preparedStatement.execute();
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
}
