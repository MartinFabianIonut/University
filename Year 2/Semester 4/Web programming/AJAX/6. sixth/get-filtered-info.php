<?php
    $dbname = '../ajax.db';
    $conn = new SQLite3($dbname);
    
    if (!$conn) {
        die("Connection failed to SQLite database");
    } 

    // filter notebooks by manufacturer, processor, ram, hdd and video
    $notebooks = array();
    
    // echo table with filtered notebooks
    echo '<tr>';
    echo '<th>Manufacturer</th>';
    echo '<th>Processor</th>';
    echo '<th>RAM</th>';
    echo '<th>HDD</th>';
    echo '<th>Video</th>';
    echo '</tr>';

    if(isset($_REQUEST['manufacturer']))
    {
        $manufacturer = $_REQUEST['manufacturer'];
        // for each manufacturer
        foreach ($manufacturer as $m) {
            $sql = "SELECT * FROM notebooks WHERE manufacturer = '$m'";
            $result = $conn->query($sql);
            $rows = array();  
            while ($row = $result->fetchArray(SQLITE3_ASSOC)) {
                $rows[] = $row;  
            }
            $notebooks = array_merge($notebooks,$rows);
        }
    }

    if(isset($_REQUEST['processor']))
    {
        $processor = $_REQUEST['processor'];
        foreach ($processor as $p) {
            $sql = "SELECT * FROM notebooks WHERE processor = '$p'";
            $result = $conn->query($sql);
            $rows = array();  
            while ($row = $result->fetchArray(SQLITE3_ASSOC)) {
                $rows[] = $row;  
            }
            $notebooks = array_merge($notebooks,$rows);        }
    }
    
    if (isset($_REQUEST['ram']))
    {
        $ram = $_REQUEST['ram'];
        foreach ($ram as $r) {
            $sql = "SELECT * FROM notebooks WHERE ram = '$r'";
            $result = $conn->query($sql);
            $rows = array();  
            while ($row = $result->fetchArray(SQLITE3_ASSOC)) {
                $rows[] = $row;  
            }
            $notebooks = array_merge($notebooks,$rows);        }
    }

    if (isset($_REQUEST['hdd']))
    {
        $hdd = $_REQUEST['hdd'];
        foreach ($hdd as $h) {
            $sql = "SELECT * FROM notebooks WHERE hdd = '$h'";
            $result = $conn->query($sql);
            $rows = array();  
            while ($row = $result->fetchArray(SQLITE3_ASSOC)) {
                $rows[] = $row;  
            }
            $notebooks = array_merge($notebooks,$rows);        }
    }

    if (isset($_GET['video']))
    {
        $video = $_GET['video'];
        foreach ($video as $v) {
            $sql = "SELECT * FROM notebooks WHERE video = '$v'";
            $result = $conn->query($sql);
            $rows = array();  
            while ($row = $result->fetchArray(SQLITE3_ASSOC)) {
                $rows[] = $row;  
            }
            $notebooks = array_merge($notebooks,$rows);        }
    }

    // remove duplicates
    $notebooks = array_unique($notebooks, SORT_REGULAR);

    // print notebooks
    foreach ($notebooks as $n) {
        echo '<tr>';
        echo '<td>' . $n['manufacturer'] . '</td>';
        echo '<td>' . $n['processor'] . '</td>';
        echo '<td>' . $n['ram'] . '</td>';
        echo '<td>' . $n['hdd'] . '</td>';
        echo '<td>' . $n['video'] . '</td>';
        echo '</tr>';
    }
    
    $conn->close();
?>