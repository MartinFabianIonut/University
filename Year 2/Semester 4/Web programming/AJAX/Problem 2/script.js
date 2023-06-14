var currentPage = 0;
var dimension = 3;
var pages = 0;

function getNoOfPages(){
    var request = new XMLHttpRequest();
    request.onreadystatechange = function(){
        if(request.readyState == 4){
            if(request.status == 200){
                pages = request.responseText / dimension;
                echo(pages);
            }
            else
                alert('Error: ' + request.status);
        }
    };
    request.open('GET', 'get-no-of-pages.php', false);
    request.send('');
}

function showPage(page){
    var request = new XMLHttpRequest();
    request.onreadystatechange = function(){
        if(request.readyState == 4){
            if(request.status == 200){
                let table = document.getElementById('people');
                table.innerHTML = table.rows[0].innerHTML + request.responseText;
                // keep the header row
            }
            else
                alert('Error: ' + request.status);
        }
    };
    request.open('GET', 'get-page.php?currentPage=' + page + '&dimension=' + dimension, true);
    request.send('');
}

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

document.getElementById('previous-button').onclick = function(){
    currentPage--;
    showPage(currentPage);
    buttonState();
}

document.getElementById('next-button').onclick = function(){
    currentPage++;
    showPage(currentPage);
    buttonState();
}

getNoOfPages();
showPage(currentPage);
buttonState();