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

// Handle comment submission
if ($_SERVER["REQUEST_METHOD"] == "POST" && isset($_POST["comment"])) {
    $comment = $_POST["comment"];
    $name = $_POST["name"];
    $articleId = $_POST["article"];

    // Insert the comment into the database if name exists in users table
    $sql = "SELECT * FROM users WHERE name = ?";
    $stmt = $conn->prepare($sql);
    $stmt->bindParam(1, $name, SQLITE3_TEXT);
    $result = $stmt->execute();
    if (!$result->fetchArray(SQLITE3_ASSOC)) {
        echo "<p>Invalid name</p>";
    } else {
        // check if articleId matches an article in the articles table and if the title of the article is the same as the selected article
        $sql = "SELECT * FROM articles WHERE id = ?";
        $stmt = $conn->prepare($sql);
        $stmt->bindParam(1, $articleId, SQLITE3_INTEGER);
        $result = $stmt->execute();
        if (!$result->fetchArray(SQLITE3_ASSOC)) {
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

            // Redirect back to the article page
            header("Location: article.php");
            exit();
        }
    }
}

// Retrieve and display the approved comments
function displayArticlesAndComments()
{
    global $conn;

    // Display the article
    $sql = "SELECT * FROM articles";
    $stmt = $conn->prepare($sql);
    $result = $stmt->execute();

    while ($row = $result->fetchArray(SQLITE3_ASSOC)) {
        echo "<h2>" . htmlspecialchars($row["title"]) . "</h2>";

        echo "<p>" . htmlspecialchars($row["content"]) . "</p>";

        $sql2 = "SELECT name, comment FROM comments WHERE approved = 1 AND article_id = ?";
        $stmt2 = $conn->prepare($sql2);
        $articleId = $row["id"];
        $stmt2->bindParam(1, $articleId, SQLITE3_INTEGER);
        $result2 = $stmt2->execute();

        echo "<h3>Comments</h3>";
        echo "<ul>";
        while ($row2 = $result2->fetchArray(SQLITE3_ASSOC)) {
            echo "<li><strong>" . htmlspecialchars($row2["name"]) . ":</strong> " . htmlspecialchars($row2["comment"]) . "</li>";
        }
        echo "</ul>";
        echo "<hr>";
    }
}

$userName = isset($_SESSION["user_name"]) ? $_SESSION["user_name"] : "";
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
    // Display the approved comments
    displayArticlesAndComments();
    ?>

    <h3>Add a Comment (Visitor)</h3>
    <form method="POST" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>">
        <label for="article">Article ID:</label>
        <select id="article" name="article" required>
            <?php
            // Retrieve and display all articles
            $stmt = $conn->prepare("SELECT id, title FROM articles ORDER BY id");
            $result = $stmt->execute();

            while ($row = $result->fetchArray(SQLITE3_ASSOC)) {
                $articleId = $row["id"];
                $articleTitle = $row["title"];
                echo "<option value=\"$articleId\">$articleTitle</option>";
            }
            $conn->close();
            ?>
        </select>
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