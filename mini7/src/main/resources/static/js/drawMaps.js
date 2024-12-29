let map;
let polyline; 
const markers = []; 

function initializeMap(startLat, startLng, endLat, endLng) {
    map = new naver.maps.Map('map', {
        center: new naver.maps.LatLng((startLat + endLat) / 2, (startLng + endLng) / 2),
        zoom: 10
    });
}

function clearRoute() {
    if (polyline) {
        polyline.setMap(null); 
    }
    markers.forEach(marker => marker.setMap(null)); 
    markers.length = 0;
}

function drawRoute(startLat, startLng, endLat, endLng) {
    clearRoute();

    if (!map) {
        initializeMap(startLat, startLng, endLat, endLng);
    }

    fetch(`/api/map/rest/dynamic?startLat=${startLat}&startLng=${startLng}&endLat=${endLat}&endLng=${endLng}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            if (data.code === 0 && data.route && data.route.traoptimal) {
                const path = data.route.traoptimal[0].path;
                polyline = new naver.maps.Polyline({
                    path: path.map(coord => new naver.maps.LatLng(coord[1], coord[0])),
                    strokeColor: '#5347AA',
                    strokeWeight: 2,
                    map: map
                });

                const startMarker = new naver.maps.Marker({
                    position: new naver.maps.LatLng(startLat, startLng),
                    map: map,
                    icon: {
                        url: '/image/patient.png',
                        scaledSize: new naver.maps.Size(50, 50),
                        origin: new naver.maps.Point(0, 0),
                        anchor: new naver.maps.Point(16, 16)
                    }
                });
                markers.push(startMarker);

                const endMarker = new naver.maps.Marker({
                    position: new naver.maps.LatLng(endLat, endLng),
                    map: map,
                    icon: {
                        url: '/image/hospital.png',
                        scaledSize: new naver.maps.Size(50, 50),
                        origin: new naver.maps.Point(0, 0),
                        anchor: new naver.maps.Point(16, 16)
                    }
                });
                markers.push(endMarker);

                const bounds = polyline.getBounds();
                map.fitBounds(bounds);
            } else {
                console.error("Failed to get route data");
            }
        })
        .catch(error => console.error('Error:', error));
}
