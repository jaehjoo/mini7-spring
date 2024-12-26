$(document).ready(function() {
    $('.hospital-row').on('click', function() {
        const href = $(this).data('href');

        $('#mapModal').modal('show');

        const urlParams = new URLSearchParams(href.split('?')[1]);
        const startLat = parseFloat(urlParams.get('startLat'));
        const startLng = parseFloat(urlParams.get('startLng'));
        const endLat = parseFloat(urlParams.get('endLat'));
        const endLng = parseFloat(urlParams.get('endLng'));

        drawRoute(startLat, startLng, endLat, endLng);
    });
});