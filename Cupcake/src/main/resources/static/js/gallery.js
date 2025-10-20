// like toggle
// _______________________________________________________________________
function toggleLike(postId, button) {
    const formData = new FormData();
    formData.append('postId', postId);

    fetch('/gallery/like', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            const icon = button.querySelector('i');
            const likeCountSpan = button.closest('.gallery-item').querySelector('.like-count');
            
            if (data.liked) {
                icon.classList.remove('fa-regular');
                icon.classList.add('fa-solid');
                button.classList.add('liked');
            } else {
                icon.classList.remove('fa-solid');
                icon.classList.add('fa-regular');
                button.classList.remove('liked');
            }
            
            likeCountSpan.textContent = data.likeCount;
        } else {
            if (data.error === 'notLoggedIn') {
                showNotification("Du skal være logget ind for at like", "orange");
            } else {
                showNotification("Kunne ikke like opslaget", "red");
            }
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showNotification("Der opstod en fejl", "red");
    });
}
// vis kommentarer
// _______________________________________________________________________
function showComments(postId) {
    const modal = document.getElementById('comments-modal');
    const commentsList = document.getElementById('comments-list');
    const commentForm = document.getElementById('comment-form');
    
    modal.dataset.postId = postId;
    modal.style.display = 'flex';
    
    // Load comments
    fetch(`/gallery/comments/${postId}`)
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            renderComments(data.comments);
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showNotification("Kunne ikke indlæse kommentarer", "red");
    });
}
// render
// _______________________________________________________________________
function renderComments(comments) {
    const commentsList = document.getElementById('comments-list');
    
    if (comments.length === 0) {
        commentsList.innerHTML = '<p class="no-comments">Ingen kommentarer endnu. Vær den første til at kommentere!</p>';
        return;
    }
    
    commentsList.innerHTML = comments.map(comment => `
        <div class="comment-item" data-comment-id="${comment.id}">
            <div class="comment-header">
                <strong>${comment.username}</strong>
                <span class="comment-time">${formatDate(comment.createdAt)}</span>
            </div>
            <p class="comment-text">${comment.commentText}</p>
        </div>
    `).join('');
}
// luk modal
// _______________________________________________________________________
function closeCommentsModal() {
    const modal = document.getElementById('comments-modal');
    modal.style.display = 'none';
    document.getElementById('comment-input').value = '';
}
// tilføj kommentar
// _______________________________________________________________________
function submitComment() {
    const modal = document.getElementById('comments-modal');
    const postId = modal.dataset.postId;
    const commentInput = document.getElementById('comment-input');
    const commentText = commentInput.value.trim();
    
    if (!commentText) {
        showNotification("Skriv en kommentar", "orange");
        return;
    }
    
    const formData = new FormData();
    formData.append('postId', postId);
    formData.append('commentText', commentText);
    
    fetch('/gallery/comment', {
        method: 'POST',
        body: formData
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            showNotification("Kommentar tilføjet!", "green");
            commentInput.value = '';
            
            // Update comment count
            const commentCountSpan = document.querySelector(`[data-post-id="${postId}"] .comment-count`);
            if (commentCountSpan) {
                commentCountSpan.textContent = data.commentCount;
            }
            
            // Reload comments
            showComments(postId);
        } else {
            if (data.error === 'notLoggedIn') {
                showNotification("Du skal være logget ind for at kommentere", "orange");
            } else {
                showNotification("Kunne ikke tilføje kommentar", "red");
            }
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showNotification("Der opstod en fejl", "red");
    });
}
// dato format - chatgpt 
// _______________________________________________________________________
function formatDate(timestamp) {
    const date = new Date(timestamp);
    const now = new Date();
    const diffMs = now - date;
    const diffMins = Math.floor(diffMs / 60000);
    const diffHours = Math.floor(diffMs / 3600000);
    const diffDays = Math.floor(diffMs / 86400000);
    
    if (diffMins < 1) return 'Lige nu';
    if (diffMins < 60) return `${diffMins} min siden`;
    if (diffHours < 24) return `${diffHours} timer siden`;
    if (diffDays < 7) return `${diffDays} dage siden`;
    
    return date.toLocaleDateString('da-DK', { day: 'numeric', month: 'short', year: 'numeric' });
}
// klik udenfor
// _______________________________________________________________________
window.onclick = function(event) {
    const modal = document.getElementById('comments-modal');
    if (event.target === modal) {
        closeCommentsModal();
    }
}
// enter klik
// _______________________________________________________________________
document.addEventListener('DOMContentLoaded', function() {
    const commentInput = document.getElementById('comment-input');
    if (commentInput) {
        commentInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter' && !e.shiftKey) {
                e.preventDefault();
                submitComment();
            }
        });
    }
});

