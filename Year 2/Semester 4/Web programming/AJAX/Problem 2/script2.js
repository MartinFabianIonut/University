$(document).ready(function() {
    getNoOfPages();
    showPage(currentPage);
    buttonState();
});

var currentPage = 0;
var dimension = 3;
var pages = 0;

function getNoOfPages() {
    $.ajax({
        url: 'get-no-of-pages.php',
        method: 'GET',
        async: false,
        success: function(response) {
            pages = response / dimension;
        },
        error: function(xhr) {
            alert('Error: ' + xhr.status);
        }
    });
}

function showPage(page) {
    $.ajax({
        url: 'get-page.php',
        method: 'GET',
        data: 'currentPage=' + page + '&dimension=' + dimension,
        success: function(response) {
            let headers = '<tr>' + $('#people').find('tr').eq(0).html() + '</tr>';
            $('#people').html(headers + response);
        },
        error: function(xhr) {
            alert('Error: ' + xhr.status);
        }
    });
}

// function buttonState() {
//     if (currentPage == 0)
//         $('#previous-button').prop('disabled', true);
//     else
//         $('#previous-button').prop('disabled', false);

//     if (currentPage > pages - 1)
//         $('#next-button').prop('disabled', true);
//     else
//         $('#next-button').prop('disabled', false);
// }
// doesn't work

function buttonState(){
    if(currentPage == 0)
        document.getElementById('previous-button').disabled = true;
    else
        document.getElementById('previous-button').disabled = false;
    if(currentPage > pages - 1)
        document.getElementById('next-button').disabled = true;
    else
        document.getElementById('next-button').disabled = false;
}

$('#previous-button').click(function() {
    currentPage--;
    showPage(currentPage);
    buttonState();
});

$('#next-button').click(function() {
    currentPage++;
    showPage(currentPage);
    buttonState();
});