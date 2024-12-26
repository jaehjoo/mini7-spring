function drawRoute(startLat, startLng, endLat, endLng) {
    var map = new naver.maps.Map('map', {
        center: new naver.maps.LatLng((startLat + endLat) / 2, (startLng + endLng) / 2),
        zoom: 10
    });

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
                const polyline = new naver.maps.Polyline({
                    path: path.map(coord => new naver.maps.LatLng(coord[1], coord[0])),
                    strokeColor: '#5347AA',
                    strokeWeight: 2,
                    map: map
                });

                new naver.maps.Marker({
                    position: new naver.maps.LatLng(startLat, startLng),
                    map: map,
                    icon: {
                        url: '/image/patient.png',
                        scaledSize: new naver.maps.Size(50, 50),
                        origin: new naver.maps.Point(0, 0),
                        anchor: new naver.maps.Point(16, 16)
                    }
                });

                new naver.maps.Marker({
                    position: new naver.maps.LatLng(endLat, endLng),
                    map: map,
                    icon: {
                        url: '/image/hospital.png',
                        scaledSize: new naver.maps.Size(50, 50),
                        origin: new naver.maps.Point(0, 0),
                        anchor: new naver.maps.Point(16, 16)
                    }
                });

                const bounds = polyline.getBounds();
                map.fitBounds(bounds);
            } else {
                console.error("Failed to get route data");
            }
        })
        .catch(error => console.error('Error:', error));
}


/*const urlParams = new URLSearchParams(window.location.search);
const startLat = parseFloat(urlParams.get('startLat'));
const startLng = parseFloat(urlParams.get('startLng'));
const endLat = parseFloat(urlParams.get('endLat'));
const endLng = parseFloat(urlParams.get('endLng'));

var map = new naver.maps.Map('map', {
        center: new naver.maps.LatLng((startLat + endLat) / 2, (startLng + endLng) / 2),
        zoom: 10
});

function drawRoute(startLat, startLng, endLat, endLng) {
    fetch(`/api/map/rest/dynamic?startLat=${startLat}&startLng=${startLng}&endLat=${endLat}&endLng=${endLng}`)
        .then(response => response.json())
        .then(data => {
            if (data.code === 0 && data.route && data.route.traoptimal) {
                const path = data.route.traoptimal[0].path;
                const polyline = new naver.maps.Polyline({
                    path: path.map(coord => new naver.maps.LatLng(coord[1], coord[0])),
                    strokeColor: '#5347AA',
                    strokeWeight: 2,
                    map: map
                });

                var startMarker = new naver.maps.Marker({
                    position: new naver.maps.LatLng(startLat, startLng),
                    map: map,
                    icon: {
                        url: '/image/patient.png',
                        scaledSize: new naver.maps.Size(50, 50),
                        origin: new naver.maps.Point(0, 0),
                        anchor: new naver.maps.Point(16, 16)
                    }
                });

                var endMarker = new naver.maps.Marker({
                    position: new naver.maps.LatLng(endLat, endLng),
                    map: map,
                    icon: {
                        url: '/image/hospital.png',
                        scaledSize: new naver.maps.Size(50, 50),
                        origin: new naver.maps.Point(0, 0),
                        anchor: new naver.maps.Point(16, 16)
                    }
                });

                const bounds = polyline.getBounds();
                map.fitBounds(bounds);
            } else {
                console.error("Failed to get route data");
            }
        })
        .catch(error => console.error('Error:', error));
}

drawRoute(startLat, startLng, endLat, endLng);*/