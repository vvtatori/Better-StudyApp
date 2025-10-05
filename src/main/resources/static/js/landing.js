const API_BASE = window.env.BASE_URL;

let email = "";
let password = "";
let firstName = null;
let lastName = null;
let username = null;
let loggedIn = false;
let error = "";


// Load all relevant stats data
const statsData = {
  totalMessages: 5900,
  messagesLast24h: 160,
  totalPosts: 78,
  studySessions: 34
};

// Mapping the IDs
const statsMap = {
  "total-messages": "totalMessages",
  "messages-24h": "messagesLast24h",
  "total-posts": "totalPosts",
  "study-sessions": "studySessions"
};

// Rolling effect for stats 
function animateNumbers(id, start, end, duration) {
  let range = end - start;
  let current = start;
  let increment = range / (duration / 5); 
  let element = document.getElementById(id);

  function updateNumber() {
    current += increment;
    if (current >= end) {
      element.textContent = Math.floor(end);
    } else {
      element.textContent = Math.floor(current);
      requestAnimationFrame(updateNumber);
    }
  }
  updateNumber();
}

// Animate stats when page loads (also acts as a refresh to load latest stats)
document.addEventListener("DOMContentLoaded", function () {
  Object.keys(statsMap).forEach(id => {
    if (document.getElementById(id)) {
      animateNumbers(id, 0, statsData[statsMap[id]], 2000);
    }
  });
});

// Light/Dark Mode Toggle with Local Storage
function toggleMode() {
  let body = document.body;
  let lamp = document.querySelector(".lamp");
  let isLightMode = body.classList.contains("light-mode");

  if (isLightMode) {
    body.classList.remove("light-mode");
    lamp.src = "/images/lamp-dark.png";
    localStorage.setItem("theme", "dark");
  } else {
    body.classList.add("light-mode");
    lamp.src = "/images/lamp-light.png";
    localStorage.setItem("theme", "light");
  }
}

// Adds the theme to Local sStorage
document.addEventListener("DOMContentLoaded", function () {
  if (localStorage.getItem("theme") === "light") {
    document.body.classList.add("light-mode");
    document.querySelector(".lamp").src = "/images/lamp-light.png";
  }
});

// Scroll to Stats
function scrollToStats() {
  document.getElementById("stats").scrollIntoView({ behavior: "smooth" });
}

// Animations
document.addEventListener("DOMContentLoaded", function () {
  const animatedElements = document.querySelectorAll(".title, .motto, .buttons, .scroll-arrow, .stats-container, .footer");

  setTimeout(() => {
    animatedElements.forEach(el => {
      el.style.opacity = "0";
    });
  }, 100);
});

// Load Policy from .txt file
document.addEventListener("DOMContentLoaded", function () {
  fetch("/policies.txt")
    .then(response => response.text())
    .then(text => {
      document.getElementById("policy-content").textContent = text;
    })
    .catch(error => console.error("Error loading policies:", error));
});
 // Open & Close Signup Popup
function openSignupPopup() {
    document.getElementById('signupPopup').style.display = 'flex';
}

function closeSignupPopup() {
    document.getElementById('signupPopup').style.display = 'none';
}

// Open & Close Login Popup
function openLoginPopup() {
    document.getElementById('loginPopup').style.display = 'flex';
}

function closeLoginPopup() {
    document.getElementById('loginPopup').style.display = 'none';
}

document.querySelectorAll('.popup-overlay').forEach(popup => {
    popup.addEventListener('click', function(e) {
        if (e.target === this) this.style.display = 'none';
    });
});


document.getElementById('signupForm').addEventListener('submit', function(e) {
    e.preventDefault();
    signup();
});

async function signup() {
    let email = document.getElementById("email").value;
    let firstName = document.getElementById("firstName").value;
    let lastName = document.getElementById("lastName").value;
    let password = document.getElementById("password").value;
    let username = document.getElementById("username").value;
    console.log("username"+ username);
    console.log("password"+password);
    console.log("email"+email);

    const response = await fetch(`${API_BASE}/api/signup?email=${encodeURIComponent(email)}&firstName=${encodeURIComponent(firstName)}&lastName=${encodeURIComponent(lastName)}&password=${encodeURIComponent(password)}&username=${encodeURIComponent(username)}`, {
        method: 'GET',
    });

    const errorMsg = await response.text();
    document.getElementById("signup-error").textContent = errorMsg;
    
if(errorMsg === "User saved successfully"){
         window.location.href = "/landing.html";      
}
    if (!errorMsg) {
        closeSignupPopup();
        
        window.location.href = "/landing.html";
    }
}

//login
document.getElementById('loginForm').addEventListener('submit', function(e) {
    e.preventDefault();
    login();
});

async function login() {
    let username = document.getElementById("login-username").value;
    let password = document.getElementById("login-password").value;

    try {
        const response = await fetch(`${API_BASE}/api/login?username=${encodeURIComponent(username)}&password=${encodeURIComponent(password)}`, {
            method: 'GET',
        });
       
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }

        const result = await response.text();

        
        if (result === "success") {
            window.location.href = "/home.html";  // Redirect to home page
        } else {
            document.getElementById("login-error").textContent = "Incorrect username or password";

        }
    } catch (error) {
        console.error('Login failed:', error);
        document.getElementById("login-error").textContent = "Login failed. Please try again.";
    }
}


    
            