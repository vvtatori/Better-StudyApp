            // Add event listener to the lamp element
            document.addEventListener("DOMContentLoaded", function() {
              const lamp = document.querySelector(".lamp");
              if (lamp) {
                lamp.addEventListener("click", toggleMode);
              }
            });  
            
            function switchWindow() {
                window.location.href = `/home.html`;   
            }