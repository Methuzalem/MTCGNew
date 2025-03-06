package Application.MTCG.repositorys;

import Application.MTCG.data.ConnectionPool;
import Application.MTCG.dto.ShowStatsDTO;
import Application.MTCG.entity.User;
import Application.MTCG.exceptions.InvalidUserData;

import java.sql.ResultSet;
import java.util.Optional;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;


public class UserRepo {
    private final static String NEW_USER = "INSERT INTO users (uuid, username, password) VALUES (?, ?, ?)";
    private final static String FIND_USER_BY_NAME = "SELECT * FROM users WHERE username = ?";
    private final static String UPDATE_USER_BY_UUID = "UPDATE users SET username = ?, password = ?, token = ?, coins = ?, packagecount = ?, name =?, bio = ?, image = ?, elo = ?, wins = ?, losses = ? WHERE uuid = ?";
    private final static String FIND_USER_BY_TOKEN = "SELECT * FROM users WHERE token = ?";
    private final static String GET_USER_STATS = "SELECT name, elo, wins, losses FROM users WHERE uuid = ?";
    private final static String GET_ALL_USER_STATS = "SELECT name, elo, wins, losses FROM users";
    private final ConnectionPool connectionPool;


    public UserRepo(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public User save(User user) {
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(NEW_USER);
        ) {
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
                return Optional.of(new User(resultSet.getString("uuid"), resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("token"), resultSet.getInt("coins"), resultSet.getInt("packageCount"), resultSet.getString("name"), resultSet.getString("bio"), resultSet.getString("image"), resultSet.getInt("elo"), resultSet.getInt("wins"), resultSet.getInt("losses")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public void updateUserByUuid(User user) {
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_USER_BY_UUID)
        ) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getToken());
            preparedStatement.setInt(4, user.getCoins());
            preparedStatement.setInt(5, user.getPackageCount());
            preparedStatement.setString(6, user.getName());
            preparedStatement.setString(7, user.getBio());
            preparedStatement.setString(8, user.getImage());
            preparedStatement.setInt(9, user.getElo());
            preparedStatement.setInt(10, user.getWins());
            preparedStatement.setInt(11, user.getLosses());
            preparedStatement.setString(12, user.getUuid());
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
                return Optional.of(new User(resultSet.getString("uuid"), resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("token"), resultSet.getInt("coins"), resultSet.getInt("packageCount"), resultSet.getString("name"), resultSet.getString("bio"), resultSet.getString("image"), resultSet.getInt("elo"), resultSet.getInt("wins"), resultSet.getInt("losses")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    public ShowStatsDTO getStatsOfUser(User user) {
        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(GET_USER_STATS)
        ) {
            preparedStatement.setString(1, user.getUuid());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new ShowStatsDTO(
                        resultSet.getString("name"),
                        String.valueOf(resultSet.getInt("elo")),
                        String.valueOf(resultSet.getInt("wins")),
                        String.valueOf(resultSet.getInt("losses"))
                );
            } else {
                throw new InvalidUserData("User stats not found");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public List<ShowStatsDTO> getAllUserStats() {
        List<ShowStatsDTO> userStatsList = new ArrayList<>();

        try (
                Connection connection = connectionPool.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_USER_STATS);
                ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            while (resultSet.next()) {
                ShowStatsDTO stats = new ShowStatsDTO(
                        String.valueOf(resultSet.getString("name")),
                        String.valueOf(resultSet.getInt("elo")),
                        String.valueOf(resultSet.getInt("wins")),
                        String.valueOf(resultSet.getInt("losses"))
                );
                userStatsList.add(stats);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving user stats", e);
        }
        return userStatsList;
    }
}

