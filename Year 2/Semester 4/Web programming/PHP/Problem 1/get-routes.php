<?php

function validateData($data)
{
    $data = trim($data);
    $data = stripslashes($data);
    $data = htmlspecialchars($data);
    return $data;
}

$departure = addslashes(htmlentities($_GET['departure'], ENT_COMPAT, "UTF-8")); // prevents Cross-Site Scripting (XSS) attacks
$arrival = addslashes(htmlentities($_GET['arrival'], ENT_COMPAT, "UTF-8"));
$departure = validateData($departure);
$arrival = validateData($arrival);
$intermediate = false;
if (isset($_GET['checkbox'])) {
    $intermediate = true;
}
$dbname = '../php.db';
$conn = new SQLite3($dbname);

if (!$conn) {
    die("Connection failed to SQLite database");
}

$sql = "SELECT * FROM stations where departure = ? AND arrival = ? ORDER BY departure ASC";
$stmt = $conn->prepare($sql); // prepare the sql statement and prevent SQL injection
$stmt->bindValue(1, $departure, SQLITE3_TEXT);
$stmt->bindValue(2, $arrival, SQLITE3_TEXT);
$result = $stmt->execute();

print '<h2>Routes</h2>';
// if there are results
while ($row = $result->fetchArray(SQLITE3_ASSOC)) {
    print '<li>' . $row['id'] . ' ' . $row['type'] . ' ' . $row['departure'] . ' ' . $row['arrival'] . ' ' . $row['hour_departure'] . ' ' . $row['hour_arrival'] . '</li>';
}
// if there is no direct route, check for intermediate routes (if $intermediate is true)
if ($intermediate == true) {
    // check for intermediate routes, so that we can get from departure to arrival with one or more intermediate stops (e.g. from A to C with a stop in B)
    // sql statement to get all intermediate routes
    //print '<p>'. 'Intermediate routes' . '</p>';
    $sql = "SELECT s1.id AS s1id, s1.type AS s1type, s1.departure AS s1departure, s1.arrival AS s1arrival, s1.hour_departure AS s1hour_departure, s1.hour_arrival AS s1hour_arrival, 
    s2.id AS s2id, s2.type AS s2type, s2.departure AS s2departure, s2.arrival AS s2arrival, s2.hour_departure AS s2hour_departure, s2.hour_arrival AS s2hour_arrival FROM 
    stations s1, stations s2 WHERE s1.departure = ? AND s2.arrival = ? AND s1.arrival = s2.departure AND s1.hour_arrival < s2.hour_departure ORDER BY s1.departure ASC";
    $stmt = $conn->prepare($sql); // prepare the sql statement and prevent SQL injection
    $stmt->bindValue(1, $departure, SQLITE3_TEXT);
    $stmt->bindValue(2, $arrival, SQLITE3_TEXT);
    $result = $stmt->execute();
    // if there are results
    while ($row = $result->fetchArray(SQLITE3_ASSOC)) {
        print '<li>' . $row['s1id'] . ' ' . $row['s1type'] . ' ' . $row['s1departure'] . ' ' . $row['s1arrival'] . ' ' . $row['s1hour_departure'] . ' ' . $row['s1hour_arrival'] . ' -> ' . $row['s2id'] . ' ' . $row['s2type'] . ' ' . $row['s2departure'] . ' ' . $row['s2arrival'] . ' ' . $row['s2hour_departure'] . ' ' . $row['s2hour_arrival'] . '</li>';
    }
}

// close the database connection
$conn->close();
