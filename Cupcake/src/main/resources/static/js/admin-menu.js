document.addEventListener('DOMContentLoaded', () => {

    // Attributes
    const buttons = document.querySelectorAll('.guac-btn-profile');
    const sections = document.querySelectorAll('.profile-actual');
    const addBtn = document.getElementById("addBalanceBtn");

    // _____________________________________________________________________________________________

    function hideAllSections() {
        sections.forEach(sec => {
            sec.style.display = 'none';
        });
    }

    // _____________________________________________________________________________________________

    function showSection(id) {
        const section = document.getElementById(id);
        if (section) {
            section.style.display = 'block';
            return true;
        }
        return false;
    }

    // _____________________________________________________________________________________________

    function getFirstSectionId() {
        if (document.getElementById('brugere')) return 'brugere';
        if (document.getElementById('minProfil')) return 'minProfil';
        return sections.length > 0 ? sections[0].id : null;
    }

    // _____________________________________________________________________________________________

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

    // ____________________________________________________________________________________________

    addBtn.addEventListener("click", async () => {

        // Gets our input first
        const username = document.querySelector("#addMoneyAdmin input[placeholder='Brugernavn...']").value.trim();
        const id = document.querySelector("#addMoneyAdmin input[placeholder='ID...']").value.trim();
        const amount = document.querySelector("#addMoneyAdmin input[placeholder='Beløb...']").value.trim();

        // Validation
        if ((!username && !id) || (username && id)) {
            showNotification("Please only fill out one of the fields", "orange")
            return;
        }

        // Checks if there's an amount entered
        if (!amount) {
            showNotification("Add amount", "orange")
            return;
        }

        // Execute if pass
        try {

            const formData = new URLSearchParams();
            formData.append("username", username);
            formData.append("id", id);
            formData.append("amount", amount);

            const res = await fetch("/admin/add-balance", {
                method: "POST",
                body: formData
            });

            // Very important. Will NOT redirect without.
            if (res.redirected) {
                window.location.href = res.url;
            } else {
                const text = await res.text(); alert(text);
            }

        } catch (err) {
            console.error(err);
            showNotification("Connection error", "red")
        }
    });

});