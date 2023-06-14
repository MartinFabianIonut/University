<?php

$dbname = '../ajax.db';
$conn = new SQLite3($dbname);

if (!$conn) {
    die("Connection failed to SQLite database");
} 

$sql = "SELECT id FROM people";
$result = $conn->query($sql);

while ($row = $result->fetchArray(SQLITE3_ASSOC)) {
    echo '<li>' . $row['id'] . '</li>';
}
// close the database connection
$conn->close();

?>

