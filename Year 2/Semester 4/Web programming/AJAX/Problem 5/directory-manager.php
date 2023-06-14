<?php

    $directory = $_GET["directory"];

    if (is_file($directory)) {
        echo "<div onclick=loadDirectory('../..')>..</div>";
        echo strip_tags(file_get_contents($directory), "<html>");
        exit();
    }

    $files = scandir($directory);
    foreach($files as $file) {
        echo "<li><div onclick=loadDirectory('" . $_GET["directory"] . "/" . $file . "')>" . $file . "</div></li>";
    }

?>