
const API_BASE = window.env.BASE_URL;

function toggleMode() {
    let body = document.body;
    let lamp = document.querySelector(".lamp");
    let themeLink = document.getElementById('theme-css');
    let isLightMode = body.classList.contains("light-mode");

    // If in light mode, switch to dark mode
    if (isLightMode) {
        body.classList.remove("light-mode");
        lamp.src = "/images/lamp-dark.png";
        themeLink.setAttribute('href', 'dark.css');
        localStorage.setItem("theme", "dark");
    } else {
        body.classList.add("light-mode");
        lamp.src = "/images/lamp-light.png";  // Light theme lamp image
        themeLink.setAttribute('href', 'light.css');  // Switch to light theme CSS
        localStorage.setItem("theme", "light");
    }
}

//this is an event listener to toggle theme and change the lamp image when checkbox is changed
document.addEventListener('DOMContentLoaded', function () {
    const themeToggle = document.querySelector('.switch input');
    if (themeToggle) {
        themeToggle.addEventListener('change', toggleMode);
    }

    // Set theme based on localStorage
    if (localStorage.getItem("theme") === "light") {
        document.body.classList.add("light-mode");
        document.querySelector(".lamp").src = "/images/lamp-light.png";
        document.getElementById('theme-css').setAttribute('href', 'light.css');
    } else {
        document.body.classList.remove("light-mode");
        document.querySelector(".lamp").src = "/images/lamp-dark.png";
        document.getElementById('theme-css').setAttribute('href', 'dark.css');
    }
});
//added this here because app.js is overloaded with code

function switchWindow(){
 window.location.href = `/home.html`;   
}
    
