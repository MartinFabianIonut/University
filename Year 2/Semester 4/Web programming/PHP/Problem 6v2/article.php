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

$userName = isset($_SESSION["user_name"]) ? $_SESSION["user_name"] : "";

// Retrieve and display all articles
function displayArticles()
{
    global $conn;

    $stmt = $conn->prepare("SELECT id, title FROM articles ORDER BY id");
    $result = $stmt->execute();

    echo "<h2>Articles</h2>";
    echo "<ul>";
    while ($row = $result->fetchArray(SQLITE3_ASSOC)) {
        $articleId = $row["id"];
        $articleTitle = $row["title"];
        echo "<li><a href=\"individual_article.php?id=$articleId\">$articleTitle</a></li>";
    }
    echo "</ul>";
}

?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Articles</title>
</head>

<body>
    <h1>Welcome, <?php echo $userName; ?></h1>

    <?php
    // Display all articles
    displayArticles();
    $conn->close();
    ?>

</body>

</html>