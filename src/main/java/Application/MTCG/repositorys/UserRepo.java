package Application.MTCG.repositorys;

import Application.MTCG.data.ConnectionPool;
import Application.MTCG.entity.User;

import java.sql.ResultSet;
import java.util.Optional;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class UserRepo {
    private final static String NEW_USER = "INSERT INTO users (uuid, username, password) VALUES (?, ?, ?)";
    private final static String FIND_USER_BY_NAME = "SELECT * FROM users WHERE username = ?";
    private final static String UPDATE_USER_BY_UUID = "UPDATE users SET username = ?, password = ?, token = ?, coins = ?, packagecount = ?, bio = ?, image = ?, elo = ?, wins = ?, losses = ? WHERE uuid = ?";
    private final static String FIND_USER_BY_TOKEN = "SELECT * FROM users WHERE token = ?";
    private final static String FIND_ALL_USERS = "SELECT * FROM users";
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
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_NAME)
        ) {
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new User(resultSet.getString("uuid"), resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("token"), resultSet.getInt("coins"), resultSet.getInt("packageCount"), resultSet.getString("bio"), resultSet.getString("image"), resultSet.getInt("elo"), resultSet.getInt("wins"), resultSet.getInt("losses")));
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
            preparedStatement.setString(3, user.getToken());
            preparedStatement.setInt(4, user.getCoins());
            preparedStatement.setInt(5, user.getPackageCount());
            preparedStatement.setString(6, user.getBio());
            preparedStatement.setString(7, user.getImage());
            preparedStatement.setInt(8, user.getElo());
            preparedStatement.setInt(9, user.getWins());
            preparedStatement.setInt(10, user.getLosses());
            preparedStatement.setString(11, user.getUuid());
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public Optional<User> findUserByToken(String token) {
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_TOKEN)
        ) {
            preparedStatement.setString(1, token);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return Optional.of(new User(resultSet.getString("uuid"), resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("token"), resultSet.getInt("coins"), resultSet.getInt("packageCount"), resultSet.getString("bio"), resultSet.getString("image"), resultSet.getInt("elo"), resultSet.getInt("wins"), resultSet.getInt("losses")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }
}

