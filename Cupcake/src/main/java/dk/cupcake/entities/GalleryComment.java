// Package
package dk.cupcake.entities;

// Imports
import java.sql.Timestamp;

public class GalleryComment {

    // Attributes
    private int id;
    private int postId;
    private int userId;
    private String username;
    private String commentText;
    private Timestamp createdAt;

    // ___________________________________________________

    public int getId() {
        return id;
    }

    // ___________________________________________________

    public void setId(int id) {
        this.id = id;
    }

    // ___________________________________________________

    public int getPostId() {
        return postId;
    }

    // ___________________________________________________

    public void setPostId(int postId) {
        this.postId = postId;
    }

    // ___________________________________________________

    public int getUserId() {
        return userId;
    }

    // ___________________________________________________

    public void setUserId(int userId) {
        this.userId = userId;
    }

    // ___________________________________________________

    public String getUsername() {
        return username;
    }

    // ___________________________________________________

    public void setUsername(String username) {
        this.username = username;
    }

    // ___________________________________________________

    public String getCommentText() {
        return commentText;
    }

    // ___________________________________________________

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    // ___________________________________________________

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    // ___________________________________________________

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

} // GalleryComment end
