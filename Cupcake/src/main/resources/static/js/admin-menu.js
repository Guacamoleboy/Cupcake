document.addEventListener('DOMContentLoaded', () => {
    const buttons = document.querySelectorAll('.guac-btn-profile');
    const sections = document.querySelectorAll('.profile-actual');

    function hideAllSections() {
        sections.forEach(sec => {
            sec.style.display = 'none';
        });
    }

    function showSection(id) {
        const section = document.getElementById(id);
        if(section) section.style.display = 'block';
    }

    hideAllSections();
    showSection('brugere');

    buttons.forEach(btn => {
        btn.addEventListener('click', (e) => {
            e.preventDefault();
            const target = btn.dataset.section;

            hideAllSections();
            showSection(target);

            // Active button styling
            buttons.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
        });
    });
});