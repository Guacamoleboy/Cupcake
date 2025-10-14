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
        if (section) {
            section.style.display = 'block';
            return true;
        }
        return false;
    }

    // Fix for admin / profile instead of only being admin.html
    function getFirstSectionId() {
        if (document.getElementById('brugere')) return 'brugere';
        if (document.getElementById('minProfil')) return 'minProfil';
        return sections.length > 0 ? sections[0].id : null;
    }

    hideAllSections();
    const firstSectionId = getFirstSectionId();
    if (firstSectionId) showSection(firstSectionId);

    const defaultButton = document.querySelector(`.guac-btn-profile[data-section="${firstSectionId}"]`);
    if (defaultButton) defaultButton.classList.add('active');

    buttons.forEach(btn => {
        btn.addEventListener('click', (e) => {
            e.preventDefault();
            const target = btn.dataset.section;

            hideAllSections();
            showSection(target);

            buttons.forEach(b => b.classList.remove('active'));
            btn.classList.add('active');
        });
    });
});