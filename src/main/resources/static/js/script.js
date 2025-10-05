

let n = 1;
let editingPost = null;
let posts = new Map();
let commentContent = "";
let clicked = false;
let x = 1;
let userId = null;
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

// Adds the theme to Local sStorage
document.addEventListener("DOMContentLoaded", function () {
  if (localStorage.getItem("theme") === "light") {
    document.body.classList.add("light-mode");
    document.querySelector(".lamp").src = "/images/lamp-light.png";
  }
});
        async function fetchUserId() {
            try{
                const response = await fetch(`${API_BASE}/api/id`);//request the id from a function  i made in the backend that takes in the session id of the user
                                                                   //a session helps alot as i can use the session id across my code
                const text = await response.text();


                const userId = Number(text);
                
                console.log("Fetchuserid",userId);
                if (isNaN(userId)) {
                    console.error("Invalid user ID:", text);
                    return;
                }
                return userId;


            }catch (error) {
                console.error("Error fetching user ID:", error);
            }
        }

    function fetchAndDisplayLeaderboard() {
        const API_BASE = window.env.BASE_URL;
      fetch(`${API_BASE}/api/leaderboardto`)
        .then(response => response.json())
        .then(data => {
          const list = document.getElementById("list"); 
          list.innerHTML = "";

    data.forEach(user => {
      const listItem = document.createElement("li");

        if(n == 1){

            const goldMedal = new Image(50,50);
            goldMedal.src = '/images/goldmedal.png';
            listItem.appendChild(goldMedal);
            listItem.classList.add("gold");
        }else if(n == 2){
            const silverMedal = new Image(50,50);
            silverMedal.src = '/images/silvermedal.png';
            listItem.appendChild(silverMedal);
            listItem.classList.add("silver");

        }else if( n == 3){
            const bronzeMedal = new Image(50,50);
            bronzeMedal.src = '/images/bronzeMedal.png';
            listItem.appendChild(bronzeMedal);      
            listItem.classList.add("bronze");
        }else{
            const empty = new Image(50,50);
            empty.src = '/images/defaultmedal.png';
            listItem.appendChild(empty);    

        }



      const number = document.createElement("span");
      number.classList.add("number");
      number.textContent = n + ' ';

      const title = document.createElement("span");
      title.classList.add("username");
      title.textContent = user.username;

      const upvotes = document.createElement("span");
      upvotes.classList.add("upvotes");
      upvotes.textContent = `${user.upvotes} upvotes`;
      listItem.appendChild(title);
      listItem.appendChild(upvotes);


      list.appendChild(listItem);

      n++;
    });

        })
        .catch(error => console.error("Error fetching data:", error));
    }

    document.addEventListener("DOMContentLoaded", fetchAndDisplayLeaderboard);






    async function createPost() {
        const API_BASE = window.env.BASE_URL;
        const postContent = document.getElementById("postText").value;
        const fileInput = document.getElementById("postImage");


     //to finish code later https://developer.mozilla.org/en-US/docs/Web/API/XMLHttpRequest_API/Using_FormData_Objects   
        const formData = new FormData();
        formData.append("postContent", postContent);
        formData.append("file", fileInput.files[0]);

        try {
            const response = await fetch(`${API_BASE}/api/posts/create`, {
                method: "POST",
                body: formData
            });

            const result = await response.text();
            console.log("Success:", result);
        } catch (error) {
            console.error("Error:", error);
        }
    }
    async function likePost(postID){
        const API_BASE = window.env.BASE_URL;
        const response = await fetch(`${API_BASE}/api/posts/${postID}/like`, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"//sends posts request to creategroup function in the backend as a url
            },
            body: `postID=${postID}`
        });
        if(!posts.has(postID)){
            
            posts.set(postID,new Set());
        }
    }
    function alreadyLiked(postID,id){
    if (!posts.has(postID)) {
        posts.set(postID,new Set());
    }
    let likes = posts.get(postID);
    if (likes.has(id)) {
        return true; // User already liked the post
    }

    likes.add(id); // adds the user to the set of likes for this post
    return false; // user has not liked the post yet
    }
    async function comment(postID,commentContent){
        const API_BASE = window.env.BASE_URL;
        let userId = await fetchUserId();
        
        if(!userId){
            console.log("comment",userId)
        }
        console.log("CommentContent",commentContent)
        
        const params = new URLSearchParams();
        
        console.log("comment",userId);
        
        params.append("postID",postID);                
        params.append("commentContent",commentContent);

        const response = await fetch(`${API_BASE}/api/comments/sendcomment`, {
            method: "POST",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            body: params.toString()
        });      
        if(!response.ok){
            console.log("error");
        }

    }
    
    

        
 async function fetchPosts() {
    try {
        const API_BASE = window.env.BASE_URL;
        const response = await fetch(`${API_BASE}/api/posts`);
        const posts = await response.json();
        const postList = document.getElementById("post-list");
        

        postList.innerHTML = "";

        posts.forEach(post => {
            const postContainer = document.createElement("div");//creates div for posts
            const line = document.createElement("hr");
            postContainer.classList.add("post-container"); 
            line.classList.add("color-hr"); 
            
            const postContent = document.createElement("div");
            const postText = document.createElement("strong");
            postText.innerText = post.postContent; 
            postContent.appendChild(postText);
            postContainer.appendChild(postContent); 
            postContainer.appendChild(line);

            if (post.base64File) {//checks if there is an image attached to the post
                const img = document.createElement("img");
                const contentType = post.contentType || "image/jpeg";
                img.src = `data:${contentType};base64,${post.base64File}`;

                const imageDiv = document.createElement("div");
                imageDiv.classList.add("imageDiv");
                imageDiv.appendChild(img);
                postContainer.appendChild(imageDiv);
            }
            
            const username = document.createElement("div");
            username.className = "un";
            const unn = document.createElement("div");
            unn.classList.add("idpost");

            unn.textContent = `User: ${post.username}`;
            
            const commentImage = document.createElement("img");//comment logo
            commentImage.className = "ci";
            commentImage.src = "/images/commentt.png"
            commentImage.alt = "/images/comment logo"
            commentImage.style.cursor = "pointer";
            
            const searchBtn = document.createElement("button");
            searchBtn.classList.add("search-btn");
            
            searchBtn.textContent = "Post";
            
            const commentBar = document.createElement("div");
            commentBar.classList.add("comment-bar");     
            commentBar.appendChild(searchBtn);
            
            const searchDiv = document.createElement("div");
            searchDiv.classList.add("search-div");

            const search = document.createElement("textarea");
            search.placeholder = "Post your reply...";
            search.classList.add("search");
            
            searchDiv.appendChild(search);
            searchDiv.appendChild(commentBar);
            
            commentImage.onclick = async () => {
                const API_BASE = window.env.BASE_URL;
                const commentContainer = postContainer.querySelector('.comments-section');
                
                if (commentContainer) {
                    commentContainer.style.display = commentContainer.style.display === 'block' ? 'none' : 'block';
                } else {
                    search.style.display = "block"; 
                    try {
                        const response = await fetch(`${API_BASE}/api/comments/getComments?postID=${post.postID}`);
                        const comments = await response.json();
                        const newCommentContainer = document.createElement("div");
                        newCommentContainer.classList.add("comments-section");
                        newCommentContainer.style.display = `block`;
                        newCommentContainer.appendChild(searchDiv);    

                        searchBtn.onclick = async () => {
                            const postID = post.postID;
                            const commentContent = search.value;
                            comment(postID, commentContent);
                            search.value = "";
                            console.log("postid: ",postID);
                            console.log("content ",commentContent);
                        };

                        comments.forEach(comment => {
                            const combar = document.createElement("hr");
                            combar.classList.add("com-line");
                            newCommentContainer.appendChild(combar);

                            const commentList = document.createElement("li");
                            commentList.classList.add("each-comment");

                            const nameDiv = document.createElement("div");
                            nameDiv.classList.add("name-div");
                            time = comment.timeSent;
                            const weekDays = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];  
                            const monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];                            
                            const date = new Date(time);
                            const numberDay = date.getDay();
                            const day = weekDays[numberDay];// day of the week
                            const actualDate = date.getDate();// date number
                            const numberMonth = date.getMonth();
                            const actualMonth = monthNames[numberMonth];// month name
                            let ending = "";

                            switch (actualDate) {
                                case 1:
                                case 21:
                                case 31:
                                    ending = "st";
                                    break;
                                case 2:
                                case 22:
                                    ending = "nd";
                                    break;
                                case 3:
                                case 23:
                                    ending = "rd";
                                    break;
                                default:
                                    ending = "th";
                                    break;
                            }

                            const realDate = actualDate + ending;

                            const sepDiv = document.createElement("div");
                            sepDiv.classList.add("inside-div");

                            const usernameElement = document.createElement("span");
                            const timeStuff = document.createElement("span");
                            timeStuff.classList.add("time");
                            timeStuff.textContent = "" + day + " " + realDate + " " + actualMonth;  
                            usernameElement.textContent = comment.username;
                            usernameElement.classList.add("c-username");
                            usernameElement.appendChild(timeStuff);

                            sepDiv.appendChild(usernameElement);
                            sepDiv.appendChild(timeStuff);

                            nameDiv.appendChild(sepDiv);

                            const commentText = document.createElement("span");
                            commentText.textContent = comment.commentContent;
                            commentText.classList.add("c-content");

                            commentList.appendChild(nameDiv);
                            commentList.appendChild(commentText);

                            newCommentContainer.appendChild(commentList);
                        });
                        postContainer.appendChild(newCommentContainer);
                    } catch (error) {
                        console.error(error);
                    }
                }
            };

            const thumbDiv = document.createElement("div");
            thumbDiv.className = "thumb";
            const up = document.createElement("img");

            up.className = "up";// upvotes
            up.src = "/images/thumbs-up.png";
            up.alt = "upvote";
            up.style.cursor = "pointer";

            const down = document.createElement("img");// downvotes
            down.className = "down";
            down.src = "/images/thumbs-down.png";
            down.alt = "downvote";
            down.style.cursor = "pointer";

            up.onclick = async () => {
                if (!alreadyLiked(post.postId, id)) {
                    try {
                        const likeCount = await likePost(post.postID);
                        alreadyLiked(post.postID, null);
                    } catch (error) {
                        console.error('Like failed:', error);
                    }
                }
            };

            thumbDiv.appendChild(commentImage);     
            thumbDiv.appendChild(up);
            thumbDiv.appendChild(down);           
            username.appendChild(unn);
            postContainer.append(username);
            postContainer.appendChild(thumbDiv);
            const bar = document.createElement("div");
            bar.classList.add("post-bar");            

            postList.appendChild(postContainer); // appends to the list of all the posts
            postList.appendChild(bar); // added this bar between posts for separation and clean look
        });
    } catch (error) {
        console.error("Error fetching posts:", error);
    }
}



    window.onload = fetchPosts;


    document.addEventListener('DOMContentLoaded', fetchPosts);





    // rolling effect for stats 
    function animateNumbers(id, start, end, duration) {
      let range = end - start;
      let current = start;
      let increment = range / (duration / 1); 
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

    // animate stats when page loads (also acts as a refresh to load latest stats)
    document.addEventListener("DOMContentLoaded", function () {
      Object.keys(statsMap).forEach(id => {
        if (document.getElementById(id)) {
          animateNumbers(id, 0, statsData[statsMap[id]], 2000);
        }
      });
    });

    // light/dark Mode Toggle with Local Storage
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
      Object.keys(statsMap).forEach(id => {
        if (document.getElementById(id)) {
          animateNumbers(id, 0, statsData[statsMap[id]], 2000);
        }
      });
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
      fetch("policies.txt")
        .then(response => response.text())
        .then(text => {
          document.getElementById("policy-content").textContent = text;
        })
        .catch(error => console.error("Error loading policies:", error));
    });

    let navbarOpen = false;
            function toggleNavbar() {
                const navbar = document.getElementById("navbar");
                navbar.style.top = navbarOpen ? "-100px" : "0";
                navbarOpen = !navbarOpen;
            }

            function openPopup(type) {
              closePopup(); // Close any existing popup
              document.getElementById(type + "Popup").style.display = "block";
              document.getElementById("overlay").style.display = "block";
          }

          function closePopup() {
              document.getElementById("loginPopup").style.display = "none";
              document.getElementById("signupPopup").style.display = "none";
              document.getElementById("overlay").style.display = "none";
          }

          function switchPopup(type) {
              closePopup();
              openPopup(type);
          }
        
        function openPostPopup() {
            document.getElementById("createPostPopup").style.display = "block";
        }


        function closePostPopup() {
            document.getElementById("createPostPopup").style.display = "none";
        }


            function openEditPopup(post) {
                document.getElementById("editPopup").style.display = "block";
                document.getElementById("overlay").style.display = "block";
                document.getElementById("editText").value = post.querySelector("p").textContent;
                editingPost = post;
            }

            function closeEditPopup() {
                document.getElementById("editPopup").style.display = "none";
                document.getElementById("overlay").style.display = "none";
                editingPost = null;
            }

            function addPost() {
                let text = document.getElementById("postText").value;
                let image = document.getElementById("postImage").files[0];

                if (text.trim() === "" && !image) {
                    alert("Please enter a message or upload on image.");
                    return;
                }

                let postDiv = document.createElement("div");
                postDiv.classList.add("post");

                if (image) {
                  let imgElement = document.createElement("img");
                  imgElement.src = URL.createObjectURL(image);
                  postDiv.appendChild(imgElement);
              }
                let editButton = document.createElement("button");
                editButton.textContent = "Edit";
                editButton.classList.add("edit-btn");
                
                editButton.onclick = function () {
                    openEditPopup(postDiv);
                };

                let textElement = document.createElement("p");
                textElement.textContent = text;

                postDiv.appendChild(editButton);
                postDiv.appendChild(textElement);

                document.getElementById("posts").prepend(postDiv);

                document.getElementById("postText").value = "";
                closePostPopup();
            }
         

            function saveEdit() {
                if (editingPost) {
                    let newText = document.getElementById("editText").value;
                    if (newText.trim() === "") {
                        alert("Post text cannot be empty.");
                        return;
                    }
                    editingPost.querySelector("p").textContent = newText;
                    closeEditPopup();
                }
            }

            function deletePost() {
                if (editingPost) {
                    if (confirm("Are you sure you want to delete this post?")) {
                        editingPost.remove();
                        closeEditPopup();
                    }
                }
            }
        
