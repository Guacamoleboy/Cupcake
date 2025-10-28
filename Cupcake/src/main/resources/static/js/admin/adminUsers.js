document.addEventListener("DOMContentLoaded", () => {

    // Attributes
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    const hideUserBtn = document.getElementById("hideUserBtn");
    const searchUserBtn = document.getElementById("searchUserbtn");
    const deleteUser = document.getElementById("deleteUser");
    const createUser = document.getElementById("createUser");
    let resultDiv = document.getElementById("searchUser");

    // _________________________________________________________________________

    searchUserBtn.addEventListener("click", async () => {

        const userId = document.getElementById("searchuser-id").value.trim();
        const username = document.getElementById("searchusername").value.trim();
        const email = document.getElementById("searchEmail").value.trim();

        console.log(userId, username, email)

        if (!userId && !username && !email) {
            showNotification("Indtast mindst ét søgekriterie (ID, brugernavn eller email)!", "orange")
            return;
        }

        if (email && !emailRegex.test(email)) {
            showNotification("Indtast en gyldig emailadresse!", "orange");
            return;
        }

        try {

            const form = new FormData();
            if (userId) form.append("id", userId);
            if (username) form.append("username", username);
            if (email) form.append("email", email);

            const res = await fetch("/admin/searchUser", {

                method: "POST",
                body: form

            });

            const data = await res.json();


            if (!res.ok || !data) {

                showNotification("Brugeren blev ikke fundet!", "red")
                return;

            }

            resultDiv.innerHTML = `
                <div class="user-card">
                    <div class="user-header">
                        <h3>User ID: ${data.id}</h3>
                    </div>

                    <div class="user-info">
                        <p><strong>User Email:</strong> ${data.email}</p>
                        <p><strong>Username:</strong> ${data.username}</p>

                        <p><strong>User phone:</strong> ${data.phone || "Ikke oplyst"}</p>
                        <p><strong>User balance:</strong> ${data.balance} kr</p>

                        <p><strong>Role:</strong> ${data.role}</p><p>
                        <strong>Created at:</strong> ${new Date(data.createdAt).toLocaleString("da-DK")}</p>

                    </div>
                </div>
            `;

            hideUserBtn.style.display = "inline-block";
            searchUserBtn.style.display = "none";

        } catch (err) {
            console.error(err);
            showNotification("Der skete en fejl!", "red")
        }
    });

    // _________________________________________________________________________

    hideUserBtn.addEventListener("click", () => {

        resultDiv.innerHTML = "";
        hideUserBtn.style.display = "none";
        searchUserBtn.style.display = "inline-block";

    });

    // _________________________________________________________________________

    createUser.addEventListener("click", async () => {

        const password = document.getElementById("createuser-password").value.trim();
        const username = document.getElementById("createuser-username").value.trim();
        const email = document.getElementById("createuser-email").value.trim();

        if (!password && !username && !email) {
            showNotification("indtast mindst ét søgekriterie (password, brugernavn eller email)!", "orange")
            return;
        }

        if (email && !emailRegex.test(email)) {
            showNotification("Indtast en gyldig emailadresse!", "orange");
            return;
        }

        try {
            const form = new FormData();
            if (password) form.append("password", password);
            if (username) form.append("username", username);
            if (email) form.append("email", email);

            const res = await fetch("/admin/createUser", {
                method: "POST",
                body: form
            });

            const data = await res.json();

            if (!res.ok || !data) {
                showNotification("Du kunne ikke oprette denne bruger!", "red")
                return;
            }
            showNotification("Du oprettede brugeren "+ username +"!", "green")
        } catch (err) {
            console.error(err);
            showNotification("Der er sket en fejl!", "red")

        }
    })

    // _________________________________________________________________________

    deleteUser.addEventListener("click", async () => {

        const id = document.getElementById("deleteuser-id").value.trim();
        const username = document.getElementById("deleteuser-username").value.trim();
        const email = document.getElementById("deleteuser-email").value.trim();

        if (!id && !username && !email) {
            showNotification("indtast mindst ét kriterie (id, brugernavn eller email)!", "orange")
            return;
        }

        if (email && !emailRegex.test(email)) {
            showNotification("Indtast en gyldig emailadresse!", "orange");
            return;
        }

        try {
            const form = new FormData();
            if (id) form.append("id", id);
            if (username) form.append("username", username);
            if (email) form.append("email", email);

            const res = await fetch("/admin/deleteUser", {
                method: "POST",
                body: form
            });

            const data = await res.json();

            if (!res.ok || !data) {
                showNotification("Du kunne ikke slette denne bruger!", "red")
                return;
            }
            showNotification("Du slettede brugeren "+ data.username +" med ID: "+ data.id+" og email: "+ data.email+"!", "green")
        } catch (err) {
            console.error(err);
            showNotification("Der er sket en fejl!", "red")

        }
    })

});