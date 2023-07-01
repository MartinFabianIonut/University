<?php
session_start();

// Check if the user is authenticated
if (!isset($_SESSION["user_name"])) {
    // Redirect to the login page or display an error message
    header("Location: index.php");
    exit;
}

$conn = new SQLite3('../php.db');
if (!$conn) {
    die("Connection failed to SQLite database");
}
// Check if the user is authenticated as an administrator
$isAdmin = false;
if (isset($_SESSION["user_role"]) && $_SESSION["user_role"] === "admin") {
    $isAdmin = true;
}

// Handle comment approval
if ($isAdmin && $_SERVER["REQUEST_METHOD"] == "POST" && isset($_POST["approve"])) {
    $commentId = $_POST["approve"];

    // Update the comment's approved status
    $sql = "UPDATE comments SET approved = 1 WHERE id = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bindParam(1, $commentId, SQLITE3_INTEGER);
    $stmt->execute();

    // Redirect back to the admin page
    header("Location: admin.php");
    exit();
}

// Handle comment deletion
if ($isAdmin && $_SERVER["REQUEST_METHOD"] == "POST" && isset($_POST["delete"])) {
    $commentId = $_POST["delete"];

    // Delete the comment
    $sql = "DELETE FROM comments WHERE id = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bindParam(1, $commentId, SQLITE3_INTEGER);
    $stmt->execute();

    // Redirect back to the admin page
    header("Location: admin.php");
    exit();
}

// Retrieve and display the unapproved comments
function displayUnapprovedComments()
{
    global $conn;

    // Display the article
    $sql = "SELECT * FROM articles";
    $stmt = $conn->prepare($sql);
    $result = $stmt->execute();

    while ($row = $result->fetchArray(SQLITE3_ASSOC)) {
        echo "<h3>" . 'Articol: ' . htmlspecialchars($row["title"]) . "</h3>";

        $sql2 = "SELECT id, name, comment FROM comments WHERE approved = 0 AND article_id = ?";
        $stmt2 = $conn->prepare($sql2);
        $articleId = $row["id"];
        $stmt2->bindParam(1, $articleId, SQLITE3_INTEGER);
        $result2 = $stmt2->execute();

        echo "<h4>Comments</h4>";
        echo "<ul>";
        while ($row2 = $result2->fetchArray(SQLITE3_ASSOC)) {
            echo "<li><strong>" . htmlspecialchars($row2["name"]) . ":</strong> " . htmlspecialchars($row2["comment"]);
            echo "<form method='POST' action='" . htmlspecialchars($_SERVER["PHP_SELF"]) . "' style='display: inline; margin-left: 10px;'>";
            echo "<input type='hidden' name='approve' value='" . htmlspecialchars($row2["id"]) . "'>";
            echo "<button type='submit'>Approve</button>";
            echo "</form>";

            echo "<form method='POST' action='" . htmlspecialchars($_SERVER["PHP_SELF"]) . "' style='display: inline; margin-left: 10px;'>";
            echo "<input type='hidden' name='delete' value='" . htmlspecialchars($row2["id"]) . "'>";
            echo "<button type='submit'>Delete</button>";
            echo "</form></li>";
        }
        echo "</ul>";
        echo "<hr>";
    }
}
?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Panel</title>
</head>

<body>
    <h1>Admin Panel</h1>

    <h2>Unapproved Comments</h2>
    <?php
    // Display the unapproved comments
    displayUnapprovedComments();
    $conn->close();
    ?>

</body>

</html>