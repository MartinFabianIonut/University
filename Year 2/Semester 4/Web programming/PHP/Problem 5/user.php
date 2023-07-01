<?php
session_start();

// Check if the user is authenticated
if (!isset($_SESSION["user_name"])) {
    // Redirect to the login page or display an error message
    header("Location: index.php");
    exit;
}

// Get the user's name from the session
$userName = $_SESSION["user_name"];
$targetDirectory = "uploads/"; // Specify the directory where you want to store the uploaded photos

// Handle file upload
if ($_SERVER["REQUEST_METHOD"] == "POST" && isset($_FILES["photo"])) {
    // Generate a unique filename for the uploaded photo
    $fileName = $userName . "_" . basename($_FILES["photo"]["name"]);

    $targetFilePath = $targetDirectory . $fileName;
    $uploadOk = true;
    $imageFileType = strtolower(pathinfo($targetFilePath, PATHINFO_EXTENSION));

    // Check if the uploaded file is an image
    $check = getimagesize($_FILES["photo"]["tmp_name"]);
    if ($check === false) {
        echo "Error: The uploaded file is not an image.";
        $uploadOk = false;
    }

    // Check if the file already exists
    if (file_exists($targetFilePath)) {
        echo "Error: A file with the same name already exists.";
        $uploadOk = false;
    }

    // Check file size (you can adjust the size limit as needed)
    $maxFileSize = 5 * 1024 * 1024; // 5MB
    if ($_FILES["photo"]["size"] > $maxFileSize) {
        echo "Error: The uploaded file is too large. It must be less than 5MB";
        $uploadOk = false;
    }

    // Allow only specific file formats (you can adjust the allowed formats as needed)
    $allowedFormats = array("jpg", "jpeg", "png");
    if (!in_array($imageFileType, $allowedFormats)) {
        echo "Error: Only JPG, JPEG, and PNG files are allowed.";
        $uploadOk = false;
    }

    // If all checks pass, move the uploaded file to the target directory
    if ($uploadOk) {
        if (move_uploaded_file($_FILES["photo"]["tmp_name"], $targetFilePath)) {
            header("Location: user.php"); // Redirect to a new page after successful submission
            exit(); // Stop the rest of the script from running after the redirect
        } else {
            echo "Error: There was an error uploading the file.";
        }
    }
}

// Delete photo
if ($_SERVER["REQUEST_METHOD"] === "POST" && isset($_POST["delete"])) {
    // Get the filename to be deleted
    $fileName = $_POST["delete"];

    // Validate the filename to prevent unauthorized access
    $filePath = $targetDirectory . $fileName;

    // Check if the file exists and delete it
    if (file_exists($filePath)) {
        $fileOwner = substr($fileName, 0, strpos($fileName, "_")); // Extract the username from the filename

        if ($fileOwner === $userName) {
            unlink($filePath);
            header("Location: user.php"); // Redirect to a new page after successful deletion
            exit(); // Stop the rest of the script from running after the redirect
        } else {
            echo "You do not have permission to delete this photo.";
        }
    } else {
        echo "File '$fileName' does not exist.";
    }
}

// Retrieve and display the user's uploaded photos
function displayUserPhotos($userName)
{
    $targetDirectory = "uploads/";

    // Get the list of files in the target directory
    $files = glob($targetDirectory . "*_*");

    if (empty($files)) {
        echo "No photos found.";
    } else {
        foreach ($files as $file) {
            echo "<div style='display: inline-block; margin-right: 40px;'>"; // Container to display photos side by side
            echo "<img src='" . htmlspecialchars($file) . "' alt='Uploaded Photo' style='width: 370px; height: auto;'><br>";

            $fileOwner = substr(basename($file), 0, strpos(basename($file), "_")); // Extract the username from the filename

            if ($fileOwner === $userName) {
                echo "<form method='POST' action='" . htmlspecialchars($_SERVER["PHP_SELF"]) . "'>";
                echo "<input type='hidden' name='delete' value='" . htmlspecialchars(basename($file)) . "'>";
                echo "<input type='submit' value='Delete'>";
                echo "</form>";
            } else {
                echo "Uploaded by: $fileOwner";
            }

            echo "</div>";
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
    <title>User Profile</title>
</head>

<body>
    <h1>Welcome, <?php echo $userName; ?></h1>

    <!-- Upload form -->
    <form method="POST" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>" enctype="multipart/form-data">
        <label for="photo">Upload a photo:</label>
        <input type="file" id="photo" name="photo" accept="image/*" required>
        <button type="submit">Upload</button>
    </form>

    <!-- Display user's uploaded photos -->
    <h2>Your Photos:</h2>
    <?php
    displayUserPhotos($userName);
    ?>

</body>

</html>