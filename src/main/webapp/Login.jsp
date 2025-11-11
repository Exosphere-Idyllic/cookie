<%--
  Created by IntelliJ IDEA.
  User: Pablo Aguilar
  Date: 10/11/2025
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Login Usuario</title>
    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- CSS personalizado -->
    <link rel="stylesheet" href="css/Styles.css">
</head>
<body>

<div class="container d-flex justify-content-center align-items-center vh-100">
    <div class="card p-5 shadow-lg rounded-4 bg-white text-center box-login">
        <h1 class="mb-4 text-primary">Inicio de Sesión</h1>

        <form action="login" method="post" class="text-start">
            <div class="mb-3">
                <label for="user" class="form-label fw-semibold">Usuario</label>
                <input type="text" id="user" name="user" class="form-control rounded-pill" required>
            </div>

            <div class="mb-4">
                <label for="password" class="form-label fw-semibold">Contraseña</label>
                <input type="password" id="password" name="password" class="form-control rounded-pill" required>
            </div>

            <div class="d-grid">
                <input type="submit" value="Iniciar Sesión" class="btn btn-primary btn-lg rounded-pill">
            </div>
        </form>
    </div>
</div>

</body>
</html>
