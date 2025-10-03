document.addEventListener('DOMContentLoaded', function() {
            
    const heartButtons = document.querySelectorAll('.heart-btn');
            
            heartButtons.forEach(button => {
                button.addEventListener('click', function(e) {
                    
                    this.classList.toggle('liked');
                   
                    const icon = this.querySelector('i');
                    if (this.classList.contains('liked')) {
                        icon.className = 'fa-solid fa-heart';
                    } else {
                        icon.className = 'fa-regular fa-heart';
                    }
                });
            });
});