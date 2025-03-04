function showPlanModal() {
    let modal = new bootstrap.Modal(document.getElementById('planModal'));
    modal.show();
}

function addPlan() {
    let name = document.getElementById("planName").value;
    let validity = document.getElementById("planValidity").value;
    let data = document.getElementById("planData").value;
    let offers = document.getElementById("planOffers").value;
    if (!name || !validity || !data || !offers) {
        alert("Please fill all fields");
        return;
    }
    let table = document.getElementById("plansTable");
    let newRow = table.insertRow();
    newRow.innerHTML = `<td>${name}</td><td>${validity}</td><td>${data}</td><td>${offers}</td><td><button class='btn btn-danger' onclick='removePlan(this)'>Remove</button></td>`;
    bootstrap.Modal.getInstance(document.getElementById('planModal')).hide();
}

function removePlan(button) {
    let row = button.parentElement.parentElement;
    row.remove();
}

function viewRechargeHistory(name) {
    let history = {
        "John Doe": [
            { plan: "Unlimited 30 Days", date: "Jan 20, 2025", mode: "Credit Card" },
            { plan: "Basic 7 Days", date: "Dec 13, 2024", mode: "UPI" }
        ],
        "Jane Smith": [
            { plan: "Basic 7 Days", date: "Feb 12, 2025", mode: "Debit Card" }
        ],
        "Mike Johnson": [
            { plan: "Standard 15 Days", date: "Feb 3, 2025", mode: "Net Banking" }
        ],
        "Emma Wilson": [
            { plan: "Unlimited 7 Days", date: "Feb 10, 2025", mode: "Credit Card" }
        ]
    };
    let userHistory = history[name] || [];
    let output = `<h3>Recharge History for ${name}</h3><ul class='list-group mt-3'>`;
    userHistory.forEach(entry => {
        output += `<li class='list-group-item'>${entry.plan} - ${entry.date} (${entry.mode})</li>`;
    });
    output += "</ul>";
    let historyElement = document.getElementById(`history-${name}`);
    historyElement.innerHTML = output;
    historyElement.style.display = "table-cell";
}
function editSettings() {
    document.querySelectorAll('.settings-form input').forEach(input => input.removeAttribute('disabled'));
    let editButton = document.querySelector('.settings-form button:first-of-type');
    let saveButton = editButton.nextElementSibling;
    editButton.style.display = 'none';
    saveButton.style.display = 'inline-block';
}

function saveSettings() {
    document.querySelectorAll('.settings-form input').forEach(input => input.setAttribute('disabled', 'true'));
    let saveButton = document.querySelector('.settings-form button:last-of-type');
    let editButton = saveButton.previousElementSibling;
    saveButton.style.display = 'none';
    editButton.style.display = 'inline-block';
    alert("Settings saved successfully!");
}
function showCategoryModal() {
    let modal = new bootstrap.Modal(document.getElementById('categoryModal'));
    modal.show();
}


function addCategory() {
    let categoryName = document.getElementById("categoryName").value;
    if (!categoryName) {
        alert("Please enter a category name");
        return;
    }
    let table = document.getElementById("categoriesTable");
    let newRow = table.insertRow();
    newRow.innerHTML = `<td>${categoryName}</td><td><button class='btn btn-danger' onclick='removeCategory(this)'>Remove</button></td>`;

    let dropdown = document.getElementById("categoryDropdown");
    let newOption = document.createElement("option");
    newOption.text = categoryName;
    newOption.value = categoryName;
    dropdown.add(newOption);

    bootstrap.Modal.getInstance(document.getElementById('categoryModal')).hide();
}

function removeCategory(button) {
    let row = button.parentElement.parentElement;
    let categoryName = row.cells[0].innerText;
    row.remove();

    let dropdown = document.getElementById("categoryDropdown");
    for (let i = 0; i < dropdown.options.length; i++) {
        if (dropdown.options[i].value === categoryName) {
            dropdown.remove(i);
            break;
        }
    }
}
document.addEventListener("DOMContentLoaded", function () {
    let loginModal = new bootstrap.Modal(document.getElementById('loginModal'));
    loginModal.show();
    document.addEventListener("DOMContentLoaded", function () {
        let triggerTabList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tab"]'));
        triggerTabList.forEach(function (tabEl) {
            new bootstrap.Tab(tabEl);
        });
    });

});

function adminLogin() {
    let email = document.getElementById("adminEmail").value;
    let password = document.getElementById("adminPassword").value;

    if (email === "admin@example.com" && password === "admin123") {
        alert("Login Successful!");
        bootstrap.Modal.getInstance(document.getElementById('loginModal')).hide();
    } else {
        alert("Invalid Credentials");
    }
}
const ctx5 = document.getElementById('dailyRechargeChart');
new Chart(ctx5, {
    type: 'line',
    data: {
        labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
        datasets: [{
            label: 'Daily Recharges',
            data: [150, 180, 170, 200, 220, 250, 300],
            borderColor: 'green'
        }]
    }
});

document.addEventListener("DOMContentLoaded", () => {
    fetchCategories();
});

let currentEditCategory = "";
let currentEditPlanId = "";

async function fetchCategories() {
    const categories = [
        "popular_plans", "true_5g_unlimited", "entertainment_plans",
        "annual_plans", "phone_plans", "isd_plans", "data_packs"
    ];

    const categoriesTable = document.getElementById("categoriesTable");
    const plansTable = document.getElementById("plansTable");
    const planCategorySelect = document.getElementById("planCategory");

    categoriesTable.innerHTML = "";
    plansTable.innerHTML = "";
    planCategorySelect.innerHTML = '<option value="">Select Category</option>';

    for (const category of categories) {
        try {
            const response = await fetch(`http://localhost:3000/${category}`);
            if (!response.ok) throw new Error(`Failed to fetch ${category}`);

            const data = await response.json();
            const rowId = `category-${category}`;

            // Display category in the categories table
            categoriesTable.innerHTML += `
                <tr id="${rowId}" class="${data.active ? 'active-category' : 'deactivated'}">
                    <td>${category.replace(/_/g, " ").toUpperCase()}</td>
                    <td>
                        <button class="btn btn-success btn-sm me-1" onclick="activateCategory('${rowId}')">
                            <i class="bi bi-check-circle"></i>
                        </button>
                        <button class="btn btn-warning btn-sm me-1" onclick="openEditCategoryModal('${category}')">
                            <i class="bi bi-pencil-square"></i>
                        </button>
                        <button class="btn btn-secondary btn-sm" onclick="deactivateCategory('${rowId}')">
                            <i class="bi bi-x-circle"></i>
                        </button>
                    </td>
                </tr>
            `;

            planCategorySelect.innerHTML += `<option value="${category}">${category.replace(/_/g, " ").toUpperCase()}</option>`;

            // Loop through subcategories
            if (data.subcategories && data.subcategories.length > 0) {
                data.subcategories.forEach(subCategory => {
                    subCategory.plans.forEach(plan => {
                        const planRowId = `plan-${plan.offer.replace(/\s+/g, "-")}`;
                        const planData = JSON.stringify(plan).replace(/"/g, "&quot;");

                        plansTable.innerHTML += `
                            <tr id="${planRowId}" class="${data.active ? 'active-plan' : 'deactivated'}">
                                <td>${category.replace(/_/g, " ").toUpperCase()}</td>
                                <td>${subCategory.subheading}</td>
                                <td>${plan.offer}</td>
                                <td>${plan.duration} Days</td>
                                <td>${plan.data || "N/A"}</td>
                                <td>${plan.ott || "None"}</td>
                                <td>${plan.calls || "N/A"}</td>
                                <td>${plan.sms || "N/A"}</td>
                                <td>${plan.benefits || "N/A"}</td>
                                <td><span class="badge bg-${plan.badgeColor}">${plan.offer}</span></td>
                                <td>
                                    <button class="btn btn-success btn-sm me-1" onclick="activatePlan('${planRowId}')">
                                        <i class="bi bi-check-circle"></i>
                                    </button>
                                    <button class="btn btn-warning btn-sm me-1" onclick="openEditPlanModal('${planRowId}', '${planData}')">
                                        <i class="bi bi-pencil-square"></i>
                                    </button>
                                    <button class="btn btn-secondary btn-sm" onclick="deactivatePlan('${planRowId}')">
                                        <i class="bi bi-x-circle"></i>
                                    </button>
                                </td>
                            </tr>
                        `;
                    });
                });
            }

        } catch (error) {
            console.error(error);
        }
    }
}


function activateCategory(categoryId) {
    const correctedCategoryId = categoryId.replace("category-", "");

    // First, fetch the existing data to preserve `subcategories`
    fetch(`http://localhost:3000/${correctedCategoryId}`)
        .then(response => response.json())
        .then(existingData => {
            // Send a PATCH request that only updates `active` while keeping `subcategories`
            return fetch(`http://localhost:3000/${correctedCategoryId}`, {
                method: "PATCH",  // ✅ Use PATCH to avoid overwriting data
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    active: true,
                    subcategories: existingData.subcategories || [] // ✅ Preserve subcategories
                })
            });
        })
        .then(response => response.json())
        .then(updatedData => {
            alert(`Category ${correctedCategoryId} has been activated.`);
            console.log(`Category ${correctedCategoryId} activated`, updatedData);
            fetchAndUpdatePlans(correctedCategoryId);
        })
        .catch(error => {
            console.error("Error activating category:", error);
            alert(`Failed to activate category: ${error.message}`);
        });
}


function deactivateCategory(categoryId) {
    const correctedCategoryId = categoryId.replace("category-", "");

    fetch(`http://localhost:3000/${correctedCategoryId}`, {
        method: "PUT",  // ✅ Change from PATCH to PUT
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            active: false,
            subcategories: [] // ⚠️ Ensure subcategories remain in the object
        }) 
    })
    .then(response => response.json())
    .then(updatedData => {
        alert(`Category ${correctedCategoryId} has been deactivated.`);
        console.log(`Category ${correctedCategoryId} deactivated`, updatedData);
        document.getElementById(correctedCategoryId).style.display = "none";
    })
    .catch(error => {
        console.error("Error deactivating category:", error);
        alert(`Failed to deactivate category: ${error.message}`);
    });
}

function fetchAndUpdatePlans(categoryId) {
    console.log(`Fetching updated plans for ${categoryId}`);

    fetch(`http://localhost:3000/${categoryId}`)
        .then(response => response.json())
        .then(data => {
            // Auto-detect and extract the correct level of data
            let categoryData = data;

            while (categoryData.item) {  
                categoryData = categoryData.item; // ✅ Remove unnecessary "item" wrappers
            }

            let tabContent = document.getElementById(categoryId);
            if (!categoryData.active) {
                console.log(`Category ${categoryId} is inactive, hiding it.`);
                tabContent.style.display = "none";
                return;
            }

            console.log(`Category ${categoryId} is active, displaying it.`);
            tabContent.style.display = "block";

            let accordionHtml = `<div class="accordion" id="accordion${categoryId}">`;
            categoryData.subcategories.forEach((item, index) => {
                accordionHtml += createAccordionItem(item.subheading, item.plans, categoryId, index + 1);
            });
            accordionHtml += `</div>`;

            tabContent.innerHTML = accordionHtml;
        })
        .catch(error => console.error(`Error fetching ${categoryId}:`, error));
}


document.addEventListener("click", function (event) {
    let button = event.target.closest("button");
    if (button) {
        let categoryId = button.getAttribute("data-category");
        if (button.classList.contains("btn-success")) {
            activateCategory(categoryId);
        } else if (button.classList.contains("btn-secondary")) {
            deactivateCategory(categoryId);
        }
    }
});


function activatePlan(rowId) {
    document.getElementById(rowId).classList.remove("deactivated");
}

function deactivatePlan(rowId) {
    document.getElementById(rowId).classList.add("deactivated");
}

function openEditCategoryModal(category) {
    currentEditCategory = category;
    document.getElementById("editCategoryName").value = category.replace(/_/g, " ").toUpperCase();
    new bootstrap.Modal(document.getElementById("editCategoryModal")).show();
}

function saveCategoryEdit() {
    const newName = document.getElementById("editCategoryName").value;
    if (newName) {
        document.getElementById(`category-${currentEditCategory}`).querySelector("td").innerText = newName;
    }
}

function openEditPlanModal(planRowId, planData) {
    currentEditPlanId = planRowId;
    const plan = JSON.parse(planData.replace(/&quot;/g, '"')); // Fix for encoded quotes

    document.getElementById("editPlanName").value = plan.offer;
    document.getElementById("editPlanDuration").value = plan.duration;
    document.getElementById("editPlanData").value = plan.data;
    document.getElementById("editPlanOTT").value = plan.ott;
    document.getElementById("editPlanCalls").value = plan.calls;
    document.getElementById("editPlanSMS").value = plan.sms;
    document.getElementById("editPlanBenefits").value = plan.benefits;
    document.getElementById("editPlanOffer").value = plan.offer;

    new bootstrap.Modal(document.getElementById("editPlanModal")).show();
}

function savePlanEdit() {
    const row = document.getElementById(currentEditPlanId);
    const cells = row.querySelectorAll("td");

    cells[2].innerText = document.getElementById("editPlanName").value;
    cells[3].innerText = document.getElementById("editPlanDuration").value + " Days";
    cells[4].innerText = document.getElementById("editPlanData").value;
    cells[5].innerText = document.getElementById("editPlanOTT").value;
    cells[6].innerText = document.getElementById("editPlanCalls").value;
    cells[7].innerText = document.getElementById("editPlanSMS").value;
    cells[8].innerText = document.getElementById("editPlanBenefits").value;
    cells[9].querySelector("span").innerText = document.getElementById("editPlanOffer").value;

    document.getElementById("editPlanModal").querySelector(".btn-close").click(); // Close modal
}


document.addEventListener("DOMContentLoaded", function () {
    function handleAddCategory() {
        let inputField = document.getElementById("newCategoryName");
        if (!inputField) {
            alert("Error: Input field not found.");
            return;
        }

        let categoryName = inputField.value.trim();
        if (!categoryName) {
            alert("Please enter a category name.");
            return;
        }

        let categoryId = categoryName.toLowerCase().replace(/\s+/g, "_");

        addCategory(categoryId, categoryName);

        // Close the modal
        let modalElement = document.getElementById("addCategoryModal");
        let modal = bootstrap.Modal.getInstance(modalElement);
        modal.hide();
    }

    function addCategory(categoryId, categoryName) {
        fetch(`http://localhost:3000/${categoryId}`)
            .then(response => {
                if (response.ok) {
                    throw new Error(`Category '${categoryName}' already exists.`);
                }
                return null;
            })
            .catch(() => null)
            .then(() => {
                return fetch(`http://localhost:3000/${categoryId}`, {
                    method: "PUT",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify({
                        active: true,
                        name: categoryName,
                        subcategories: []
                    })
                });
            })
            .then(response => response.json())
            .then(newCategory => {
                alert(`Category '${categoryName}' has been added.`);
                console.log(`New category added:`, newCategory);
                fetchAndUpdatePlans(categoryId);
            })
            .catch(error => {
                console.error("Error adding category:", error);
                alert(`Failed to add category: ${error.message}`);
            });
    }

    document.getElementById("addCategoryBtn").addEventListener("click", handleAddCategory);
});
