// Package
package dk.cupcake.controller;

// Imports
import dk.cupcake.entities.GalleryComment;
import dk.cupcake.entities.GalleryLike;
import dk.cupcake.entities.GalleryPost;
import dk.cupcake.entities.User;
import dk.cupcake.mapper.GalleryCommentMapper;
import dk.cupcake.mapper.GalleryLikeMapper;
import dk.cupcake.mapper.GalleryPostMapper;
import dk.cupcake.server.ThymeleafSetup;
import io.javalin.Javalin;
import io.javalin.http.Handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GalleryController {

    // Attributes
    private static final GalleryPostMapper postMapper = new GalleryPostMapper();
    private static final GalleryLikeMapper likeMapper = new GalleryLikeMapper();
    private static final GalleryCommentMapper commentMapper = new GalleryCommentMapper();

    // _______________________________________________

    public static void registerRoutes(Javalin app) {
        app.get("/galleri", getGalleryPage);
        app.post("/gallery/post", createPost);
        app.post("/gallery/like", toggleLike);
        app.post("/gallery/comment", addComment);
        app.post("/gallery/comment/delete", deleteComment);
        app.post("/gallery/post/delete", deletePost);
        app.get("/gallery/comments/{postId}", getComments);
    }

    // _______________________________________________

    public static Handler getGalleryPage = ctx -> {
        try {
            List<GalleryPost> posts = postMapper.getAll();
            
            Map<Integer, Integer> likeCounts = new HashMap<>();
            Map<Integer, Integer> commentCounts = new HashMap<>();
            Map<Integer, Boolean> userLikes = new HashMap<>();
            
            User user = ctx.sessionAttribute("user");
            
            for (GalleryPost post : posts) {
                likeCounts.put(post.getId(), postMapper.getLikeCount(post.getId()));
                commentCounts.put(post.getId(), postMapper.getCommentCount(post.getId()));
                
                if (user != null) {
                    userLikes.put(post.getId(), likeMapper.hasUserLiked(post.getId(), user.getId()));
                }
            }
            
            Map<String, Object> model = new HashMap<>();
            model.put("posts", posts);
            model.put("likeCounts", likeCounts);
            model.put("commentCounts", commentCounts);
            model.put("userLikes", userLikes);
            model.put("user", user);
            
            ctx.html(ThymeleafSetup.render("galleri.html", model));
            
        } catch (Exception e) {
            ctx.redirect("/galleri?error=dbError");
        }
    };

    // _______________________________________________

    public static Handler createPost = ctx -> {
        User user = ctx.sessionAttribute("user");
        
        if (user == null) {
            ctx.redirect("/login?error=notLoggedIn");
            return;
        }
        
        if (!user.getRole().equals("admin")) {
            ctx.redirect("/galleri?error=noPermission");
            return;
        }
        
        String imageUrl = ctx.formParam("imageUrl");
        String description = ctx.formParam("description");
        String sizeClass = ctx.formParam("sizeClass");
        
        if (imageUrl == null || imageUrl.isEmpty()) {
            ctx.redirect("/galleri?error=missingImageUrl");
            return;
        }
        
        if (description == null || description.isEmpty()) {
            ctx.redirect("/galleri?error=missingDescription");
            return;
        }
        
        try {
            GalleryPost post = new GalleryPost();
            post.setImageUrl(imageUrl);
            post.setDescription(description);
            post.setUserId(user.getId());
            post.setSizeClass(sizeClass != null ? sizeClass : "");
            
            postMapper.create(post);
            ctx.redirect("/galleri?success=postCreated");
            
        } catch (Exception e) {
            ctx.redirect("/galleri?error=500");
        }
    };

    // _______________________________________________

    public static Handler toggleLike = ctx -> {
        User user = ctx.sessionAttribute("user");
        
        if (user == null) {
            ctx.json(Map.of("success", false, "error", "notLoggedIn"));
            return;
        }
        
        String postIdStr = ctx.formParam("postId");
        
        if (postIdStr == null) {
            ctx.json(Map.of("success", false, "error", "missingPostId"));
            return;
        }
        
        try {
            int postId = Integer.parseInt(postIdStr);
            boolean hasLiked = likeMapper.hasUserLiked(postId, user.getId());
            
            if (hasLiked) {
                likeMapper.delete(postId, user.getId());
            } else {
                GalleryLike like = new GalleryLike();
                like.setPostId(postId);
                like.setUserId(user.getId());
                likeMapper.create(like);
            }
            
            int newLikeCount = postMapper.getLikeCount(postId);
            
            ctx.json(Map.of(
                "success", true,
                "liked", !hasLiked,
                "likeCount", newLikeCount
            ));
            
        } catch (Exception e) {
            ctx.json(Map.of("success", false, "error", "500"));
        }
    };

    // _______________________________________________

    public static Handler addComment = ctx -> {
        User user = ctx.sessionAttribute("user");
        
        if (user == null) {
            ctx.json(Map.of("success", false, "error", "notLoggedIn"));
            return;
        }
        
        String postIdStr = ctx.formParam("postId");
        String commentText = ctx.formParam("commentText");
        
        if (postIdStr == null || commentText == null || commentText.trim().isEmpty()) {
            ctx.json(Map.of("success", false, "error", "missingFields"));
            return;
        }
        
        try {
            int postId = Integer.parseInt(postIdStr);
            
            GalleryComment comment = new GalleryComment();
            comment.setPostId(postId);
            comment.setUserId(user.getId());
            comment.setCommentText(commentText.trim());
            
            commentMapper.create(comment);
            
            int newCommentCount = postMapper.getCommentCount(postId);
            
            ctx.json(Map.of(
                "success", true,
                "commentCount", newCommentCount
            ));
            
        } catch (Exception e) {
            ctx.json(Map.of("success", false, "error", "500"));
        }
    };

    // _______________________________________________

    public static Handler getComments = ctx -> {
        String postIdStr = ctx.pathParam("postId");
        
        try {
            int postId = Integer.parseInt(postIdStr);
            List<GalleryComment> comments = commentMapper.getByPostId(postId);
            
            ctx.json(Map.of(
                "success", true,
                "comments", comments
            ));
            
        } catch (Exception e) {
            ctx.json(Map.of("success", false, "error", "500"));
        }
    };

    // _______________________________________________

    public static Handler deleteComment = ctx -> {
        User user = ctx.sessionAttribute("user");
        
        if (user == null) {
            ctx.json(Map.of("success", false, "error", "notLoggedIn"));
            return;
        }
        
        String commentIdStr = ctx.formParam("commentId");
        
        if (commentIdStr == null) {
            ctx.json(Map.of("success", false, "error", "missingCommentId"));
            return;
        }
        
        try {
            int commentId = Integer.parseInt(commentIdStr);
            GalleryComment comment = commentMapper.getById(commentId);
            
            if (comment == null) {
                ctx.json(Map.of("success", false, "error", "commentNotFound"));
                return;
            }
            
            if (comment.getUserId() != user.getId() && !user.getRole().equals("admin")) {
                ctx.json(Map.of("success", false, "error", "noPermission"));
                return;
            }
            
            commentMapper.delete(commentId);
            
            int newCommentCount = postMapper.getCommentCount(comment.getPostId());
            
            ctx.json(Map.of(
                "success", true,
                "commentCount", newCommentCount
            ));
            
        } catch (Exception e) {
            ctx.json(Map.of("success", false, "error", "500"));
        }
    };

    // _______________________________________________

    public static Handler deletePost = ctx -> {
        User user = ctx.sessionAttribute("user");
        
        if (user == null) {
            ctx.redirect("/login?error=notLoggedIn");
            return;
        }
        
        if (!user.getRole().equals("admin")) {
            ctx.redirect("/galleri?error=noPermission");
            return;
        }
        
        String postIdStr = ctx.formParam("postId");
        
        if (postIdStr == null) {
            ctx.redirect("/galleri?error=missingPostId");
            return;
        }
        
        try {
            int postId = Integer.parseInt(postIdStr);
            postMapper.delete(postId);
            ctx.redirect("/galleri?success=postDeleted");
            
        } catch (Exception e) {
            ctx.redirect("/galleri?error=500");
        }
    };

} // GalleryController end
