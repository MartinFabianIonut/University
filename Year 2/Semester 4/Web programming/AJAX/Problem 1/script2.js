$(document).ready(function() {
    showDepartures();
    showArrivalsForDeparture();
});

// function showDepartures() {
//     $.ajax({
//         url: "get-departure-stations.php",
//         method: "GET",
//         async: false, // synchronous request - need to wait for the response
//         success: function(response) {
//             $('#departureStations').html(response);
//         },
//         error: function(xhr) {
//             alert('Error: ' + xhr.status);
//         }
//     });
// }

function showDepartures() {
    $.ajax({
        url: "get-departure-stations.php",
        method: "POST",
        async: false, // synchronous request - need to wait for the response
        dataType: "html",
        success: function(response) {
            $('#departureStations').html(response);
        },
        error: function(xhr) {
            alert('Error: ' + xhr.status);
        }
    });
}


function showArrivalsForDeparture() {
    $('#departureStations li').click(function(){
        var departure = $(this).html(); // departure station
        $.ajax({
            url: 'get-arrival-stations.php',
            method: 'GET',
            async: true,
            data: 'departure=' + departure,
            success: function(response) {
                $('#arrivalStations').html(response);
            },
            error: function(xhr) {
                alert('Error: ' + xhr.status);
            }
        });
    });
}