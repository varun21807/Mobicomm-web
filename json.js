document.addEventListener("DOMContentLoaded", function () {
    const apiUrl = "http://localhost:3000/categories";
    let planTabs = document.getElementById("planTabs");
    let planContent = document.getElementById("planContent");

    fetch(apiUrl)
        .then(response => response.json())
        .then(categories => {
            let tabLinks = '';
            let tabPanels = '';

            categories.forEach((category, index) => {
                let tabClass = index === 0 ? "nav-link active" : "nav-link";
                let contentClass = index === 0 ? "tab-pane fade show active" : "tab-pane fade";
                
                tabLinks += `<li class="nav-item">
                    <a class="${tabClass}" data-bs-toggle="pill" href="#${category.id}">${category.name}</a>
                </li>`;
                
                tabPanels += `<div class="${contentClass}" id="${category.id}">
                    <p>Loading ${category.name}...</p>
                </div>`;
            });

            planTabs.innerHTML = tabLinks;
            planContent.innerHTML = tabPanels;

            categories.forEach(category => fetchAndUpdatePlans(category));
        })
        .catch(error => console.error("Error fetching categories:", error));

    function fetchAndUpdatePlans(category) {
        let tabContent = document.getElementById(category.id);
        let accordionHtml = `<div class="accordion" id="accordion${category.id}">`;

        category.subcategories.forEach((subcategory, index) => {
            accordionHtml += createAccordionItem(subcategory.name, subcategory.plans, category.id, index + 1);
        });

        accordionHtml += "</div>";
        tabContent.innerHTML = accordionHtml;
    }

    function createAccordionItem(subheading, plans, categoryId, accordionId) {
        let plansHtml = plans.map((plan, index) => createPlanCard(plan, categoryId, accordionId, index)).join("");

        return `<div class="accordion-item border-0 rounded mb-3 shadow-sm">
            <p class="accordion-header" id="heading${categoryId}${accordionId}">
                <button class="accordion-button ${accordionId === 1 ? '' : 'collapsed'}" type="button"
                    data-bs-toggle="collapse" data-bs-target="#collapse${categoryId}${accordionId}"
                    aria-expanded="${accordionId === 1 ? 'true' : 'false'}"
                    aria-controls="collapse${categoryId}${accordionId}">
                    ${subheading}
                </button>
            </p>
            <div id="collapse${categoryId}${accordionId}" class="accordion-collapse collapse ${accordionId === 1 ? 'show' : ''}"
                aria-labelledby="heading${categoryId}${accordionId}" data-bs-parent="#accordion${categoryId}">
                <div class="accordion-body">
                    <div class="row">${plansHtml}</div>
                </div>
            </div>
        </div>`;
    }

    function createPlanCard(plan, categoryId, accordionId, index) {
        return `<div class="col-md-6 col-lg-4 mb-3">
            <div class="card plan-card p-3 text-center position-relative">
                <span class="badge bg-${plan.badgeColor || 'primary'} position-absolute top-0 start-50 translate-middle-x">
                    ${plan.offer || ''}
                </span>

                <div class="plan-icon position-absolute top-0 end-0 m-3">
                    <i class="fas fa-wifi fa-lg text-primary"></i>
                </div>

                <h3 class="fw-bold mt-4">₹${plan.price || 'N/A'}</h3>
                <p>${plan.duration ? `${plan.duration} Days` : ''} ${plan.data ? ` | ${plan.data}/Day` : ''}</p>

                <div class="d-flex gap-2">
                    <button class="btn btn-primary show-details-btn w-50" data-bs-toggle="collapse"
                        data-bs-target="#planDetails${categoryId}${accordionId}${index}" aria-expanded="false">
                        <i class="fas fa-info-circle"></i> See More
                    </button>
                    <button class="btn buy-now-btn w-50" data-bs-toggle="modal" data-bs-target="#getplanModal"
                        data-plan='${JSON.stringify(plan)}'>
                        <i class="fas fa-shopping-cart"></i> Buy Now
                    </button>
                </div>

                <div class="collapse mt-3 text-start" id="planDetails${categoryId}${accordionId}${index}">
                    <hr>
                    <p><i class="fas fa-calendar-alt"></i> <strong>Duration:</strong> ${plan.duration || 'N/A'} Days</p>
                    <p><i class="fas fa-database"></i> <strong>Data:</strong> ${plan.data || 'N/A'}</p>
                    <p><i class="fas fa-phone-alt"></i> <strong>Calls:</strong> ${plan.calls || 'N/A'}</p>
                    <p><i class="fas fa-envelope"></i> <strong>SMS:</strong> ${plan.sms || 'N/A'} messages</p>
                    <p><i class="fas fa-tv"></i> <strong>OTT Subscriptions:</strong> ${plan.ott || 'None'}</p>
                    <p><i class="fas fa-gift"></i> <strong>Additional Benefits:</strong> ${plan.benefits || 'N/A'}</p>
                    <p><i class="fas fa-tag"></i> <strong>Offer:</strong> ${plan.offer || 'N/A'}</p>
                    
                    <button class="btn buy-now-btn w-100" data-bs-toggle="modal" data-bs-target="#getplanModal"
                        data-plan='${JSON.stringify(plan)}'>
                        <i class="fas fa-shopping-cart"></i> Buy Now
                    </button>
                </div>
            </div>
        </div>`;
    }

    document.addEventListener("click", function (event) {
        if (event.target.classList.contains("show-details-btn")) {
            let button = event.target;
            let targetId = button.getAttribute("data-bs-target");
            let targetCollapse = document.querySelector(targetId);
            let buyNowButton = button.nextElementSibling;

            if (!targetCollapse.classList.contains("show")) {
                button.innerHTML = '<i class="fas fa-chevron-up"></i> See Less';
                button.classList.remove("w-50");
                button.classList.add("w-100");
                if (buyNowButton && buyNowButton.classList.contains("buy-now-btn")) {
                    buyNowButton.style.display = "none";
                }
                targetCollapse.addEventListener('hidden.bs.collapse', function () {
                    button.innerHTML = '<i class="fas fa-info-circle"></i> See More';
                    button.classList.remove("w-100");
                    button.classList.add("w-50");
                    if (buyNowButton && buyNowButton.classList.contains("buy-now-btn")) {
                        buyNowButton.style.display = "inline-block";
                    }
                }, { once: true });
            }
        }
    });
});