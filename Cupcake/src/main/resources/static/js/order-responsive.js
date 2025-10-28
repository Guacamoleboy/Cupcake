document.querySelectorAll('.cupcake-imgBox').forEach(imgBox => {
    imgBox.addEventListener('touchend', function (e) {
        e.preventDefault();
        e.stopPropagation();
        const card = this.closest('.cupcake-card');
        card.classList.toggle('active');
        document.querySelectorAll('.cupcake-card').forEach(c => {
            if (c !== card) c.classList.remove('active');
        });
    });
});