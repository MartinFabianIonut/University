<?php

$dbname = '../ajax.db';
$conn = new SQLite3($dbname);

if (!$conn) {
    die("Connection failed to SQLite database");
} 

$sql = "SELECT COUNT(*) as nr FROM people";
$result = $conn->query($sql);

while ($row = $result->fetchArray(SQLITE3_ASSOC)) {
    echo $row['nr'];
}
// close the database connection
$conn->close();

?>

