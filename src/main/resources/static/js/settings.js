const API_BASE = window.env.BASE_URL;

let canChange = false;
let code = "";
let redirect = false;
let emailInput = "";
let canLogout = false;
let canDelete = false;

        async function showDetails(){

            const importantSection = document.getElementById("important");
            const detailSection = document.getElementById("userdetails");

            const detailbar = document.getElementById("detail-bar");
            detailbar.style.backgroundColor = "#4F5DD9";

            const importantbar = document.getElementById("important-bar");
            importantbar.style.backgroundColor = "#2C2C30";

            importantSection.style.display="none";
            detailSection.style.display = "block";
        }
        
        function showImportant(){
            const importantSection = document.getElementById("important");
            const detailSection = document.getElementById("userdetails");

            const detailbar = document.getElementById("detail-bar");
            detailbar.style.backgroundColor = "#2C2C30";

            const importantbar = document.getElementById("important-bar");
            importantbar.style.backgroundColor = "red";

            detailSection.style.display = "none";
            importantSection.style.display="block";
        }
        
        function goHome(){
            window.location.href = "/home.html";
        }
        function sleep(ms) {
            return new Promise(resolve => setTimeout(resolve, ms));
        } 
        async function checkPass(){
            const oldPassword = document.getElementById("oldPassword").value;
            const newPassword = document.getElementById("newPassword").value;
            const newPasswordTwo = document.getElementById("newPasswordTwo").value;
            
            if(newPassword !== newPasswordTwo){
                console.log("new passwords dont match");
                return;
            }
            
            const params =  new URLSearchParams();
            params.append("oldPassword",oldPassword);
            params.append("newPassword",newPassword);
            params.append("newPasswordTwo",newPasswordTwo);
            
            const response = await fetch(`${API_BASE}/api/getEncryptPass`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: params.toString()
            });      
            const text = await response.text();
                if(text === "true"){
                    canChange = true;
                }else if(text === "error"){
                   console.log("error"); 
               }
               
            if(text === "Successful password change"){
                await sleep(500);
                closePopup();
            }
                
            
        }

    async function fetchCode(){
        try{
            const response = await fetch(`${API_BASE}/api/getcode`); // request the code from the backend
            const text = await response.text(); // this should be the code or username

            code = text; // assigns the fetched code to a variable

            if(code === null || code === "") { // checks if the code is invalid
                console.error("Invalid code:", code);
                return;
            }

            //backend code
            console.log("Fetched code:", code);

        } catch(error) {
            console.error("Error fetching the code:", error);
        }
    }

    async function email(){

        localStorage.setItem("storedEmail", email); //so i can access the email in a different file
        //sends a req to endpoint in backend
        fetch(`${API_BASE}/api/sendmail?toEmail=${encodeURIComponent(emailInput)}`, {
            method: 'GET',
        })
        .then(response => response.text())
        .then(data => {
            console.log('Success:', data);
            closeEmail();
            openChangeEmail();
        })
        .catch(error => {
            console.error('Error:', error);
        });
    }
    function sleep(ms) {
      return new Promise(resolve => setTimeout(resolve, ms));
    }
    async function checkCode(){
        await fetchCode(); //gets code
        const userCodeInput = document.getElementById("code").value; // Get the code input element 
        const msg = document.getElementById("errorMsg");       
        //// Get the value entered by the user

        if(userCodeInput === code){ // Compare the user-provided code with the fetched code
            const newEmaill = document.getElementById("newEmail").value;
            
            if(newEmaill === ""){
                msg.innerHTML = "Enter a valid email"
            }
            try {
                
                const params = new URLSearchParams();
                params.append("newEmail",newEmaill);
                
                const response = await fetch(`${API_BASE}/api/checkemail`, {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded"
                    },
                    body: params.toString()
                });                
                
                    const errorMsg = await response.text();
                    if(errorMsg === "Your email has been updated"){
                        msg.innerHTML = "Your email was successful changed";
                        await sleep(1000);
                        closeChangeEmail();                        
                    }else{
                        msg.innerHTML = "That email already exists or is invalid";

                    }
            } catch (error) {
            console.error('Error sending data to backend:', error);
            }
        } else {      
            document.getElementById("approval").innerHTML = "Unsuccesful";   

        }
    } 
        async function checkPassWithUsername(){
            const newUsernamee = document.getElementById("newUsername").value.trim();
            const currentPassword = document.getElementById("pass").value;
            const mesg = document.getElementById("errorMesg");
            
            if(newUsernamee === ""){
                mesg.innerHTML = "Enter a valid username";
            }
            const params = new URLSearchParams();
            params.append("newUsername",newUsernamee);
            params.append("password",currentPassword);
            
            const response = await fetch(`${API_BASE}/api/passwithuser`,{
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: params.toString()
            });
           
            const errorMessage = await response.text();
            if(errorMessage === "Your username has been changed successfully"){
                await sleep(1000);
                closeUsername();
                getDetails();
                document.getElementById("newUsername").value = "";
                document.getElementById("pass").value = "";
                
                    
            }else{
                mesg.innerHTML = "Enter a valid username";
            }   
        }
        async function logout(){
            canLogout = true;
            const params = new URLSearchParams();
            params.append("logout",canLogout);
            
            const response = await fetch(`${API_BASE}/api/logout`,{
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body: params.toString()  
            });
           const text = await response.text();
           if(text === "true"){
               window.location.href = "/landing.html";
           }
        }
        async function deleteAccount(){
            canDelete = true;            
            const params = new URLSearchParams();
            params.append("delete",canDelete);
            const inputText = document.getElementById("deletetxt").value;
            
            if(inputText !== "delete"){
                return;
                console.log("not valid");
            }
            
            const response = await fetch(`${API_BASE}/api/delete`,{
                method: "POST",
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                },
                body:params.toString()
            });
            const thingy = await response.text();
            if(thingy === "true"){
                window.location.href = "/landing.html";
                canDelete = false;
            }else{
                console.log("error");
                canDelete = false;
            }
                
            
        }
        
        async function getDetails(){
            
            const response = await fetch(`${API_BASE}/api/user`);
            const details = await response.json();
            
            if(!response.ok  || !details){
                console.error(response);
            }
            const email = document.getElementById("email");
            const username = document.getElementById("username");
            const password = document.getElementById("password");
            
               emailInput = details.email;

               email.value = details.email;
               username.value = details.username;
               password.value = details.password;
               
        }
        
        const openPopupButton = document.getElementById('openPopup');
        const closePopupButton = document.getElementById('closePopup');
        const popupOverlay = document.getElementById('popupOverlay');
        const submitButton = document.getElementById('submitButton');
        const closeEmailButton = document.getElementById('closeEmail');
        const emailOverlay = document.getElementById('emailOverlay');
      
        const changeUsernameOverlay = document.getElementById('changeUsernameOverlay');
        
        const changeEmailOverlay = document.getElementById('changeEmailOverlay');   
        const changeEmailButton = document.getElementById('closeChangeEmail');
        
        const logoutOverlay = document.getElementById('logoutOverlay');
        
        const deleteOverlay = document.getElementById('deleteOverlay');
        
        
        function openDelete(){
            deleteOverlay.style.display = 'flex';           
        }
        function closeDelete(){
            deleteOverlay.style.display = 'none';           
        }
        
        function openLogout(){
            logoutOverlay.style.display = 'flex';
        }
        function closeLogout(){
            logoutOverlay.style.display = 'none';            
        }
        
        function openUsername(){
            changeUsernameOverlay.style.display = 'flex';
        }
        function closeUsername(){
            changeUsernameOverlay.style.display = 'none';
        }
        
        function openChangeEmail(){
            changeEmailOverlay.style.display = 'flex';
        }
        function closeChangeEmail(){
            changeEmailOverlay.style.display = 'none';
        }
        
        function openEmail(){
            emailOverlay.style.display = 'flex';
        }
        function closeEmail(){
            emailOverlay.style.display = 'none';
        }
        
        function openPopup() {
          popupOverlay.style.display = 'flex';
        }

        function closePopup() {
          popupOverlay.style.display = 'none';
        }
        closeEmailButton.addEventListener('click', closeEmail);
        
        openPopupButton.addEventListener('click', openPopup);
        closePopupButton.addEventListener('click', closePopup);
        submitButton.addEventListener('click', checkPass);

        popupOverlay.addEventListener('click', function(event) {
          if (event.target === popupOverlay) {
            closePopup();
          }
        });      
        
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

        document.addEventListener("DOMContentLoaded", function () {
          if (localStorage.getItem("theme") === "light") {
            document.body.classList.add("light-mode");
            document.querySelector(".lamp").src = "/images/lamp-light.png";
          }
        });        
  document.addEventListener("DOMContentLoaded", function() {
      getDetails();
  });