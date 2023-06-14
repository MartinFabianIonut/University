<?php

$dbname = '../ajax.db';
$conn = new SQLite3($dbname);

if (!$conn) {
    die("Connection failed to SQLite database");
} 
$departure = $_REQUEST['departure'];

$sql = "SELECT DISTINCT arrival FROM stations WHERE departure = '$departure'";
$result = $conn->query($sql);

while ($row = $result->fetchArray(SQLITE3_ASSOC)) {
    echo '<li>' . $row['arrival'] . '</li>';
}
// close the database connection
$conn->close();

?>

