<?php

$dbname = '../ajax.db';
$conn = new SQLite3($dbname);

if (!$conn) {
    die("Connection failed to SQLite database");
} 

$currentPage = $_REQUEST['currentPage'];
$dimension = $_REQUEST['dimension'];
$skip = $currentPage * $dimension;

$sql = "SELECT * FROM people LIMIT $dimension OFFSET $skip";
$result = $conn->query($sql);
while ($row = $result->fetchArray(SQLITE3_ASSOC)) {
    echo '<tr>' . '<td>' . $row['first_name'] . '</td>' . '<td>' . $row['last_name'] . '</td>' . '<td>' . $row['phone'] . '</td>' . '<td>' . $row['email'] . '</td>' . '</tr>';
}
// close the database connection
$conn->close();

?>

