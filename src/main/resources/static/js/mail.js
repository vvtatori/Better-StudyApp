const API_BASE = window.env.BASE_URL;

let code = "";
let redirect = false;

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
    const emailInput = document.getElementById("emailinput"); // gets the email input element
    const email = emailInput.value; // takes value
    
    localStorage.setItem("storedEmail", email); //so i can access the email in a different file
    //sends a req to endpoint in backend
    fetch(`${API_BASE}/api/sendmail?toEmail=${encodeURIComponent(email)}`, {
        method: 'GET',
    })
    .then(response => response.text())
    .then(data => {
        console.log('Success:', data);
    })
    .catch(error => {
        console.error('Error:', error);
    });
}
function sleep(ms) {
  return new Promise(resolve => setTimeout(resolve, ms))
}
async function checkCode(){
    await fetchCode(); //gets code
    const userCodeInput = document.getElementById("usercode"); // Get the code input element
    const userCode = userCodeInput.value; // Get the value entered by the user

    if(userCode === code){ // Compare the user-provided code with the fetched code
        
        document.getElementById("approval").innerHTML = "Redirecting... " + "<img src='/images/loading.gif' height='30px' width='30px' style='margin-top:10px;'>";


        
        redirect = true;
    try {
        const response = await fetch(`${API_BASE}/api/redirectChangePassword?redirect=${redirect}`, {
            method: 'GET'
        });

        const data = await response.text(); // takes in response from backend like a message to check for errors
        console.log('Backend response:', data);
        
        if (data.includes("redirect:/changepassword.html")) {//from resetpassword.java, if data returns that it will redirect
            await sleep(1000);// after a second so its looks good
                window.location.href = "/changepassword.html"; // this redirects the user once the users enter the same code
            }
        
    } catch (error) {
        console.error('Error sending data to backend:', error);
    }
    } else {      
        redirect = false;
        document.getElementById("approval").innerHTML = "Unsuccesful"    
        
    }
}