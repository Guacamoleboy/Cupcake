// Package
package dk.cupcake.mapper;

// Imports
import dk.cupcake.entities.User;
import dk.cupcake.exceptions.DatabaseException;
import dk.cupcake.db.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    // ________________________________________________________________

    public User getById(int id) throws SQLException {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return toUser(rs);
            }
            return null;
        }
    }

    // ________________________________________________________________

    public User getByUserName(String username) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return toUser(rs);
            }
            return null;
        }
    }

    // ________________________________________________________________

    public void newUser(User user) throws DatabaseException {
        String sql = "INSERT INTO users (email, password_hash, role, username) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, user.getRole());
            stmt.setString(4, user.getUsername());

            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                user.setId(keys.getInt(1));
            }

        } catch (SQLException e) {
            String msg = "Databasefejl – kontakt admin";
            if (e.getMessage().contains("duplicate key")) {
                msg = "Brugernavn eller email findes allerede";
            }
            throw new DatabaseException(msg, e.getMessage());
        }
    }

    // ________________________________________________________________

    public void update(User user) throws SQLException {
        String sql = "UPDATE users SET email = ?, password_hash = ?, role = ?, username = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, user.getRole());
            stmt.setString(4, user.getUsername());
            stmt.setInt(5, user.getId());
            stmt.executeUpdate();
        }
    }

    // ________________________________________________________________

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // ________________________________________________________________

    public List<User> getAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                users.add(toUser(rs));
            }
        }
        return users;
    }

    // ________________________________________________________________

    private User toUser(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setEmail(rs.getString("email"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setRole(rs.getString("role"));
        u.setUsername(rs.getString("username"));
        u.setCreatedAt(rs.getTimestamp("created_at"));
        return u;
    }

    // ________________________________________________________________

    public User login(String username, String passwordHash) throws DatabaseException {
        String sql = "SELECT * FROM users WHERE username = ? AND password_hash = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, passwordHash);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return toUser(rs);
            } else {
                throw new DatabaseException("Forkert brugernavn eller adgangskode");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Databasefejl – kontakt admin", e.getMessage());
        }
    }

    // ________________________________________________________________


    public boolean existsByEmailOrUsername(String email, String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ? OR username = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

} // UserMapper end