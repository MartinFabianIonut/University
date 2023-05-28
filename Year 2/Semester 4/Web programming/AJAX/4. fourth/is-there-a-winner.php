<?php
    $tableString = $_REQUEST['table'];
    $userPlayer = $_REQUEST['userPlayer'];
    // strig of user player

    function userOrServerWinner($content){
        $userPlayer = $_REQUEST['userPlayer'];
        if($content == $userPlayer){
            echo 'You won!';  
        }
        else{
            echo 'Server won!';
        }
    }

    
    // check rows
    for($i = 0; $i < 3; $i++){
        if($tableString[$i*3] == $tableString[$i*3+1] && $tableString[$i*3+1] == $tableString[$i*3+2] && $tableString[$i*3] != '_'){
            userOrServerWinner($tableString[$i*3]);
            return;
        }
    }

    // check columns
    for($i = 0; $i < 3; $i++){
        if($tableString[$i] == $tableString[$i+3] && $tableString[$i+3] == $tableString[$i+6] && $tableString[$i] != '_'){
            userOrServerWinner($tableString[$i]);
            return;
        }
    }

    // check diagonals
    if($tableString[0] == $tableString[4] && $tableString[4] == $tableString[8] && $tableString[0] != '_'){
        userOrServerWinner($tableString[0]);
        return;
    }

    if($tableString[2] == $tableString[4] && $tableString[4] == $tableString[6] && $tableString[2] != '_'){
        userOrServerWinner($tableString[2]);
        return;
    }

    // draw case

    if(strpos($tableString, '_') === false){
        echo 'Draw!';
        return;
    }

?>

