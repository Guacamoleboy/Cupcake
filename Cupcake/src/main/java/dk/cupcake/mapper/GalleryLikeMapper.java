// Package
package dk.cupcake.mapper;

// Imports
import dk.cupcake.db.Database;
import dk.cupcake.entities.GalleryLike;
import dk.cupcake.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GalleryLikeMapper {

    // Attributes

    // ________________________________________________________________

    public List<GalleryLike> getByPostId(int postId) throws SQLException {
        List<GalleryLike> likes = new ArrayList<>();
        String sql = "SELECT * FROM gallery_likes WHERE post_id = ? ORDER BY created_at DESC";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                likes.add(toGalleryLike(rs));
            }
        }
        return likes;
    }

    // ________________________________________________________________

    public boolean hasUserLiked(int postId, int userId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM gallery_likes WHERE post_id = ? AND user_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            stmt.setInt(2, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            return false;
        }
    }

    // ________________________________________________________________

    public void create(GalleryLike like) throws DatabaseException {
        String sql = "INSERT INTO gallery_likes (post_id, user_id) VALUES (?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, like.getPostId());
            stmt.setInt(2, like.getUserId());

            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                like.setId(keys.getInt(1));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Kunne ikke tilføje like", e.getMessage());
        }
    }

    // ________________________________________________________________

    public void delete(int postId, int userId) throws SQLException {
        String sql = "DELETE FROM gallery_likes WHERE post_id = ? AND user_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        }
    }

    // ________________________________________________________________

    private GalleryLike toGalleryLike(ResultSet rs) throws SQLException {
        GalleryLike like = new GalleryLike();
        like.setId(rs.getInt("id"));
        like.setPostId(rs.getInt("post_id"));
        like.setUserId(rs.getInt("user_id"));
        like.setCreatedAt(rs.getTimestamp("created_at"));
        return like;
    }

} // GalleryLikeMapper end
