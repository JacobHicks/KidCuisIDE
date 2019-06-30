<?php

include "config.php";
include "session.php";

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
            <h1>Router Admin Panel</h1>

            <a href="/status.php">System Status</a>
            <a href="/tools.php">System Tools</a>

            <hr>

            <small><pre><?php echo shell_exec('uname -a');?></pre></small>
            <pre><?php echo shell_exec('uptime -p');?></pre>
            <pre><?php echo shell_exec('whoami');?></pre>
        </div>
      </div>
    </div>
</body>
</html>
