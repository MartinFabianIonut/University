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
// Get the article ID from the query parameter
$articleId = isset($_GET["id"]) ? $_GET["id"] : "";

// Retrieve and display the article and its approved comments
function displayArticleAndComments()
{
    global $conn;
    global $articleId;
    // Retrieve the article details
    $stmt = $conn->prepare("SELECT * FROM articles WHERE id = ?");
    $stmt->bindParam(1, $articleId, SQLITE3_INTEGER);
    $result = $stmt->execute();
    $article = $result->fetchArray(SQLITE3_ASSOC);

    if (!$article) {
        echo "<p>Invalid article ID</p>";
        return;
    }

    echo "<h2>" . htmlspecialchars($article["title"]) . "</h2>";
    echo "<p>" . htmlspecialchars($article["content"]) . "</p>";

    // Retrieve and display the approved comments for the article
    $stmt = $conn->prepare("SELECT name, comment FROM comments WHERE approved = 1 AND article_id = ?");
    $stmt->bindParam(1, $articleId, SQLITE3_INTEGER);
    $result = $stmt->execute();

    echo "<h3>Approved Comments</h3>";
    echo "<ul>";
    while ($row = $result->fetchArray(SQLITE3_ASSOC)) {
        echo "<li><strong>" . htmlspecialchars($row["name"]) . ":</strong> " . htmlspecialchars($row["comment"]) . "</li>";
    }
    echo "</ul>";
}

// Handle comment submission
if ($_SERVER["REQUEST_METHOD"] == "POST" && isset($_POST["comment"])) {
    $comment = $_POST["comment"];
    $name = $_POST["name"];
    $articleIdSend = $_POST["article"];
    $articleId = isset($_GET["id"]) ? $_GET["id"] : "";

    // Insert the comment into the database if name exists in users table
    $sql = "SELECT * FROM users WHERE name = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bindParam(1, $name, SQLITE3_TEXT);
    $result = $stmt->execute();
    if (!$result->fetchArray(SQLITE3_ASSOC)) {
        echo "<p>Invalid name</p>";
    } else {
        // check if articleIdSend matches the articleId in the query parameter
        if ($articleIdSend != $articleId) {
            echo "<p>Invalid article</p>";
        } else {
            $sql = "INSERT INTO comments (name, comment, approved, article_id) VALUES (?, ?, ?, ?)";
            $stmt = $conn->prepare($sql);
            $stmt->bindParam(1, $name, SQLITE3_TEXT);
            $stmt->bindParam(2, $comment, SQLITE3_TEXT);
            $approved = 0; // All comments are initially set to unapproved
            $stmt->bindParam(3, $approved, SQLITE3_INTEGER);
            $stmt->bindParam(4, $articleId, SQLITE3_INTEGER);
            $stmt->execute();

            // Redirect back to the article page with the article ID
            header("Location: individual_article.php?id=" . urlencode($articleId));
            exit();
        }
    }
}


?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Individual Article</title>
</head>

<body>
    <h1>Welcome, <?php echo $userName; ?></h1>

    <?php
    // Display the article and its approved comments
    displayArticleAndComments();

    ?>

    <h3>Add a Comment</h3>
    <form method="POST" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"] . "?id=" . urlencode($articleId)); ?>">
        <input type='hidden' id="article" name="article" value="<?php echo htmlspecialchars($articleId); ?>" required>
        <label for="name">Your Name:</label>
        <input type="text" id="name" name="name" value="<?php echo htmlspecialchars($userName); ?>" required>
        <br>
        <label for="comment">Your Comment:</label>
        <textarea id="comment" name="comment" rows="4" cols="47" required></textarea>
        <br>
        <button type="submit">Submit</button>
    </form>

</body>

</html>