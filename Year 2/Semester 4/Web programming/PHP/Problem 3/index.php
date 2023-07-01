<?php
session_start();
session_destroy(); // destroy session for each leave or refresh
session_start();

if (isset($_SESSION["authenticated"]) && $_SESSION["authenticated"] === true) {
	header("Location: professor.php");
	exit;
}

function validateData($data)
{
	$data = trim($data);
	$data = stripslashes($data);
	$data = htmlspecialchars($data);
	$data = addslashes($data);
	$data = htmlentities($data, ENT_QUOTES, "UTF-8");
	return $data;
}

if ($_SERVER["REQUEST_METHOD"] == "POST") {
	$username = validateData($_POST["username"]);
	$password = validateData($_POST["password"]);

	$conn = new SQLite3('../php.db');

	if (!$conn) {
		die("Connection failed to SQLite database");
	}

	$sql = "SELECT * FROM professors WHERE username = ? AND password = ?";
	$stmt = $conn->prepare($sql);
	$stmt->bindValue(1, $username, SQLITE3_TEXT);
	$stmt->bindValue(2, $password, SQLITE3_TEXT);
	$result = $stmt->execute()->fetchArray();

	if ($result) {
		$_SESSION["authenticated"] = true;
		$_SESSION['professor_name'] = $result['name'];
		header("Location: professor.php");
		exit;
	}
	$error = "Nume de utilizator sau parolă incorectă.";
	$conn->close();
}

$conn = new SQLite3('../php.db');
if (!$conn) {
	die("Connection failed to SQLite database");
}
$results = $conn->query("SELECT * FROM grades ORDER BY id ASC");

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
	<h1>Autentificare Profesor</h1>

	<?php
	// Afișăm mesajul de eroare (dacă există)
	if (isset($error)) {
		echo "<p>" . htmlspecialchars($error) . "</p>";
	}
	?>
	<!-- Formularul de autentificare (cu metoda POST) 
	htmlspecialchars($_SERVER["PHP_SELF"]) este o metodă de securitate pentru a preveni atacurile de tip XSS (Cross Site Scripting) -->
	<form method="POST" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]); ?>">
		<div style="display: inline-block; text-align: right;">
			<label for="username">Nume de utilizator:</label>
			<input type="text" id="username" name="username" required><br>

			<label for="password">Parolă:</label>
			<input type="password" id="password" name="password" required><br>
		</div>
		<input type="submit" value="Autentificare">
	</form>

	<h2>Note Studenți:</h2>

	<table>
		<tr>
			<th>Student ID</th>
			<th>Materie</th>
			<th>Notă</th>
		</tr>
		<?php while ($row = $results->fetchArray()) { ?>
			<tr>
				<td><?php echo htmlspecialchars($row['id']); ?></td>
				<td><?php echo htmlspecialchars($row['subject']); ?></td>
				<td><?php echo htmlspecialchars($row['grade']); ?></td>
			</tr>
		<?php }
		$conn->close();
		?>
	</table>
</body>

</html>