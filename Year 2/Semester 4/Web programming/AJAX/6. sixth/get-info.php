<?php

$dbname = '../ajax.db';
$conn = new SQLite3($dbname);

if (!$conn) {
    die("Connection failed to SQLite database");
} 

$sql = "SELECT DISTINCT manufacturer FROM notebooks";
$result = $conn->query($sql);
echo '<h3>Manufacturer</h3>';
while ($row = $result->fetchArray(SQLITE3_ASSOC)) {
    echo '<input type="checkbox" name="manufacturer[]" value="' . $row['manufacturer'] . '" id="' . $row['manufacturer'] . '"></input>' ;
    echo '<label for="' . $row['manufacturer'] . '">' . $row['manufacturer'] . '</label>';
}
echo '<br>';
echo '<h3>Processor</h3>';
$sql = "SELECT DISTINCT processor FROM notebooks";
$result = $conn->query($sql);
while ($row = $result->fetchArray(SQLITE3_ASSOC)) {
    echo '<input type="checkbox" name="processor[]" value="' . $row['processor'] . '" id="' . $row['processor'] . '"></input>' ;
    echo '<label for="' . $row['processor'] . '">' . $row['processor'] . '</label>';
}
echo '<br>';
echo '<h3>RAM</h3>';
$sql = "SELECT DISTINCT ram FROM notebooks";
$result = $conn->query($sql);
while ($row = $result->fetchArray(SQLITE3_ASSOC)) {
    echo '<input type="checkbox" name="ram[]" value="' . $row['ram'] . '" id="' . $row['ram'] . '"></input>' ;
    echo '<label for="' . $row['ram'] . '">' . $row['ram'] . '</label>';
}
echo '<br>';
echo '<h3>HDD</h3>';
$sql = "SELECT DISTINCT hdd FROM notebooks";
$result = $conn->query($sql);
while ($row = $result->fetchArray(SQLITE3_ASSOC)) {
    echo '<input type="checkbox" name="hdd[]" value="' . $row['hdd'] . '" id="' . $row['hdd'] . '"></input>' ;
    echo '<label for="' . $row['hdd'] . '">' . $row['hdd'] . '</label>';
}
echo '<br>';
echo '<h3>Video</h3>';
$sql = "SELECT DISTINCT video FROM notebooks";
$result = $conn->query($sql);
while ($row = $result->fetchArray(SQLITE3_ASSOC)) {
    echo '<input type="checkbox" name="video[]" value="' . $row['video'] . '" id="' . $row['video'] . '"></input>' ;
    echo '<label for="' . $row['video'] . '">' . $row['video'] . '</label>';
}
// close the database connection
$conn->close();

?>

