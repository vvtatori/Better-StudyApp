const API_BASE = window.env.BASE_URL;

async function submit() {
    let firstPass = document.getElementById("emailinput");
    let secondPass = document.getElementById("usercode");    
    let message = document.getElementById("approval");

    if (!firstPass || !secondPass || !message) {
        console.error("One or more elements are missing.");
        return;
    }

    if (firstPass.value === secondPass.value) {
        message.innerHTML = "Your password change was successful";
        await sendToBackend(firstPass.value);
    } else {
        message.innerHTML = "Your password change was unsuccessful";
        firstPass.value = "";
        secondPass.value = "";
    }
}

async function sendToBackend(password) {
    const storedEmail = localStorage.getItem("storedEmail");

    if (!storedEmail || !password) {
        console.error("Email or password is missing.");
        return;
    }

    try {
        const response = await fetch(`${API_BASE}/api/getPass`, {
            method: "POST",
            headers: { "Content-Type": "application/x-www-form-urlencoded" },
            body: new URLSearchParams({ storedEmail, password })
        });

        const data = await response.text();
        
        window.location.href = "/landing.html";
        
        console.log("Backend response:", data);
    } catch (error) {
        console.error("Error sending data to backend:", error);
    }
}
