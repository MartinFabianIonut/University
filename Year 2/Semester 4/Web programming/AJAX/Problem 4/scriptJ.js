var tableVector = new Array(9).fill('_');
var userPlayer = '';
var serverPlayer = '';
var gameOver = false;

function whoIsTheFirstPlayer() {
    let p = Math.random();
    if (p < 0.5) {
        userPlayer = 'X';
        serverPlayer = '0';
    } else {
        userPlayer = '0';
        serverPlayer = 'X';
    }
}

function isThereAWinner() {
    $.ajax({
        url: 'is-there-a-winner.php',
        type: 'GET',
        data: {
            table: tableVector.join(''),
            userPlayer: userPlayer
        },
        success: function (response) {
            if (response == 'Draw!') {
                alert('Draw!');
                gameOver = true;
                location.reload();
            } else {
                if (response.length > 3)
                    alert(response);
                if (response.indexOf('won') != -1) {
                    gameOver = true;
                    location.reload();
                }
            }
        },
        error: function (xhr) {
            alert('Error: ' + xhr.status);
        },
        async: false
    });
}

function serverMove() {
    $.ajax({
        url: 'server-move.php',
        type: 'GET',
        data: {
            table: tableVector.join(''),
            serverPlayer: serverPlayer
        },
        success: function (response) {
            let tableString = response;
            tableVector = tableString.split('');
            refreshTable();
            isThereAWinner();
        },
        error: function (xhr) {
            alert('Error: ' + xhr.status);
        }
    });
}

function initialiseTableVectorJ(){
    $('#board td').click(function () {
        let rowIndex = $(this).parent().index();
        let columnIndex = $(this).index();
        let vectorIndex = rowIndex * 3 + columnIndex;
        tableVector[vectorIndex] = userPlayer;
        $(this).text(userPlayer);
        isThereAWinner();
        if (!gameOver)
            serverMove();
    });
}

function refreshTable(){
    var table = $('#board')[0];
    var rows = table.rows;
    for(let i = 0; i < rows.length; i++){
        var cells = rows[i].cells;
        for(let j = 0; j < cells.length; j++){
            if (tableVector[i * 3 + j] != '_')
                cells[j].innerHTML = tableVector[i * 3 + j];
        }
    }
}


$(document).ready(function() {
    initialiseTableVectorJ();
    whoIsTheFirstPlayer();
    if (serverPlayer == 'X')
        serverMove();
    alert('You are ' + userPlayer + ' and the server is ' + serverPlayer);
});
