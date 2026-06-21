let addToPlanModalInstance = null;

document.addEventListener("DOMContentLoaded", () => {
    const modalEl = document.getElementById('addToPlanModal');
    if (modalEl) {
        addToPlanModalInstance = new bootstrap.Modal(modalEl);
    }
});

function openAddToPlanModal(venueId, venueName) {
    document.getElementById('modalVenueId').value = venueId;
    document.getElementById('modalVenueName').textContent = venueName;
    document.getElementById('modalRedirectTo').value = window.location.pathname;

    const select = document.getElementById('modalPlanSelect');
    select.innerHTML = '<option value="">Loading your plans...</option>';

    fetch('/plans/my-plans-json')
        .then(res => res.json())
        .then(plans => {
            if (plans.length === 0) {
                select.innerHTML = '<option value="">No plans yet — create one first</option>';
                return;
            }
            select.innerHTML = plans
                .map(p => `<option value="${p.id}">${p.title}</option>`)
                .join('');
        })
        .catch(() => {
            select.innerHTML = '<option value="">Could not load plans</option>';
        });

    addToPlanModalInstance.show();
}