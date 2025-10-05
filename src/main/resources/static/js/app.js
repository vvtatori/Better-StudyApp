const API_BASE = window.env.BASE_URL;

let stompClient = null;
        let currentGroupId = null;
        let id = null;
        let privacy = null;
        
        let groupName = "";
        let groupType = "";
        let privateBox = false;        
        let publicBox = false;
        let gname = null;
        let globalgname = null;
        let showAll = true;
        let privacyOn = false;
        let groupID = null;
        let username = "";

         
    async function fetchUsernameById(){
        try{

            const response = await fetch(`${API_BASE}/api/username`);//request the id from a function i made in the backend that takes in the session id of the user and returns me the username associated with the id                                                                           
            const text = await response.text();                                //a session helps alot as i can use the session id across my code            
            const username = text;

            if(username === null){
                console.error("Invalid username",username);
                return;
            }
        }catch(error){

        }
    }
    function goSettings(){
        window.location.href = "/settings.html"
    }
    
    function openCreatePopup() {
        document.getElementById('createGroupPopup').style.display = 'flex';
    }

    function closeCreatePopup() {
        document.getElementById('createGroupPopup').style.display = 'none';
    }
    function sleep(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    } 
    function scrollToBottom() {
        const container = document.querySelector('.chat-box');
        if (container) {
            container.scrollTop = container.scrollHeight;
        }
    }
    async function creategroup() {
    event.preventDefault();

    const groupName = document.getElementById("groupname").value;
    const groupType = document.getElementById("category").value;
    const error = document.getElementById("group-error");

    const privateBox = document.getElementById("private");
    const publicBox = document.getElementById("public");

    // Validate checkboxes
    if (privateBox.checked && publicBox.checked) {
        error.innerHTML = "Only select one checkbox";
        return;
    }

    if (!groupName) {
        error.innerHTML = "Enter a group name";
        return;
    }
    if(groupName.length > 15){
        return;
        console.log("")
    }

    let privacyOn = privateBox.checked; 
    

    try {
        const params = new URLSearchParams();
        params.append("groupName", groupName);
        params.append("groupType", groupType);
        params.append("privacyOn", privacyOn.toString());

        const response = await fetch(`${API_BASE}/api/groups/creategroup`, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: params.toString()
        });

        if (response.ok) {
            console.log("Group created successfully.");
            closeCreatePopup();
            location.reload();
        } else {
            console.error("Failed to create group:", response.statusText);
        }

    } catch (error) {
        console.error("Error:", error);
    }
}

    if (window.location.href.includes('groups.html')) {
        document.getElementById("messageInput").addEventListener('keypress', function(e) {
          if (e.key === 'Enter') {
            const messageContent = document.getElementById("messageInput").value; // Takes message from input box
            if (messageContent && currentGroupId && stompClient) { // Check if group exists and stomp client is running
              sendMessage(currentGroupId, messageContent);
              document.getElementById("messageInput").value = ''; 
              setTimeout(scrollToBottom, 100);
            }
          }
        });
    }
    
 

    
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


            }catch (error) {
                console.error("Error fetching user ID:", error);
            }
        }
        fetchUserId();
        
  
        function joinRedirect(){
            window.location.href = `joingroup.html`;
        }
        async function addUser(){
            
            console.log("currentgroupid",currentGroupId);//testing
            const username = document.getElementById("username");
            const name = username.value.trim();
            try {
                const params = new URLSearchParams();
                params.append("groupID", currentGroupId);
                params.append("username", name);

                const response = await fetch(`${API_BASE}/api/groups/adduser`, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded"
                    },
                    body: params.toString()
                });

                if (response.ok) {
                    console.log("User added successfully");
                } else {
                    console.error("Failed to add user", response.statusText);
                }

            } catch (error) {
                console.error("Error", error);
            }            
            
        }
        async function commment(){
            
            
            const params = new URLSearchParams();
            
            params.append("postID",postID);           
            params.append("id",id);
            params.append("commentContent",commentContent);
            
        const response = await fetch(`${API_BASE}/api/comments/sendcomment`, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: params.toString()
        });        
            
        }
        async function fetchGroups() {
            try {

                await fetchUserId();
                const response = await fetch(`${API_BASE}/api/groups`);
                console.log(response);

                const groups = await response.json();
                console.log(groups);
                
                
  
        
                const groupList = document.getElementById("group-list");
                

                console.log("Group List Element:", groupList);
                if (!groupList) {
                    console.error("no group list");

                }
                groupList.innerHTML = "";   
                const joinLi = document.createElement("li");       
                joinLi.classList.add("jg-li");     
                const joinButton = document.createElement("button");
                joinButton.classList.add("jg");
                joinButton.id = "jg";
                joinButton.textContent = "Join a group";    
                joinButton.onclick = joinRedirect;

                groupList.innerHTML = "";
                const createLi = document.createElement("li");
                createLi.classList.add("cg-li");                
                const createButton = document.createElement("button");
                createButton.classList.add("cg");
                createButton.id = "cg";
                createButton.textContent = "Create a group";
                createButton.onclick = openCreatePopup;

                joinLi.appendChild(joinButton);
                createLi.appendChild(createButton);
                groupList.appendChild(joinLi);
                groupList.appendChild(createLi);
                
                groups.forEach(group => {//loops through groups fetched
                    const li = document.createElement("li");
                    li.innerHTML = `<p>${group.groupName}</p>`;
                        
                    li.onclick = () => {


                        window.location.href = `groups.html?groupID=${group.groupID}&groupName=${encodeURIComponent(group.groupName)}`;//i made this to redirect from home.html to groups.html
                    };

                    groupList.appendChild(li);
                });
            } catch (error) {
                console.error("Error fetching groups:", error);
            }
        }

        // fetch group is called when the html is fully loaded
        document.addEventListener("DOMContentLoaded", function () {
            fetchGroups();
        });


        // opens chats by groupchat they select by groupid
        function openChat(groupID) {
            if (stompClient) {
                stompClient.disconnect();
            }

            currentGroupId = groupID;
            fetchMessagesByGroupID(currentGroupId);

            document.getElementById("chatBox").style.display = 'block';
            document.getElementById("messages").innerHTML = '';

            connectToWebSocket();
        }

        // Connect to WebSocket
        function connectToWebSocket() {
            const socket = new SockJS(`${API_BASE}/app/chat`);//this connects to the websocket.. the same endpoint used in the backend
            stompClient = Stomp.over(socket);

            stompClient.connect({}, onConnected, onError);
        }
        
        function onConnected() {
            console.log("Connected to WebSocket");

            if (currentGroupId) {
                const topic = `/topic/group/${currentGroupId}`;
                console.log(`Subscribing to ${topic}`);
                
                stompClient.subscribe(topic, function (messageOutput) {
                    const message = JSON.parse(messageOutput.body);

                    displayMessage(message);
                });
            }
        }

        function onError(error) {
            console.error("WebSocket Connection Error:", error);
        }
        
        
 //<-------------------------------------------------------------------------------->       
        async function fetchMessagesByGroupID(groupID) {
            try {
                const response = await fetch(`${API_BASE}/api/messages/getMessagesByGroupID?groupID=${groupID}`);//fetches messages from backend by groupid
                if (!response.ok) {
                    throw new Error("Failed to fetch messages");
                }
                const messages = await response.json();
                displayHistory(messages);
            } catch (error) {
                console.error("Error fetching messages:", error);
            }
        }


        function displayMessage(message) {
            const messageDiv = document.createElement("div");

            messageDiv.classList.add("message");
            time = message.timeSent;
            timeSent = time.substring(11,16);
            messageDiv.innerHTML = `        <small>${timeSent}</small><br>
                <strong>${message.username}</strong>: ${message.messageContent}

            `;

            document.getElementById("messages").appendChild(messageDiv);
            setTimeout(scrollToBottom, 100);
        }

        function displayHistory(messages) {
            const messageBox = document.getElementById("messages");
            messages.forEach(msg => {
                const messageDiv = document.createElement("div");
            time = msg.timeSent;
            timeSent = time.substring(11,16);
            messageDiv.classList.add("message");
            
            if (msg.id !== id) {
                messageDiv.classList.add("other-msg");
            }
            messageDiv.innerHTML = `<small>${timeSent}</small><br><strong>${msg.username}</strong>: ${msg.messageContent}`;
            console.log("chatBox exists?", document.getElementById("chatBox") !== null);
            console.log("messages exists?", document.getElementById("messages") !== null);
            messageBox.appendChild(messageDiv);
                        const container = document.querySelector('.chat-box');
            if(container){
                container.scrollTop = container.scrollHeight;
            }
            messageBox.appendChild(messageDiv);
           });
        }

            //send message function
        document.getElementById("sendMessageButton").onclick = function () {
            const messageContent = document.getElementById("messageInput").value;//this takes the message from the input box when the button is pressed
            
            if (messageContent && currentGroupId && stompClient) {//used to check if groupexists and stomp client is running
                sendMessage(currentGroupId, messageContent);
                document.getElementById("messageInput").value = ''; // this clear input after sending
                
            }
        };

        // sens message to backend
        function sendMessage(currentGroupId, messageContent) {
            if (stompClient) {
                stompClient.send("/app/chat", {}, JSON.stringify({//sends it to the backend.. the sendMessage functoin in my MessageLogic class which saves the messages and directs
                    groupID: currentGroupId,
                    id: id,
                    messageContent: messageContent,
                    username: username
                }));
                setTimeout(scrollToBottom, 100);
            } else {
                console.error("STOMP client is not connected.");
            }
        }


        document.addEventListener("DOMContentLoaded", function () {
            gname = document.getElementById("h3");
            const urlParams = new URLSearchParams(window.location.search);
            const groupID = urlParams.get("groupID");
            const groupName = urlParams.get("groupName");
            gname.innerHTML = groupName;


            if (groupID && groupName) {
                openChat(groupID, groupName);//to open chat from html

                fetchGroups();
            }

        });
document.addEventListener("DOMContentLoaded", function () {
    const urlParams = new URLSearchParams(window.location.search);
    currentGroupId = urlParams.get("groupID");
    if(currentGroupId){
        openChat(currentGroupId)
    }
    fetchGroups();
});