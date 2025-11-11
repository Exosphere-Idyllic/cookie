package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Optional;

/**
 * Author: Pablo Aguilar
 * Fecha: 11/11/2025
 * Descripción:
 * Servlet que maneja el proceso de autenticación de usuarios.
 * Gestiona el login mediante cookies y redirecciona según el estado de sesión.
 */
@WebServlet({"/login", "/login.html"})
public class LoginServlet extends HttpServlet {
    // Credenciales predefinidas para autenticación
    final static String USERNAME = "admin";
    final static String PASSWORD = "12345";

    /**
     * Maneja solicitudes GET para el login.
     * Verifica si el usuario ya está autenticado mediante cookies.
     * Si ya está logueado, redirige a productos; si no, muestra el formulario de login.
     *
     * @param req Solicitud HTTP recibida
     * @param resp Respuesta HTTP a generar
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Obtener cookies de la solicitud, o array vacío si no hay cookies
        Cookie[] cookies = req.getCookies() != null ? req.getCookies() : new Cookie[0];

        // Buscar la cookie de username usando Streams y Optional
        Optional<String> cookieOptional = Arrays.stream(cookies)
                .filter(c -> "username".equals(c.getName()))  // Filtra por nombre "username"
                .map(Cookie::getValue)                        // Obtiene el valor de la cookie
                .findAny();                                   // Retorna cualquier coincidencia

        // Si existe la cookie de usuario, redirigir a productos (ya está logueado)
        if (cookieOptional.isPresent()) {
            resp.sendRedirect(req.getContextPath() + "/productos.html");
        } else {
            // Si no está logueado, mostrar el formulario de login
            getServletContext().getRequestDispatcher("/Login.jsp").forward(req,resp);
        }
    }

    /**
     * Maneja solicitudes POST para el proceso de login.
     * Válida las credenciales y crea cookie de sesión si son correctas.
     *
     * @param req Solicitud HTTP con parámetros de login
     * @param resp Respuesta HTTP con resultado de autenticación
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Obtener parámetros del formulario de login
        String username = req.getParameter("user");
        String password = req.getParameter("password");

        // Validar credenciales contra las constantes definidas
        if (USERNAME.equals(username) && PASSWORD.equals(password)) {
            // Crear cookie de sesión con nombre de usuario
            Cookie cookie = new Cookie("username", username);
            cookie.setMaxAge(3600); // Establecer expiración en 1 hora (3600 segundos)
            resp.addCookie(cookie);  // Agregar cookie a la respuesta

            // Redirigir a la página de productos después del login exitoso
            resp.sendRedirect(req.getContextPath() + "/productos.html");
        } else {
            // Si las credenciales son incorrectas, mostrar página de error
            resp.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = resp.getWriter()) {
                // Generar página HTML de error con Bootstrap
                out.println("<!DOCTYPE html>");
                out.println("<html lang='es'>");
                out.println("<head>");
                out.println("    <meta charset='UTF-8'>");
                out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
                out.println("    <title>Error de Login</title>");
                out.println("    <link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>");
                out.println("    <link href='" + req.getContextPath() + "/css/styles.css' rel='stylesheet'>");
                out.println("</head>");
                out.println("<body class='bg-light'>");
                out.println("    <div class='container mt-5'>");
                out.println("        <div class='row justify-content-center'>");
                out.println("            <div class='col-md-6'>");
                out.println("                <div class='card shadow'>");
                out.println("                    <div class='card-header bg-danger text-white'>");
                out.println("                        <h4 class='card-title mb-0'><i class='bi bi-exclamation-triangle'></i> Error de Autenticación</h4>");
                out.println("                    </div>");
                out.println("                    <div class='card-body'>");
                out.println("                        <div class='alert alert-danger'>");
                out.println("                            <p class='mb-3'><strong>Credenciales incorrectas</strong></p>");
                out.println("                            <p class='mb-0'>Las credenciales proporcionadas no son válidas. Por favor, inténtelo nuevamente.</p>");
                out.println("                        </div>");
                out.println("                        <div class='d-grid'>");
                out.println("                            <a href='" + req.getContextPath() + "/login' class='btn btn-primary'>");
                out.println("                                <i class='bi bi-arrow-left-circle'></i> Intentar nuevamente");
                out.println("                            </a>");
                out.println("                        </div>");
                out.println("                    </div>");
                out.println("                </div>");
                out.println("            </div>");
                out.println("        </div>");
                out.println("    </div>");
                out.println("    <script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js'></script>");
                out.println("</body>");
                out.println("</html>");
            }
        }
    }
}