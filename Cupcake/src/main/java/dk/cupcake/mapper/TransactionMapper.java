// Package
package dk.cupcake.mapper;

// Imports
import dk.cupcake.db.Database;
import dk.cupcake.entites.Transaction;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionMapper {

    // _________________________________________________________
    // Gets a transaction by id

    public Transaction getById(int id) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return toTransaction(rs);
            return null;
        }
    }

    // _________________________________________________________
    // Gets all transactions

    public List<Transaction> getAll() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                transactions.add(toTransaction(rs));
            }
        }
        return transactions;
    }

    // _________________________________________________________
    // Gets transactions from a specific user

    public List<Transaction> getByUserId(int userId) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions WHERE user_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                transactions.add(toTransaction(rs));
            }
        }
        return transactions;
    }

    // _________________________________________________________
    // Create a new transaction

    public void newTransaction(Transaction t) throws SQLException {
        String sql = "INSERT INTO transactions (user_id, amount, description) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, t.getUserId());
            stmt.setDouble(2, t.getAmount());
            stmt.setString(3, t.getDescription());

            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                t.setId(keys.getInt(1));
            }
        }
    }

    // _________________________________________________________
    // Updates a transaction

    public void update(Transaction t) throws SQLException {
        String sql = "UPDATE transactions SET user_id = ?, amount = ?, description = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, t.getUserId());
            stmt.setDouble(2, t.getAmount());
            stmt.setString(3, t.getDescription());
            stmt.setInt(4, t.getId());

            stmt.executeUpdate();
        }
    }

    // _________________________________________________________
    // Deletes a transaction

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM transactions WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // _________________________________________________________
    // Act sort of like a toString() method by printing a transaction.

    private Transaction toTransaction(ResultSet rs) throws SQLException {
        Transaction t = new Transaction();
        t.setId(rs.getInt("id"));
        t.setUserId(rs.getInt("user_id"));
        t.setAmount(rs.getDouble("amount"));
        t.setCreatedAt(rs.getTimestamp("created_at"));
        t.setDescription(rs.getString("description"));
        return t;
    }

} // TransactionMapper End