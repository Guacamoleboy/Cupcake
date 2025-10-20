// Package
package dk.cupcake.entities;

// Imports
import java.sql.Timestamp;

public class GalleryPost {

    // Attributes
    private int id;
    private String imageUrl;
    private String description;
    private int userId;
    private Timestamp createdAt;
    private String sizeClass;

    // ___________________________________________________

    public int getId() {
        return id;
    }

    // ___________________________________________________

    public void setId(int id) {
        this.id = id;
    }

    // ___________________________________________________

    public String getImageUrl() {
        return imageUrl;
    }

    // ___________________________________________________

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    // ___________________________________________________

    public String getDescription() {
        return description;
    }

    // ___________________________________________________

    public void setDescription(String description) {
        this.description = description;
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

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    // ___________________________________________________

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    // ___________________________________________________

    public String getSizeClass() {
        return sizeClass;
    }

    // ___________________________________________________

    public void setSizeClass(String sizeClass) {
        this.sizeClass = sizeClass;
    }

} // GalleryPost end
