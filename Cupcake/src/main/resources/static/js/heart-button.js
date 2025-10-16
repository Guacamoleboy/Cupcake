/*

    Saves heart-btn likes per gallery photo in localStorage
    Probably not the best solution, but for logged out users it should
    do the trick.

    - Guac.

*/

document.addEventListener('DOMContentLoaded', function() {

    const heartButtons = document.querySelectorAll('.heart-btn');
    const likedItems = JSON.parse(localStorage.getItem('likedGalleryItems')) || {};

    heartButtons.forEach((button, index) => {

        const img = button.closest('.gallery-item').querySelector('.gallery-item__img');
        const itemId = img ? img.getAttribute('src') : `item-${index}`;

        if (likedItems[itemId]) {
            button.classList.add('liked');
            const icon = button.querySelector('i');
            icon.className = 'fa-solid fa-heart';
        }

        button.addEventListener('click', function() {
            this.classList.toggle('liked');
            const icon = this.querySelector('i');

            if (this.classList.contains('liked')) {
                icon.className = 'fa-solid fa-heart';
                likedItems[itemId] = true;
            } else {
                icon.className = 'fa-regular fa-heart';
                delete likedItems[itemId];
            }

            localStorage.setItem('likedGalleryItems', JSON.stringify(likedItems));
        });

    });

});