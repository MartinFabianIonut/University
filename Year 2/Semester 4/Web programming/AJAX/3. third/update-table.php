<?php

    $dbname = '../ajax.db';
    $conn = new SQLite3($dbname);

    if (!$conn) {
        die("Connection failed to SQLite database");
    }

    $id = $_REQUEST['id'];
    $first_name = $_POST['first_name'];
    $last_name = $_POST['last_name'];
    $phone = $_POST['phone'];
    $email = $_POST['email'];

    $sql = "UPDATE people SET first_name = '$first_name', last_name = '$last_name', phone = '$phone', email = '$email' WHERE id = $id";
    $result = $conn->query($sql);

    // close the database connection
    $conn->close();

?>