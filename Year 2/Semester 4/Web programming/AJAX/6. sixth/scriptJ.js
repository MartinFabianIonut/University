var filters = '?';

function getInfo() {
    $.ajax({
        url: 'get-info.php',
        method: 'GET',
        async: false,
        success: function(response) {
            $('#combo').html(response);
        },
        error: function(xhr) {
            alert('Error: ' + xhr.status);
        }
    });
}

function getFilteredInfo() {
    $.ajax({
        url: 'get-filtered-info.php' + filters,
        method: 'GET',
        success: function(response) {
            $('#table').html(response);
        },
        error: function(xhr) {
            alert('Error: ' + xhr.status);
        }
    });
}

getInfo();

$("input[type=checkbox]").change(function() {
    var checkbox = $(this);
    if (checkbox.is(":checked")) {
        filters += "&" + checkbox.attr("name") + "=" + checkbox.attr("id");
    } else {
        filters = filters.replace("&" + checkbox.attr("name") + "=" + checkbox.attr("id"), "");
    }
    getFilteredInfo();
});
