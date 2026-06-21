/* global L */

let allMarkers = [];

document.addEventListener("DOMContentLoaded", () => {
    const mapElement = document.getElementById('map');
    if (!mapElement) return;

    const venuesData = mapElement.getAttribute('data-venues');
    const venues = venuesData ? JSON.parse(venuesData) : [];

    const map = L.map('map').setView([40.4093, 49.8671], 13);

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        attribution: '© OpenStreetMap contributors'
    }).addTo(map);

    venues.forEach(venue => {
        if (!venue.latitude || !venue.longitude) return;

        const marker = L.marker([venue.latitude, venue.longitude], {
            icon: createColoredIcon(venue.color || '#6b7280')
        }).addTo(map);

        marker.bindPopup(createPopupMarkup(venue));
        marker._categoryLabel = venue.category;
        allMarkers.push(marker);
    });
});

function createColoredIcon(color) {
    return L.divIcon({
        className: '',
        html: `<div style="
            width: 14px;
            height: 14px;
            border-radius: 50%;
            background-color: ${color};
            border: 2px solid white;
            box-shadow: 0 1px 4px rgba(0,0,0,0.4);
        "></div>`,
        iconSize: [14, 14],
        iconAnchor: [7, 7],
        popupAnchor: [0, -10]
    });
}

function getCsrfToken() {
    const meta = document.querySelector('meta[name="_csrf"]');
    return meta ? meta.getAttribute('content') : '';
}

function createPopupMarkup(venue) {
    const csrf = getCsrfToken();
    const imageHtml = venue.photoUrl
        ? `<img src="${venue.photoUrl}" alt="${venue.name}" style="width:100%; border-radius:6px; margin-bottom:8px; object-fit:cover; max-height:120px;"/>`
        : '';

    const safeName = (venue.name || '').replace(/'/g, "\\'");

    return `
        <div style="min-width:200px">
            <div style="display:flex; align-items:center; gap:6px; margin-bottom:4px;">
                <div style="width:10px; height:10px; border-radius:50%; background:${venue.color}; flex-shrink:0;"></div>
                <small style="color:#888;">${venue.category || ''}</small>
            </div>
            <h6 style="margin:0 0 6px 0;">${venue.name || 'Unknown Venue'}</h6>
            <p style="font-size:12px; color:#555; margin:0 0 8px 0;">${venue.description || ''}</p>
            ${imageHtml}
            <div class="d-flex gap-2">
                <button class="btn btn-sm btn-dark w-100"
                        onclick="openAddToPlanModal(${venue.id}, '${safeName}')">Add to Plan</button>
            </div>
            <form action="/wishlist/add" method="post" class="mt-2">
                <input type="hidden" name="_csrf" value="${csrf}"/>
                <input type="hidden" name="venueId" value="${venue.id}"/>
                <button class="btn btn-sm btn-outline-dark w-100">Save to Wishlist</button>
            </form>
        </div>
    `;
}

function filterByCategory(btn) {
    const category = btn.getAttribute('data-category');

    document.querySelectorAll('.category-btn').forEach(b => b.classList.add('inactive'));
    btn.classList.remove('inactive');

    allMarkers.forEach(marker => {
        if (marker._categoryLabel === category) {
            marker.getElement() && (marker.getElement().style.display = '');
        } else {
            marker.getElement() && (marker.getElement().style.display = 'none');
        }
    });
}

function showAll(btn) {
    document.querySelectorAll('.category-btn').forEach(b => b.classList.remove('inactive'));
    allMarkers.forEach(marker => {
        marker.getElement() && (marker.getElement().style.display = '');
    });
}