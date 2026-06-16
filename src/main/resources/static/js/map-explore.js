/* global L */

function getCsrfToken() {
    const meta = document.querySelector('meta[name="_csrf"]');
    const header = document.querySelector('meta[name="_csrf_header"]');
    return {
        token: meta ? meta.getAttribute('content') : '',
        header: header ? header.getAttribute('content') : ''
    };
}

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
        if (venue.latitude && venue.longitude) {
            const marker = L.marker([venue.latitude, venue.longitude]).addTo(map);

            const popupContent = createPopupMarkup(venue);
            marker.bindPopup(popupContent);
        }
    });
});


function createPopupMarkup(venue) {
    const csrf = getCsrfToken();
    const imageAlt = venue.name ? venue.name.replace(/"/g, '&quot;') : 'Venue image';

    const imageHtml = venue.photoUrl
        ? `<img src="${venue.photoUrl}" alt="${imageAlt}" style="width:100%; border-radius:6px; margin-bottom:6px; object-fit:cover; aspect-ratio: 16/9;"/>`
        : '';

    return `
        <div style="min-width:200px">
            <h6>${venue.name || 'Unknown Venue'}</h6>
            <small class="text-muted">${venue.category || ''}</small>
            <p style="font-size:13px; margin: 4px 0;">${venue.description || ''}</p>
            ${imageHtml}
            <form action="/wishlist/add" method="post">
                <input type="hidden" name="_csrf" value="${csrf.token}"/>
                <input type="hidden" name="venueId" value="${venue.id || ''}"/>
                <button class="btn btn-sm btn-outline-dark w-100">Save to Wishlist</button>
            </form>
        </div>
    `;
}