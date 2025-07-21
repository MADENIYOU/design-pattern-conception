<?php
// login.php

$valid_login = "admin";
$valid_password = "pass123";

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $login = isset($_POST['login']) ? trim($_POST['login']) : '';
    $password = isset($_POST['password']) ? trim($_POST['password']) : '';

    if ($login === $valid_login && $password === $valid_password) {
        echo "Connexion réussie";
    } else {
        echo "Échec de la connexion";
    }
} else {
    echo "Méthode non autorisée.";
}
?>