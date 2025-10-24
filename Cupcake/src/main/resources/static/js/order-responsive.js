document.querySelectorAll('.cupcake-card').forEach(card => {
    card.addEventListener('touchend', function(e) {
        e.preventDefault();
        console.log('Kort trykket!', card);
        card.classList.toggle('active');
        document.querySelectorAll('.cupcake-card').forEach(c => {
            if (c !== card) c.classList.remove('active');
        });
    });
});