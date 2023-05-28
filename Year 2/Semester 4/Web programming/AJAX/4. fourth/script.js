var tableVector = new Array(9).fill('_');
var userPlayer = '';
var serverPlayer = '';
var gameOver = false;

function whoIsTheFirstPlayer(){
    let p = Math.random();
    if(p < 0.5){
        userPlayer = 'X';
        serverPlayer = '0';
    }
    else{
        userPlayer = '0';
        serverPlayer = 'X';
    }
}

function isThereAWinner(){
    var request = new XMLHttpRequest();
    request.onreadystatechange = function(){
        if(request.readyState == 4){
            if(request.status == 200){
                if(request.responseText == 'Draw!'){
                    alert('Draw!');
                    gameOver = true;
                    location.reload();
                }
                else
                {
                    if(request.responseText.length > 3)
                        alert(request.responseText);
                    // if request.responseText contain won
                    if(request.responseText.indexOf('won') != -1)
                    {
                        gameOver = true;
                        location.reload();
                    }
                }
            }
            else
                alert('Error: ' + request.status);
        }
    }
    let tableString = tableVector.join('');
    request.open('GET', 'is-there-a-winner.php?table=' + tableString + '&userPlayer=' + userPlayer, false);
    request.send('');
}

function serverMove(){
    var request = new XMLHttpRequest();
    request.onreadystatechange = function(){
        if(request.readyState == 4){
            if(request.status == 200){
                let tableString = request.responseText;
                tableVector = tableString.split('');  
                refreshTable();
                isThereAWinner();
            }
            else
                alert('Error: ' + request.status);
        }
    }
    let tableString = tableVector.join('');
    request.open('GET', 'server-move.php?table=' + tableString + '&serverPlayer=' + serverPlayer, true);
    request.send('');
}

function initialiseTableVector(){
    var table = document.getElementById('board');
    var rows = table.rows;
    for(let i = 0; i < rows.length; i++){
        var cells = rows[i].cells;
        for(let j=0; j < cells.length; j++){
            cells[j].addEventListener('click', () =>{
                tableVector[i * 3 + j] = userPlayer;
                this.innerHTML = userPlayer;
                isThereAWinner();
                if(!gameOver)
                    serverMove();
            });
        }
    }
}

function refreshTable(){
    var table = document.getElementById('board');
    var rows = table.rows;
    for(let i = 0; i < rows.length; i++){
        var cells = rows[i].cells;
        for(let j = 0; j < cells.length; j++){
            if (tableVector[i * 3 + j] != '_')
                cells[j].innerHTML = tableVector[i * 3 + j];
        }
    }
}

initialiseTableVector();
whoIsTheFirstPlayer();
if(serverPlayer == 'X')
    serverMove();