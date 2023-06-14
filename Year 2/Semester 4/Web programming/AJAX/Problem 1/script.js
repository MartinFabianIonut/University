showDepartures();
showArrivalsForDeparture();

// function showDepartures(){
//     var request = new XMLHttpRequest();
//     request.onreadystatechange = function(){
//         if(request.readyState == 4){ // request finished and response is ready
//             if(request.status == 200){
//                 depList = document.getElementById('departureStations').innerHTML = request.responseText;
//             }
//             else
//                 alert('Error: ' + request.status);
//         }
//     };
//     request.open("GET", "get-departure-stations.php", false);
//     // false for synchronous request - need to wait for the response
//     request.send('');
// }

function showDepartures() {
    var request = new XMLHttpRequest();
    request.onreadystatechange = function() {
        if (request.readyState == 4) { // request finished and response is ready
            if (request.status == 200) {
                document.getElementById('departureStations').innerHTML = request.responseText;
            } else {
                alert('Error: ' + request.status);
            }
        }
    };
    request.open("POST", "get-departure-stations.php", false);
    // Set the Content-Type header for POST requests
    request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    request.send();
}


function showArrivalsForDeparture(){
    var departures = document.getElementById('departureStations').getElementsByTagName('li');
    for(let i=0; i < departures.length; i++){
        departures[i].onclick = function() {
            var request = new XMLHttpRequest();
            request.onreadystatechange = function(){
                if(request.readyState == 4){
                    if(request.status == 200){
                        document.getElementById('arrivalStations').innerHTML = request.responseText;
                    }
                    else
                        alert('Error: ' + request.status);
                }
            }
            request.open('GET', 'get-arrival-stations.php?departure=' + this.innerHTML, true);
            request.send('');
        };
    }
}