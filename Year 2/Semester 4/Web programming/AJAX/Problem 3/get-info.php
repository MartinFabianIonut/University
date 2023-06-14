<?php

$dbname = '../ajax.db';
$conn = new SQLite3($dbname);

if (!$conn) {
    die("Connection failed to SQLite database");
} 

$id = $_REQUEST['id'];

$sql = "SELECT * FROM people WHERE id = $id";
$result = $conn->query($sql);
while ($row = $result->fetchArray(SQLITE3_ASSOC)) {
    echo '<people>' . '<first_name>' . $row['first_name'] . '</first_name>' . '<last_name>' . $row['last_name'] . '</last_name>' . '<phone>' . $row['phone'] . '</phone>' . '<email>' . $row['email'] . '</email>' . '</people>';
}
// close the database connection
$conn->close();

?>

