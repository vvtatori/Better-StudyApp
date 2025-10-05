const API_BASE = window.env.BASE_URL;

document.addEventListener("DOMContentLoaded", function () {
    // Disable editing fields when the profile is loaded at first
    setFieldsEditable(false);  //first set the fields to be readonly when profile is loaded
    document.getElementById("updateBtn").style.display = "none"; //Hide update button when it's loaded
});

let id;
function toggleTheme() {
    const body = document.body;
    const lamp = document.getElementById('lampToggle');
    body.classList.toggle('lightMode');
    if (body.classList.contains('lightMode')) {
      lamp.src = '/images/lamp-light.png';
    } else {
      lamp.src = '/images/lamp-dark.png';
    }
}

//Function to fecth user id to be used in other functions
async function fetchUserId() {
    try{
        const response = await fetch(`${API_BASE}/api/id`);//request the id from a function  made in the backend that takes in the session id of the user
                                                                     //a session helps to use the session-id across the code
        const text = await response.text();


        id = Number(text);
        if (isNaN(id)) {  //check if the value is a number first
            console.error("Invalid user ID:", text);
            return;
        }
        return id; //returns the user id found

    }catch (error) {
        console.error("Error fetching user ID:", error);
    }
}

//displaying the profile page, by fetching details of a user from the backend
async function loadProfile() {
    //first fetch the user's id
    const id =  await fetchUserId();
    if (!id) {  //check if the id is founnd first
        console.error("Failed to fetch user ID.");
        return;
    }
    
    //fetch the user's username
    const name = await fetchUsernameById();
    if (!name) {  //check if the user name is found
        console.error("Failed to fetch username.");
        return;
    }
    
    let response = await fetch(`${API_BASE}/api/profile/${id}`);  //fetches the data from this endpoint, written in the backend
    if (!response.ok) {
        console.error("Error fetching profile data:", response.status);
        return;
    }
    
    let data = await response.json();  //gathers the data in form of a json file
    // Gets all the values
    document.getElementById('username').textContent = data.username;
    document.getElementById('userEmail').textContent = data.email;
    document.getElementById('bio').value = data.bio || "";
    document.getElementById('school').value = data.school || "";
    document.getElementById('course').value = data.course || "";
    document.getElementById('profileImage').src = data.profileImage || "darkmode.png";
}

//used to get the username of a user by using their id, to be used in other functions
async function fetchUsernameById(){
    try{
        const response = await fetch(`${API_BASE}/api/username`);//request the id from a functionmade in the backend that takes in the session id of the user and returns me the username associated with the id                                                                           
        const text = await response.text();                                         
        const username = text;

        if(username === null){  //check if hte username is null, meaning is not availabe
            console.error("Invalid username",username);
            return;
        }
        return username;  //returns the username found
    }catch(error){

    }
}


//Enabling edits on the Bio and Education fields on the profile
function enableEditing() {
    setFieldsEditable(true);
    document.getElementById("editBtn").style.display = "none"; // Hide Edit button after edit button is clicked
    document.getElementById("updateBtn").style.display = "inline-block"; // Show Update button
}

//Updates the new values after editting
function updateProfile() {
    //get all the values input by the user
    const bio = document.getElementById("bio").value;
    const school = document.getElementById("school").value;
    const course = document.getElementById("course").value;
 
    //to hold the appended the values
    const formData = new FormData();
    formData.append("bio", bio);
    formData.append("school", school);
    formData.append("course", course);

    fetch(`${API_BASE}/api/profile/update/${id}`, {  //fetches from the endpoint in the profile controller
        method: "PUT",
        body: formData
    })
    .then(response => response.text()) 
    .then(data => {
        try {
            if(data === "Profile saved successfully!"){ 
                document.getElementById("message").innerHTML = "Successfully updated your profile";  //display message to the user
            }
            const jsonData = JSON.parse(data);
        } catch (error) {
            console.error("Unexpected response format:", data);
        }
    })
    .catch(error => {
        console.error("Error updating profile:", error);
    });
}

//function to allow the fields in the profile, bio and eductaion cards, to be editable
function setFieldsEditable(editable) {
    document.getElementById("bio").readOnly = !editable;
    document.getElementById("school").readOnly = !editable;
    document.getElementById("course").readOnly = !editable;
    document.getElementById("profileImage").disabled = !editable;
}

//Fetching user upvotes
async function loadUserUpvotes() {
    try {
        const id = await fetchUserId(); //fetching userID first
        let response = await fetch(`${API_BASE}/api/profile/upvotes/${id}`);
        if (!response.ok) {
            console.error("Error fetching upvotes");
            return;
        }
        let data = await response.json();

        if (!data || !data.upvotes) {
            console.error("Error: No upvote data found for user.");
            return;
        }

        document.getElementById("upvotes").textContent = data.upvotes;
    } catch (error) {
        console.error("Error fetching upvotes:", error);
    }
}


// Fetching user rank 
//async function fetchUserRank(id) {
//    let response = await fetch(`${API_BASE}/api/rank/${id}`);
//    let rank = await response.text();
//    return rank || "N/A"; // Default to "N/A" if no rank found
//}
// Function to take the user back to the home page

//used to fetch groups a user is in from the groups table
async function fetchUserGroups() {
    try {
        await fetchUserId(); // Fetch user ID from session

        const response = await fetch(`${API_BASE}/api/groups`); // (Used backticks and include id) then gets all groups matching the user id
        const groups = await response.json(); // Converting response to JSON
        
        console.log("User Groups:", groups);

        const groupList = document.getElementById("group-list");

        if (!groupList) {
            console.error("Group list container not found!");
            return;
        }

        groupList.innerHTML = ""; // Clear any existing content

        groups.forEach(group => {
            const li = document.createElement("li");  //create a list for every group found
            li.innerHTML = `<p>${group.groupName}</p>`;  //fetching the group name to insert to the list

            groupList.appendChild(li);
        });

    } catch (error) {
        console.error("Error fetching user groups:", error);
    }
}

// Fetch user groups when profile page loads
document.addEventListener("DOMContentLoaded", function () {
    fetchUserGroups();
});

//back button
function backHome(){
    window.location.href = "/home.html"; 
}

document.addEventListener("DOMContentLoaded", loadUserUpvotes);
loadProfile(); 
