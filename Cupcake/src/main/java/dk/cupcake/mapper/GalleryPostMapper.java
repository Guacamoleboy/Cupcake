// Package
package dk.cupcake.mapper;

// Imports
import dk.cupcake.db.Database;
import dk.cupcake.entities.GalleryPost;
import dk.cupcake.exceptions.DatabaseException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GalleryPostMapper {

    // Attributes

    // ________________________________________________________________

    public List<GalleryPost> getAll() throws SQLException {
        List<GalleryPost> posts = new ArrayList<>();
        String sql = "SELECT * FROM gallery_posts ORDER BY created_at DESC";
        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                posts.add(toGalleryPost(rs));
            }
        }
        return posts;
    }

    // ________________________________________________________________

    public GalleryPost getById(int id) throws SQLException {
        String sql = "SELECT * FROM gallery_posts WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return toGalleryPost(rs);
            }
            return null;
        }
    }

    // ________________________________________________________________

    public void create(GalleryPost post) throws DatabaseException {
        String sql = "INSERT INTO gallery_posts (image_url, description, user_id, size_class) VALUES (?, ?, ?, ?)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, post.getImageUrl());
            stmt.setString(2, post.getDescription());
            stmt.setInt(3, post.getUserId());
            stmt.setString(4, post.getSizeClass());

            stmt.executeUpdate();

            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                post.setId(keys.getInt(1));
            }

        } catch (SQLException e) {
            throw new DatabaseException("Kunne ikke oprette galleri opslag", e.getMessage());
        }
    }

    // ________________________________________________________________

    public void update(GalleryPost post) throws SQLException {
        String sql = "UPDATE gallery_posts SET image_url = ?, description = ?, size_class = ? WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, post.getImageUrl());
            stmt.setString(2, post.getDescription());
            stmt.setString(3, post.getSizeClass());
            stmt.setInt(4, post.getId());
            stmt.executeUpdate();
        }
    }

    // ________________________________________________________________

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM gallery_posts WHERE id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // ________________________________________________________________

    public int getLikeCount(int postId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM gallery_likes WHERE post_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }

    // ________________________________________________________________

    public int getCommentCount(int postId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM gallery_comments WHERE post_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, postId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return 0;
        }
    }

    // ________________________________________________________________

    private GalleryPost toGalleryPost(ResultSet rs) throws SQLException {
        GalleryPost post = new GalleryPost();
        post.setId(rs.getInt("id"));
        post.setImageUrl(rs.getString("image_url"));
        post.setDescription(rs.getString("description"));
        post.setUserId(rs.getInt("user_id"));
        post.setCreatedAt(rs.getTimestamp("created_at"));
        post.setSizeClass(rs.getString("size_class"));
        return post;
    }

} // GalleryPostMapper end
