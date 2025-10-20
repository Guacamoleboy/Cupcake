// Package
package dk.cupcake.mapper;

// Imports
import dk.cupcake.db.Database;
import dk.cupcake.entities.GalleryComment;
import dk.cupcake.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GalleryCommentMapper {

    // Attributes

    // ________________________________________________________________

    public List<GalleryComment> getByPostId(int postId) throws SQLException {
        List<GalleryComment> comments = new ArrayList<>();
        String sql = """
                SELECT gc.*, u.username 
                FROM gallery_comments gc 
                JOIN users u ON gc.user_id = u.id 
                WHERE gc.post_id = ? 
                ORDER BY gc.created_at DESC
                """;
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                comments.add(toGalleryComment(rs));
            }
        }
        return comments;
    }

    // ________________________________________________________________

    public GalleryComment getById(int id) throws SQLException {
        String sql = """
                SELECT gc.*, u.username 
                FROM gallery_comments gc 
                JOIN users u ON gc.user_id = u.id 
                WHERE gc.id = ?
                """;
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return toGalleryComment(rs);
            }
            return null;
        }
    }

    // ________________________________________________________________

    public void create(GalleryComment comment) throws DatabaseException {
        String sql = "INSERT INTO gallery_comments (post_id, user_id, comment_text) VALUES (?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, comment.getPostId());
            stmt.setInt(2, comment.getUserId());
            stmt.setString(3, comment.getCommentText());

            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                comment.setId(keys.getInt(1));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Kunne ikke tilføje kommentar", e.getMessage());
        }
    }

    // ________________________________________________________________

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM gallery_comments WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // ________________________________________________________________

    private GalleryComment toGalleryComment(ResultSet rs) throws SQLException {
        GalleryComment comment = new GalleryComment();
        comment.setId(rs.getInt("id"));
        comment.setPostId(rs.getInt("post_id"));
        comment.setUserId(rs.getInt("user_id"));
        comment.setUsername(rs.getString("username"));
        comment.setCommentText(rs.getString("comment_text"));
        comment.setCreatedAt(rs.getTimestamp("created_at"));
        return comment;
    }

} // GalleryCommentMapper end
