<?php

    $tableString = $_REQUEST['table'];
    $serverPlayer = $_REQUEST['serverPlayer'];
    while(true){
        $pos = rand(0, strlen($tableString)-1);
        if($tableString[$pos] == '_'){
            $tableString[$pos] = $serverPlayer;
            echo $tableString;
            break;
        }
    }

?>

