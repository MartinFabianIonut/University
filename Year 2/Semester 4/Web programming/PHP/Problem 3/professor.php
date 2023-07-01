<?php
session_start();
if (!isset($_SESSION["authenticated"]) || $_SESSION["authenticated"] !== true) {
    header("Location: index.php");
    exit;
}
$professorName = isset($_SESSION['professor_name']) ? $_SESSION['professor_name'] : '';
echo "Welcome, $professorName!";

function validateData($data)
{
    $data = trim($data);
    $data = stripslashes($data);
    $data = htmlspecialchars($data);
    $data = addslashes($data);
    $data = htmlentities($data, ENT_QUOTES, "UTF-8");
    return $data;
}

$id = "";
$subject = "";
$grade = "";

if ($_SERVER["REQUEST_METHOD"] == "POST") {
    $id = validateData($_POST["id"]);
    $subject = validateData($_POST["subject"]);
    $grade = validateData($_POST["grade"]);

    $conn = new SQLite3('../php.db');
    if (!$conn) {
        die("Connection failed to SQLite database");
    }

    $sqlCheck = "SELECT * FROM students WHERE id = ?";
    $stmtCheck = $conn->prepare($sqlCheck);
    $stmtCheck->bindParam(1, $id, SQLITE3_TEXT);
    $resultCheck = $stmtCheck->execute();
    if (!$resultCheck->fetchArray(SQLITE3_ASSOC)) {
        print "Studentul nu exista!";
    } else {
        $sqlCheck = "SELECT * FROM subjects WHERE subject = ?";
        $stmtCheck = $conn->prepare($sqlCheck);
        $stmtCheck->bindParam(1, $subject, SQLITE3_TEXT);
        $resultCheck = $stmtCheck->execute();
        if (!$resultCheck->fetchArray(SQLITE3_ASSOC)) {
            print "Materia nu exista!";
        } else {
            # check if grade is between 1 and 10 and is an integer number like 3, not 3.5
            if (!is_numeric($grade) || $grade < 1 || $grade > 10 || !ctype_digit($grade)) {
                print "Nota trebuie sa fie un numar intre 1 si 10!";
            } else {
                $sql = "INSERT INTO grades (id, subject, grade, professor) VALUES (?,?,?,?)";
                $stmt = $conn->prepare($sql);
                $stmt->bindParam(1, $id, SQLITE3_TEXT);
                $stmt->bindParam(2, $subject, SQLITE3_TEXT);
                $stmt->bindParam(3, $grade, SQLITE3_TEXT);
                $stmt->bindParam(4, $professorName, SQLITE3_TEXT);
                $result = $stmt->execute();
                if ($result) {
                    print "Nota a fost adaugata cu succes!";
                    header("Location: professor.php"); // Redirect to a new page after successful submission
                    exit(); // Stop further execution
                } else {
                    print "Nota nu a putut fi adaugata!";
                }
            }
        }
    }
    $conn->close();
}
?>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Third problem PHP</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.4.4/jquery.min.js" type="text/javascript"></script>
</head>

<body>
    <h1>Adaugare Nota</h1>

    <div style="display: flex; align-items: center;">
        <h3 style="margin-right: 10px;">Student: </h3>
        <select name="id" form="grades">
            <?php
            $conn = new SQLite3('../php.db');
            if (!$conn) {
                die("Connection failed to SQLite database");
            }
            $sql = "SELECT * FROM students";
            $stmt = $conn->prepare($sql);
            $result = $stmt->execute();
            print "<option value='' selected disabled hidden>Selectează un student</option>";
            while ($row = $result->fetchArray(SQLITE3_ASSOC)) {
                print "<option  value='" . $row["id"] . "'>" . $row["first_name"] . ' ' . $row["last_name"] . "</option>";
            }
            ?>
        </select>
    </div>

    <div style="display: flex; align-items: center;">
        <h3 style="margin-right: 10px;">Materie:</h3>
        <select name='subject' form="grades">
            <?php
            $sql = "SELECT DISTINCT subject FROM subjects";
            $stmt = $conn->prepare($sql);
            $result = $stmt->execute();
            print "<option value='' selected disabled hidden>Selectează o materie</option>";
            while ($row = $result->fetchArray(SQLITE3_ASSOC)) {
                print "<option value='" . $row["subject"] . "'>" . $row["subject"] . "</option>";
            }
            ?>
        </select>
    </div>

    <div style="display: flex; align-items: center;">
        <h3 style="margin-right: 10px;">Notă [1-10]:</h3>
        <select name="grade" form="grades">
            <option value="" selected disabled hidden>Selectează o notă</option>
            <option value="1">1</option>
            <option value="2">2</option>
            <option value="3">2</option>
            <option value="4">4</option>
            <option value="5">5</option>
            <option value="6">6</option>
            <option value="7">7</option>
            <option value="8">8</option>
            <option value="9">9</option>
            <option value="10">10</option>
        </select>
    </div>

    <form id="grades" action="<?php echo htmlspecialchars($_SERVER['PHP_SELF']); ?>" method="POST" enctype="multipart/form-data">
        <input type="submit" value="Notează">
    </form>

</body>

</html>