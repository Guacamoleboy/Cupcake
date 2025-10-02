// Package
package dk.cupcake.mapper;

// Imports
import dk.cupcake.User;
import dk.cupcake.db.Database;

import java.sql.*;
import java.util.*;

public class UserMapper {

    // Attributes

    // _________________________________________________________
    // Gets a specific user from id

    public User getById(int id) throws SQLException {

        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return toUser(rs);
            } else {
                return null;
            }
        }
    }

    // _________________________________________________________
    // Creates a new user

    public void newUser(User user) throws SQLException {
        String sql = "INSERT INTO users (email, password_hash, role, username) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());
            stmt.setString(4, user.getUsername());

            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                user.setId(keys.getInt(1));
            }
        }
    }

    // _________________________________________________________
    // Updates a user

    public void update(User user) throws SQLException {
        String sql = "UPDATE users SET email = ?, password_hash = ?, role = ?, username = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());
            stmt.setString(4, user.getUsername());
            stmt.setInt(5, user.getId());

            stmt.executeUpdate();
        }
    }

    // _________________________________________________________
    // Deletes a specific user. Might need a 2 factor step to make sure its correct and not a mistake deletion!

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM users WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // _________________________________________________________
    // Gets all users.

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

    // _________________________________________________________
    // Act sort of like a toString() method by printing a user.

    private User toUser(ResultSet rs) throws SQLException {

        User u = new User();
        u.setId(rs.getInt("id"));
        u.setEmail(rs.getString("email"));
        u.setPassword(rs.getString("password_hash"));
        u.setRole(rs.getString("role"));
        u.setUsername(rs.getString("username"));
        u.setCreatedAt(rs.getTimestamp("created_at"));
        return u;

    }

} // UserMapper end