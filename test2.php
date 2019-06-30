<?php

include "config.php";

session_start();

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $username = $_POST['username'];
    $password = $_POST['password'];
    if ($username === 'admin' && $password === $DEFAULT_ADMIN_PASSWORD) {
        $_SESSION['username'] = 'admin';
        header('Location: /index.php');
        exit();
    } else {
        $error = "Invalid Credentials";
    }
}
?>

<!DOCTYPE html>
<html>
<head>
    <title>Router Admin Panel</title>
    <link rel="stylesheet" href="https://unpkg.com/spectre.css/dist/spectre.min.css">
    <link rel="stylesheet" href="https://unpkg.com/spectre.css/dist/spectre-exp.min.css">
    <link rel="stylesheet" href="https://unpkg.com/spectre.css/dist/spectre-icons.min.css">
</head>
<body>
    <div class="container">
      <div class="columns">
        <div class="column col-6 col-mx-auto">
            <form method="POST">
                <h1>Login</h1>

                <?php
                    if (isset($error)) {
                        echo "<p><strong>$error</strong></p><br>";
                    }
                ?>

                <input type="text" class="form-input" placeholder="Username" name="username">
                <br>
                <input type="text" class="form-input" placeholder="Password" name="password">
                <br>
                <hr>
                <br>
                <input type="submit" class="form-input">
            </form>
        </div>
      </div>
    </div>
</body>
</html>
