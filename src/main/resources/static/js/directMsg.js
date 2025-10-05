const API_BASE = window.env.BASE_URL;
//
////let stompClient;
//let currentUsername; //  from the session 
//let chattingWith;

let stompClient = null;
let currentUserID = null;
let currentUsername = null;
let activeChatUserID = null;  //To be used to list the current chats active to the user

document.addEventListener('DOMContentLoaded', async () => {
    currentUserID = await fetchUserId();
    currentUsername = await fetchUsernameById();

    if (!currentUserID) {
        alert("Failed to get user session ID. Please log in.");
        return;
    }

    connectWebSocket();
    preloadChatList();
});

// Fetch user ID from session (Same as profile function)
async function fetchUserId() {
    try {
        const response = await fetch(`${API_BASE}/api/id`);
        const text = await response.text();

        const id = Number(text);
        if (isNaN(id)) {
            console.error("Invalid user ID:", text);
            return null;
        }
        return id;
    } catch (error) {
        console.error("Error fetching user ID:", error);
        return null;
    }
}

// Establishing the WebSocket connection
function connectWebSocket() {
    const socket = new SockJS('/ws');  //path outlined in the webSocketConfig
    stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
        console.log("Connected to WebSocket");
    });
}

// Loading existing chats for the logged in user
async function preloadChatList() {
    try {
        const response = await fetch(`${API_BASE}/api/directchat/userChats?userId=${currentUserID}`);  //endpoint in controller- to fetch the list of previous chats based on the user ids
        const users = await response.json();

        const userList = document.getElementById('userList'); //the list to hold the users
        userList.innerHTML = '';

        if (users.length === 0) {  //prompt a new user to search for people to look up for them to start a chat with
            const msg = document.createElement('li');
            msg.textContent = "No chats yet. Start a new chat from the search bar!";
            msg.style.fontStyle = 'italic';
            userList.appendChild(msg);
        } else {
            users.forEach(user => {  //disppay the suername for each other user
                addUserToChatList(user.username, user.id);  //the fucntion to add a user to the chatList is called
            });
        }
    } catch (error) {
        console.error("Failed to load user chats:", error);
    }
}

// Adds user to the chat list side bar  
function addUserToChatList(username, userId) {
    if (document.getElementById(`chat-${userId}`)) return;

    const li = document.createElement('li');
    li.id = `chat-${userId}`;
    li.textContent = username;
    li.onclick = () => openChatWindow(userId, username);  //allows each user listed to be clickable to open the chats
    document.getElementById('userList').appendChild(li);
}

// To Open the chat window with messeges 
function openChatWindow(userId, username) {
    activeChatUserID = userId;
    document.getElementById('chattingWith').innerText = username;
    document.getElementById('chatMessages').innerHTML = '';

    const topic = `/topic/user/${currentUserID}/${userId}`;
    stompClient.subscribe(topic, (message) => {
        const msg = JSON.parse(message.body);
        showMessage(msg);
    });

    fetch(`${API_BASE}/api/directchat/getMessages?user1=${currentUserID}&user2=${userId}`)  //fetch the messages
        .then(res => res.json())
        .then(messages => {
            messages.forEach(showMessage);  //call the showMessage function to display the message
        });
}

// To Show the message in chat
function showMessage(message) {
    const chatBox = document.getElementById('chatMessages');
    const msgDiv = document.createElement('div');
    msgDiv.className = message.senderID === currentUserID ? 'sentMsg' : 'receivedMsg';
    
    //set the time
    const time = new Date(message.timeSent);
    const formattedTime = time.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' });
    
    //appending the message in form of the one laid out in the html
    msgDiv.innerHTML = `
        <div class="msgMeta">
            <strong>${message.senderUsername}</strong>
            <span class="msgTime">${formattedTime}</span>
        </div>
        <div class="msgContent">${message.messageContent}</div>
    `;
    //msgDiv.textContent = `${message.senderUsername}: ${message.messageContent}`; (Doe snot display the messages on screen)
    
    chatBox.appendChild(msgDiv);
    chatBox.scrollTop = chatBox.scrollHeight;
}

//To Send chat message
function sendText() {
    const input = document.getElementById('messageInput');
    const msgContent = input.value.trim();
    if (!msgContent || !activeChatUserID) return;

    //the details to be sent with the message: time, sender, content is displayed, while ids for the sender and receiver are stored 
    const msg = {
        senderID: currentUserID,
        receiverID: activeChatUserID,
        messageContent: msgContent,
        senderUsername: currentUsername,
        timeSent: null  //To allow the server to add the actual time
    };

    stompClient.send("/app/directChat", {}, JSON.stringify(msg));
    input.value = '';
}

//To search and start chat with new user
function searchUser() {
    const username = document.getElementById('searchInput').value.trim();
    if (!username) return;

    fetch(`/api/users/search?username=${username}`)  //searches for a user from the databse, based on their username
        .then(res => {
            if (!res.ok) throw new Error("User not found");
            return res.json();
        })
        .then(user => {
            addUserToChatList(user.username, user.id);  //user is added to the chatList on the side bar when found
            openChatWindow(user.id, user.username);
        })
        .catch(err => {
            alert("User not found.");
            console.error(err);
        });
}

//To be used for the username (Same as the profile function)
async function fetchUsernameById() {
    try {
        const response = await fetch(`${API_BASE}/api/username`);
        const username = await response.text();

        if (!username) {
            console.error("Invalid username:", username);
            return null;
        }

        return username;
    } catch (error) {
        console.error("Error fetching username:", error);
    }
}

//To go back to main
function backToMain(){
    window.location.href = "/home.html";
}

//The following codes were ones I used while learning on how web sockets work.
//They did not run for this functionality.

//// WebSocket connection
//function connectToDirectSocket(otherUser) {
//    const socket = new SockJS("http://localhost:8080/ws/direct");
//    stompClient = Stomp.over(socket);
//
//    stompClient.connect({}, function () {
//        console.log("Connected to direct message WebSocket");
//
//        chattingWith = otherUser;
//        document.getElementById("chattingWith").textContent = otherUser;
//
//        const topic = `/topic/user/${currentUsername}/${chattingWith}`;
//        stompClient.subscribe(topic, function (messageOutput) {
//            const msg = JSON.parse(messageOutput.body);
//            displayMessage(msg);
//        });
//
//        fetchChatHistory(currentUsername, chattingWith);
//    }, function (error) {
//        console.error("WebSocket Error:", error);
//    });
//}
//
//// Fetch previous messages
//async function fetchChatHistory(user1, user2) {
//    try {
//        const response = await fetch(`http://localhost:8080/api/directchat/getMessages?user1=${user1}&user2=${user2}`);
//        const messages = await response.json();
//        document.getElementById("chatMessages").innerHTML = "";
//        messages.forEach(displayMessage);
//    } catch (err) {
//        console.error("Failed to fetch chat history:", err);
//    }
//}
//
//// Display individual message
//function displayMessage(msg) {
//    const messageDiv = document.createElement("div");
//    const time = msg.timeSent ? msg.timeSent.substring(11, 16) : "";
//    messageDiv.classList.add("message");
//    messageDiv.innerHTML = `<small>${time}</small><br><strong>${msg.senderUsername}</strong>: ${msg.messageContent}`;
//    document.getElementById("chatMessages").appendChild(messageDiv);
//}
//
//// Send message
//function sendText() {
//    const input = document.getElementById("messageInput");
//    const content = input.value.trim();
//    if (content && stompClient && chattingWith) {
//        const message = {
//            senderID: currentUsername,
//            receiverID: chattingWith,
//            senderUsername: currentUsername,
//            messageContent: content
//        };
//        stompClient.send("/app/directChat", {}, JSON.stringify(message));
//        input.value = "";
//    }
//}
//
//// User search and chat starter
//async function searchUser() {
//    const username = document.getElementById("searchInput").value.trim();
//    if (!username) return;
//
//    // Simulate fetch from server or user list
//    const found = await findUser(username);
//    if (found) {
//        addUserToChatList(username);
//        connectToDirectSocket(username);
//    } else {
//        alert("User not found!");
//    }
//}
//
//// Mock: Replace with real backend check  (From chatGPT to help in understanding the errors)
//async function findUser(username) {
//    try {
//        const response = await fetch(`http://localhost:8080/api/user/exists?username=${encodeURIComponent(username)}`);
//        const exists = await response.json();
//        return exists; // true or false
//    } catch (err) {
//        console.error("Error checking user existence:", err);
//        return false;
//    }
//}
//
//// Add new user to chat list
//function addUserToChatList(username) {
//    const userList = document.getElementById("userList");
//    if ([...userList.children].some(li => li.textContent === username)) return;
//
//    const li = document.createElement("li");
//    li.textContent = username;
//    li.onclick = () => connectToDirectSocket(username);
//    userList.appendChild(li);
//}
//
//function backToMainChatPage() {
//    document.querySelector(".chatWindow").style.display = "none";
//    document.querySelector(".chatList").style.display = "block";
//}
//
//function openChatWindow() {
//    document.querySelector(".chatList").style.display = "none";
//    document.querySelector(".chatWindow").style.display = "block";
//}



//document.addEventListener("DOMContentLoaded", () => {
//    document.getElementById("backBtn").addEventListener("click", backToMain);
//    document.getElementById("sendText").addEventListener("click", sendText);
//    document.getElementById("searchUser").addEventListener("click", searchUser);
//});
////let socket;
//let stompClient;
//let selectedUser = null;
//const currentUsername = "currentUser"; //To be replaced with the current logged in user
//
//document.addEventListener("DOMContentLoaded", loadChatList);
//
////Loads the listy of existing users the currentUser has chats with
//function loadChatList(){
//    fetch("/api/chat/users")//to fetch users with active chats to create the list
//        .then(response => response.json())
//        .then(users => {
//            let userList = document.getElementById("userList");
//            userList.innerHTML = ""; //To first clear the existing list
//
//            if(users.length === 0){
//                userList.innerHTML = "<p>No Chats yet. Start a chat.</p>";
//            }else{
//                users.forEach(user => {
//                    let li = document.createElement("li");
//                    li.textContent = user.username;
//                    li.onclick = () => openChatWindow(user.username);
//                    userList.appendChild(li);
//                });
//            }
//        })
//        .catch(error => console.error("Failed to fetch chat List", error));
//}
//
////To open the chat area for a chat with a selected user
//function openChatWindow(user){
//    selectedUser = user;
//
//    //show the chat window and set the username of the user
//    document.getElementById("chattingWith").textContent = "Chats with " + user;
//    document.getElementById("chatMessages").innerHTML = ""; //clearing the chat messages before fetch
//
//    //fetching the chat history
//    fetch(`/api/chat/messages/${currentUsername}/${user}`)
//        .then(response => response.json())
//        .then(messages => {
//            let chatArea = document.getElementById("chatMessages");
//            messages.forEach(displayMessage);
//        })
//        .catch(error => console.error("Error fetching messages:", error));
//
//        // Close previous WebSocket connection (if any)
//        if (socket) {
//            socket.close();
//        }
//
//        // Open a new WebSocket connection for the selected user
//        socket = new SockJS('/chat');
//        stompClient = Stomp.over(socket);
//        stompClient.connect({}, function (frame) {
//            console.log("Connected: " + frame);
//            
//            stompClient.subscribe(`/user/${currentUsername}/queue/messages`, function (message) {
//            const receivedMessage = JSON.parse(message.body);
//
//            // To Ensure the message is between the correct users
//            if (
//                (receivedMessage.sender.username === selectedUser && receivedMessage.receiver.username === currentUsername) ||
//                (receivedMessage.sender.username === currentUsername && receivedMessage.receiver.username === selectedUser)
//            ) {
//                displayMessage(receivedMessage);
//            }
//            });
//        });
//        // socket = new WebSocket(``);
//
//        // socket.onmessage = function(event) {
//        //     let chatArea = document.getElementById("chatMessages");
//        //     let message = document.createElement("div");
//        //     message.textContent = event.data;
//        //     messageArea.appendChild(message);
//        // };
//}
//
////sending a text message in the chat
//function sendText(){
//    if (!selectedUser) {
//        alert("Please select a user first!");
//        return;
//    }
//
//    let input = document.getElementById("messageInput");
//    let messageContent = input.value.trim();
//    
//    if (messageContent !== "") {
//        // socket.send(JSON.stringify({ sender: "Me", content: message }));
//        // input.value = ""; // Clear input field
//        let chatMessage = {
//            sender: { username: currentUsername },
//            receiver: { username: selectedUser },
//            content: messageContent
//        };
//
//        stompClient.send("/app/sendMessage", {}, JSON.stringify(chatMessage));
//
//        displayMessage(chatMessage); // Show sent message immediately
//        input.value = ""; // Clear input field
//    }
//}
//
////displaying the messages in the chat
//function displayMessage(message) {
//    let chatArea = document.getElementById("chatMessages");
//    let messageDiv = document.createElement("div");
//
//    messageDiv.textContent = `${message.sender.username}: ${message.content}`;
//    messageDiv.classList.add(message.sender.username === currentUsername ? "sent" : "received");
//    chatArea.appendChild(messageDiv);
//    chatArea.scrollTop = chatArea.scrollHeight; // Auto-scroll to bottom
//}
//
////To search for  a user in order to start a chat
//function searchUser(){
//    let searchInput = document.getElementById("searchInput").value.trim();
//
//    if (searchInput === "") {
//        alert("Please enter a username to search.");
//        return;
//    }else{
//        selectedUser = searchInput;
//    }
//
//    fetch(`/api/users/search?username=${searchInput}`)
//        .then(response => {
//            if (!response.ok) {
//                throw new Error("User not found");
//            }
//            return response.json();
//        })
//        .then(user => {
//            // Check if user is already in chat list
//            let chatUsers = document.querySelectorAll("#userList li");
//            let foundUser = Array.from(chatUsers).find(li => li.textContent === user.username);
//
//            if (foundUser) {
//                openChatWindow(user.username); // Open existing chat
//            } else {
//                // User found but not in chat list, start a new chat
//                let userList = document.getElementById("userList");
//                let newLi = document.createElement("li");
//                newLi.textContent = user.username;
//                newLi.onclick = () => openChatWindow(user.username);
//                userList.appendChild(newLi);
//
//                openChatWindow(user.username); // Open the chat window
//            }
//        })
//        .catch(error => alert(error.message));
//}
//



