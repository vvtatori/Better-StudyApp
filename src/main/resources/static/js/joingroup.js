const API_BASE = window.env.BASE_URL;
let id = null;
let groupID = null;



async function fetchUserId() {
            try{
                const response = await fetch(`${API_BASE}/api/id`);//request the id from a function  i made in the backend that takes in the session id of the user
                                                                             //a session helps alot as i can use the session id across my code
                const text = await response.text();


                id = Number(text);
                if (isNaN(id)) {
                    console.error("Invalid user ID:", text);
                    return;
                }
               return id;

            }catch (error) {
                console.error("Error fetching user ID:", error);
            }
        }
    async function joinGroup(groupID){
       
        
        try {
            const params = new URLSearchParams();
            params.append("groupID", groupID);

            const response = await fetch(`${API_BASE}/api/groups/joingroup`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: params.toString()
            });

            if (response.ok) {
                console.log("Group joined successfully.");
            } else {
                console.error("Failed to create group:", response.statusText);
            }

        } catch (error) {
            console.error("Error:", error);
        }        
    }

async function displayGroups() {
    const searchInput = document.getElementById("messageInput");
    const searchQuery = searchInput.value.toLowerCase();

    const response = await fetch(`${API_BASE}/api/groups/publicgroups`);
    const allGroups = await response.json(); // Wait for the response

    const groupsContainer = document.getElementById("public-list");
    groupsContainer.innerHTML = ""; // Clear previous results
    const titles = document.createElement("li");
    titles.innerHTML = `<p>Group Name</p><p>Privacy</p><p>Category</p>&nbsp;<p>`;
    titles.classList.add("ti");
    groupsContainer.appendChild(titles)

    let groupsToShow = [];

    if (searchQuery === "") {
        groupsToShow = allGroups;
    } else {
        for (let i = 0; i < allGroups.length; i++) {
            if (allGroups[i].groupName.toLowerCase().includes(searchQuery)) {
                groupsToShow.push(allGroups[i]);
            }
        }
    }

    for (let i = 0; i < groupsToShow.length; i++) {
    
        const group = groupsToShow[i];
        const listItem = document.createElement("li");
        listItem.classList.add("jg-li");


        listItem.innerHTML = `
            <p>${group.groupName}</p><p>Public</p><p>${group.groupType}</p>`;
        
        listItem.onclick = function () {
            joinGroup(group.groupID);
        };
        const joinButton = document.createElement("button");
        joinButton.classList.add("jg");
        joinButton.textContent = "Join";
        joinButton.onclick = function () {
            joinGroup(group.groupID);
        };
        
        listItem.appendChild(joinButton);
        groupsContainer.appendChild(listItem);
    }

    if (groupsToShow.length === 0) {
        const noResults = document.createElement("li");
        noResults.textContent = "No groups found.";
        groupsContainer.appendChild(noResults);
    }
}
function switchWindow(){
 window.location.href = "/home.html";   
}
document.addEventListener("DOMContentLoaded", function () {
    displayGroups();
});

document.getElementById("messageInput").addEventListener("keypress", function (event) {
    if (event.key === "Enter") {
        event.preventDefault();
        displayGroups();
    }
});
function toggleMode() {
    let body = document.body;
    let lamp = document.querySelector(".lamp");
    let themeLink = document.getElementById('theme-css');
    let isLightMode = body.classList.contains("light-mode");

    if (isLightMode) {
        body.classList.remove("light-mode");
        lamp.src = "/images/lamp-dark.png";
        themeLink.setAttribute('href', 'dark.css');
        localStorage.setItem("theme", "dark");
    } else {
        body.classList.add("light-mode");
        lamp.src = "/images/lamp-light.png";
        themeLink.setAttribute('href', 'light.css');
        localStorage.setItem("theme", "light");
    }
}

