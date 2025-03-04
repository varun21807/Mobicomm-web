

// function validateAndLogin() {
//     const mobileNumber = document.getElementById("mobileNumber").value;
//     const errorMessage = document.getElementById("error-message");

//     const regex = /^[6-9]\d{9}$/;
//     if (!regex.test(mobileNumber)) {
//         errorMessage.classList.remove("d-none");
//         return;
//     }

   
//     errorMessage.classList.add("d-none");

   
//     setTimeout(() => {
     
//         let loginModal = bootstrap.Modal.getInstance(document.getElementById('loginModal'));
//         loginModal.hide();

       
//         document.getElementById("dropdownMenu").innerHTML = `
//             <li><a class="dropdown-item" href="profile.html">Profile</a></li>
//             <li><a class="dropdown-item" href="History.html">History</a></li>
//             <li><hr class="dropdown-divider"></li>
//             <li><a class="dropdown-item text-danger" href="#" onclick="logout()">Logout</a></li>
//         `;
//     }, 500);
// }

// function logout() {
//     // Reset dropdown to show login option again
//     document.getElementById("dropdownMenu").innerHTML = `
//         <li><a class="dropdown-item" href="#" data-bs-toggle="modal" data-bs-target="#loginModal">Login</a></li>
//     `;
// }
// document.querySelectorAll('.toggle-btn').forEach(button => {
//     button.addEventListener('click', function () {
//         const targetId = this.getAttribute('data-bs-target');  
//         const targetCollapse = document.querySelector(targetId);

//         targetCollapse.addEventListener('shown.bs.collapse', () => {
//             this.textContent = "Show Less";
//         });

//         targetCollapse.addEventListener('hidden.bs.collapse', () => {
//             this.textContent = "Show Details";
//         });
//     });
// });
// document.getElementById("editBtn").addEventListener("click", function () {
//     let inputs = document.querySelectorAll("#settingsForm input");
//     inputs.forEach(input => input.removeAttribute("disabled"));
//     document.getElementById("editBtn").classList.add("d-none");
//     document.getElementById("saveBtn").classList.remove("d-none");
// });

// document.getElementById("saveBtn").addEventListener("click", function () {
//     let inputs = document.querySelectorAll("#settingsForm input");
//     inputs.forEach(input => input.setAttribute("disabled", true));
//     document.getElementById("editBtn").classList.remove("d-none");
//     document.getElementById("saveBtn").classList.add("d-none");
// });
// function openModal(modalId) {
//     var modal = new bootstrap.Modal(document.getElementById(modalId));
//     modal.show();
// }
// function processPayment(method) {
// // Hide payment method modal (if it's open)
// const paymentMethodModal = bootstrap.Modal.getInstance(document.getElementById('upiModal')) || 
//                         bootstrap.Modal.getInstance(document.getElementById('cardModal'));
// if (paymentMethodModal) {
// paymentMethodModal.hide();
// }

// // Show the payment processing modal
// const processingModal = new bootstrap.Modal(document.getElementById('paymentProcessingModal'));
// processingModal.show();

// // Simulate the payment processing with a 3-second delay
// setTimeout(() => {
// // Hide the payment processing modal
// processingModal.hide();

// // Set plan details and validity based on payment method
// let planDetails = '';
// let validity = '';

// if (method === 'UPI') {
//     planDetails = 'Premium Plan - ₹499';
//     validity = 'Valid for 1 month';
// } else if (method === 'Credit Card' || method === 'Debit Card') {
//     planDetails = 'Gold Plan - ₹999';
//     validity = 'Valid for 3 months';
// }

// // Show the success modal with dynamic content
// const successModal = new bootstrap.Modal(document.getElementById('rechargeSuccessModal'));
// document.getElementById('paymentMethod').textContent = method;
// document.getElementById('transactionId').textContent = 'TXN' + Math.random().toString(36).substr(2, 9).toUpperCase();
// document.getElementById('rechargeDateTime').textContent = new Date().toLocaleString();
// document.getElementById('planDetails').textContent = planDetails;
// document.getElementById('planValidity').textContent = validity;

// successModal.show();
// }, 3000); // 3-second delay for processing
// }
// function payNow(paymentMethod) {
// let paymentDetails = '';

// // For UPI
// if (paymentMethod === 'UPI') {
// const vpaId = document.getElementById('vpaId').value;
// if (vpaId) {
//     paymentDetails = `UPI ID: ${vpaId}`;
// } else {
//     alert("Please enter a valid UPI ID.");
//     return;
// }
// }

// // For Cards
// if (paymentMethod === 'Card') {
// const cardNumber = document.getElementById('cardNumber').value;
// const expiryDate = document.getElementById('expiryDate').value;
// const cvv = document.getElementById('cvv').value;

// if (cardNumber && expiryDate && cvv) {
//     paymentDetails = `Card Number: ${cardNumber}\nExpiry Date: ${expiryDate}\nCVV: ${cvv}`;
// } else {
//     alert("Please enter all card details.");
//     return;
// }
// }

// // Display the entered payment details below the modal
// const paymentDetailsDiv = document.getElementById('paymentDetails');
// paymentDetailsDiv.innerHTML = `<strong>Entered Payment Details:</strong><pre>${paymentDetails}</pre>`;

// // Get modal instance for UPI or Card modal
// let modalId = paymentMethod === 'UPI' ? 'upiModal' : 'cardModal';
// let modalElement = document.getElementById(modalId);
// let modalInstance = bootstrap.Modal.getInstance(modalElement);

// // Close the modal
// modalInstance.hide(); // This will close the modal

// }
// // Function to print the payment details
// function printDetails() {
// let paymentDetails = `
// Payment Method: ${document.getElementById('paymentMethod').textContent}
// Transaction ID: ${document.getElementById('transactionId').textContent}
// Date and Time: ${document.getElementById('rechargeDateTime').textContent}
// Plan Details: ${document.getElementById('planDetails').textContent}
// Validity: ${document.getElementById('planValidity').textContent}
// `;

// // Open a new window to print
// let printWindow = window.open('', '_blank');
// printWindow.document.write('<pre>' + paymentDetails + '</pre>');
// printWindow.document.close();
// printWindow.print();
// }

// // Function to navigate to home page
// function goToHome() {
// window.location.href = 'index.html'; // Replace with your home page URL
// }
// function validatePhoneNumber(inputId, nextPage) {
//     let phoneNumber = document.getElementById(inputId).value;
//     let phoneRegex = /^[6-9]\d{9}$/; // Ensures it starts with 6-9 and has exactly 10 digits
    
//     if (phoneRegex.test(phoneNumber)) {
//         window.location.href = nextPage; // Redirect to respective page
//     } else {
//         alert("Invalid phone number! Please enter a valid 10-digit number starting with 6-9.");
//     }
// }
// document.addEventListener("DOMContentLoaded", function () {
//     let counters = document.querySelectorAll(".counter");
//     let speed = 200;
//     counters.forEach(counter => {
//         let updateCount = () => {
//             let target = +counter.getAttribute("data-target");
//             let count = +counter.innerText;
//             let increment = target / speed;
//             if (count < target) {
//                 counter.innerText = Math.ceil(count + increment);
//                 setTimeout(updateCount, 20);
//             } else {
//                 counter.innerText = target;
//             }
//         };
//         updateCount();
//     });
// });
document.addEventListener("DOMContentLoaded", function () {
    let selectedPlan = null;

    // Handle "Buy Now" click to open modal with correct plan details
    document.addEventListener("click", function (event) {
        if (event.target.classList.contains("buy-now-btn")) {
            const planData = event.target.getAttribute("data-plan");

            if (planData) {
                selectedPlan = JSON.parse(planData);
                localStorage.setItem("selectedPlan", JSON.stringify(selectedPlan));
            }
        }
    });

    // Validate Mobile Number and Redirect on "Continue" Button Click
    document.getElementById("continue-btn").addEventListener("click", function () {
        const phoneInput = document.getElementById("phone2").value.trim();
        const errorMessage = document.getElementById("error-message");

        // Simple validation for 10-digit mobile number
        if (!/^\d{10}$/.test(phoneInput)) {
            errorMessage.textContent = "Please enter a valid 10-digit mobile number.";
            errorMessage.style.display = "block";
            return;
        }

        errorMessage.style.display = "none"; // Hide error message if valid

        if (selectedPlan) {
            selectedPlan.phoneNumber = phoneInput; // Store the validated number
            localStorage.setItem("selectedPlan", JSON.stringify(selectedPlan)); // Save to localStorage
            window.location.href = "payment.html"; // Redirect to Payment Page
        }
    });
});





document.addEventListener("DOMContentLoaded", function () {
    const urlParams = new URLSearchParams(window.location.search);
    
    const mobile = urlParams.get("mobile");
    const price = urlParams.get("price");
    const duration = urlParams.get("duration");
    const data = urlParams.get("data");
    const offer = urlParams.get("offer");

    // Fill in the details
    if (mobile) document.querySelector(".plan-details p:nth-child(2)").innerHTML = `<strong>Mobile Number:</strong> +91 ${mobile}`;
    if (offer && data) document.querySelector(".plan-details p:nth-child(3)").innerHTML = `<strong>Plan:</strong> ${offer} + ${data}`;
    if (price) {
        document.querySelector(".plan-details p:nth-child(4)").innerHTML = `<strong>Price:</strong> ₹${price}`;
        document.getElementById("payAmount").innerText = price;
    }
});

    function changeForm(method) {
        let inputGroup = document.getElementById('payment-input');
        if (method === 'UPI') {
            inputGroup.innerHTML = '<i class="fas fa-university"></i><input type="text" placeholder="Enter UPI ID">';
        } else if (method === 'Card') {
            inputGroup.innerHTML = `
                <div class="input-group"><i class="fas fa-credit-card"></i><input type="text" placeholder="Card Number"></div>
                <div class="input-group"><i class="fas fa-user"></i><input type="text" placeholder="Name on Card"></div>
                <div class="input-group"><i class="fas fa-calendar-alt"></i><input type="text" placeholder="Expiry Date"></div>
                <div class="input-group"><i class="fas fa-lock"></i><input type="text" placeholder="CVV"></div>
            `;
        } else {
            inputGroup.innerHTML = '<i class="fas fa-globe"></i><input type="text" placeholder="Net Banking ID">';
        }
    }
    document.addEventListener("DOMContentLoaded", function () {
        updateDropdown();
    });
    
    function updateDropdown() {
        const dropdownMenu = document.getElementById("dropdownMenu");
    
        if (localStorage.getItem("isLoggedIn") === "true") {
            dropdownMenu.innerHTML = `
                <li><a class="dropdown-item" href="profile.html">Profile</a></li>
                <li><a class="dropdown-item" href="#" id="logoutLink">Logout</a></li>
            `;
    
            // Attach logout event after modifying the DOM
            document.getElementById("logoutLink").addEventListener("click", function () {
                localStorage.removeItem("isLoggedIn");
                localStorage.removeItem("userPhone");
                window.location.reload(); // Refresh to update the dropdown
            });
        }
    }
    
    document.querySelector(".login-btn").addEventListener("click", function () {
        const phoneInput = document.getElementById("phone").value.trim();
        const phoneRegex = /^[6-9]\d{9}$/;
    
        if (!phoneRegex.test(phoneInput)) {
            alert("Invalid phone number. It should start with 6-9 and be 10 digits long.");
            return;
        }
    
        // Simulate OTP validation (You can replace this with actual OTP verification)
        const otpInput = document.getElementById("otp").value.trim();
        if (otpInput.length !== 6) {
            alert("Invalid OTP. Please enter a 6-digit OTP.");
            return;
        }
    
        // Store login state
        localStorage.setItem("isLoggedIn", "true");
        localStorage.setItem("userPhone", phoneInput);
    
        // Redirect to home page
        window.location.href = "index.html";  // Change to your home page
    });
    document.addEventListener("DOMContentLoaded", function () {
        const dropdownMenu = document.getElementById("dropdownMenu");
    
        if (localStorage.getItem("isLoggedIn") === "true") {
            dropdownMenu.innerHTML = `
                <li><a class="dropdown-item" href="profile.html">Profile</a></li>
                <li><a class="dropdown-item" href="#" id="logoutLink">Logout</a></li>
            `;
    
            document.getElementById("logoutLink").addEventListener("click", function () {
                localStorage.removeItem("isLoggedIn");
                localStorage.removeItem("userPhone");
                window.location.reload(); // Refresh to update the dropdown
            });
        }
    });
    document.addEventListener("DOMContentLoaded", function () {
        let links = document.querySelectorAll(".nav-link");
        let currentPage = window.location.pathname.split("/").pop();
        console.log("Current Page:", currentPage); // Debugging: Check current page
    
        links.forEach(link => {
            console.log("Checking link:", link.getAttribute("href")); // Debugging: Check each link
            if (link.getAttribute("href") === currentPage) {
                link.classList.add("active");
                console.log("Active link set:", link.textContent); // Debugging: Confirm active link
            } else {
                link.classList.remove("active");
            }
        });
    });
    
    
    


    

    
    
    
    //  function login() {
    //      // Simulating a successful login
    //      localStorage.setItem('loggedIn', 'true');
    //      window.location.href = 'index.html'; // Redirect to home page after login
    //  }
  
    //  document.addEventListener("DOMContentLoaded", function() {
    //      const dropdownMenu = document.getElementById("dropdownMenu");
 
    //      function updateDropdown() {
    //          if (localStorage.getItem("loggedIn") === "true") {
    //              dropdownMenu.innerHTML = `
    //                  <li><a class="dropdown-item" href="profile.html">Profile</a></li>
    //                  <li><a class="dropdown-item" href="history.html">History</a></li>
    //                  <li><a class="dropdown-item" href="#" id="logoutLink">Logout</a></li>
    //              `;
 
    //              document.getElementById("logoutLink").addEventListener("click", function() {
    //                  localStorage.removeItem("loggedIn");
    //                  window.location.reload(); // Reload page to apply changes
    //              });
    //          }
    //      }
 
    //      updateDropdown(); // Call function on page load
    //  });
 
    //  function login() {
    //      localStorage.setItem('loggedIn', 'true');
    //      window.location.href = 'index.html';
    //  }
    //  document.addEventListener("DOMContentLoaded", function() {
    //      if (localStorage.getItem('loggedIn') === 'true') {
    //          window.location.href = 'index.html';
    //      }
    //  });
    
    
    
    
    