package pl.danielw.entity;

import org.mindrot.jbcrypt.BCrypt;
import pl.danielw.DbUtil;
import java.sql.*;
import java.util.Arrays;

public class UserDAO {
    private UserDAO() {
    }

    private static final String CREATE_USER_SQL_QUERY = "INSERT INTO users(email, username, password) VALUES (?, ?, ?);";
    private static final String READ_USER_BY_ID_SQL_QUERY = "SELECT * FROM users WHERE id = ?;";
    private static final String READ_USER_BY_EMAIL_SUBSTRING_SQL_QUERY = "SELECT * FROM users WHERE email LIKE ?;";
    private static final String READ_ALL_USERS_SQL_QUERY = "SELECT * FROM users;";
    private static final String UPDATE_USER_SQL_QUERY = "UPDATE users SET\n" +
            "email = ?,\n" +
            "username = ?,\n" +
            "password = ?\n" +
            "WHERE id = ?;";
    private static final String DELETE_USER_SQL_QUERY = "DELETE FROM users WHERE id = ?;";

    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static User create(User user) {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps =
                     conn.prepareStatement(CREATE_USER_SQL_QUERY, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getUserName());
            ps.setString(3, hashPassword(user.getEmail()));
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next(); // ResultSet is an iterator - moving from 0 to 1st
            int generatedUserID = rs.getInt(1);
            user.setId(generatedUserID);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static User[] readAll() {
        User[] users = new User[0];
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(READ_ALL_USERS_SQL_QUERY)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                User u = new User(rs.getInt("id"), rs.getString("email"),
                        rs.getString("username"), rs.getString("password"));
                users = addUserToArray(u, users);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    public static User readById(int id) {
        User u = null;
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(READ_USER_BY_ID_SQL_QUERY)) {
            ps.setString(1, String.valueOf(id));
            ResultSet rs = ps.executeQuery();
            rs.next();
            u = new User(rs.getInt("id"), rs.getString("email"),
                    rs.getString("username"), rs.getString("password"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return u;
    }

    public static User[] readByEmail(String emailSubstring) {
        User[] users = new User[0];
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(READ_USER_BY_EMAIL_SUBSTRING_SQL_QUERY)) {
            ps.setString(1, "%" + emailSubstring + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User u = new User(rs.getInt("id"), rs.getString("email"),
                        rs.getString("username"), rs.getString("password"));
                users = addUserToArray(u, users);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    public static void update(User user) {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_USER_SQL_QUERY)) {
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getUserName());
            ps.setString(3, hashPassword(user.getPassword()));
            ps.setString(4, String.valueOf(user.getId()));
            ps.executeUpdate();
            System.out.println("Update successful!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void delete(int id) {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(DELETE_USER_SQL_QUERY)) {
            ps.setString(1, String.valueOf(id));
            ps.executeUpdate();
            System.out.println("User deleted successfully!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static User[] addUserToArray(User u, User[] users) {
        User[] tmpUsers = Arrays.copyOf(users, users.length + 1);
        tmpUsers[users.length] = u;
        return tmpUsers;
    }
}
