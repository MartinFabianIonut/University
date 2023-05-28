var filters = '?';

function getInfo(){
    var request = new XMLHttpRequest();
    request.onreadystatechange = function(){
        if(request.readyState == 4){
            if(request.status == 200){
                document.getElementById('combo').innerHTML = request.responseText;
            }
            else
                alert('Error: ' + request.status);
        }
    }
    request.open('GET', 'get-info.php', false);
    request.send('');
}

function getFilteredInfo(){
    var request = new XMLHttpRequest();
    request.onreadystatechange = function(){
        if(request.readyState == 4){
            if(request.status == 200){
                document.getElementById('table').innerHTML = request.responseText;
            }
            else
                alert('Error: ' + request.status);
        }
    }
    request.open('GET', 'get-filtered-info.php' + filters, true);
    request.send('');
}

getInfo();
document.querySelectorAll("input[type=checkbox]").forEach((checkbox) => {
    checkbox.addEventListener("change", () => {
        if (checkbox.checked) {
            filters += "&" + checkbox.name + "=" + checkbox.id;
        } else {
            filters = filters.replace("&" + checkbox.name + "=" + checkbox.id, "");
        }
        getFilteredInfo();
    });
});