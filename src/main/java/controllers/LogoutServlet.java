package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Author: Pablo Aguilar
 * Fecha: 11/11/2025
 * Descripción:
 * Servlet que maneja el cierre de sesión de usuarios.
 * Elimina la cookie de autenticación y muestra página de confirmación.
 */
@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {

    /**
     * Maneja solicitudes GET para el cierre de sesión.
     * Elimina la cookie de usuario y muestra página de confirmación.
     *
     * @param req Solicitud HTTP recibida
     * @param resp Respuesta HTTP a generar
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Eliminar cookie estableciendo una cookie vacía con tiempo de expiración 0
        Cookie cookie = new Cookie("username", "");
        cookie.setMaxAge(0);  // Establecer expiración inmediata
        resp.addCookie(cookie);  // Agregar cookie a la respuesta para eliminarla

        // Mostrar página de confirmación de logout
        resp.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = resp.getWriter()) {
            // Generar página HTML de confirmación con Bootstrap
            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("    <meta charset='UTF-8'>");
            out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("    <title>Sesión Cerrada</title>");
            out.println("    <link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>");
            out.println("    <link href='https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css' rel='stylesheet'>");
            out.println("    <link href='" + req.getContextPath() + "/css/styles.css' rel='stylesheet'>");
            out.println("</head>");
            out.println("<body class='bg-light'>");
            out.println("    <div class='container mt-5'>");
            out.println("        <div class='row justify-content-center'>");
            out.println("            <div class='col-md-6'>");
            out.println("                <div class='card shadow'>");
            out.println("                    <div class='card-header bg-info text-white'>");
            out.println("                        <h4 class='card-title mb-0'><i class='bi bi-check-circle'></i> Sesión Cerrada</h4>");
            out.println("                    </div>");
            out.println("                    <div class='card-body text-center'>");
            out.println("                        <div class='mb-4'>");
            out.println("                            <i class='bi bi-box-arrow-right display-1 text-info'></i>");
            out.println("                        </div>");
            out.println("                        <h5 class='card-title'>¡Hasta pronto!</h5>");
            out.println("                        <p class='card-text'>Has cerrado sesión exitosamente.</p>");
            out.println("                        <div class='d-grid gap-2'>");
            out.println("                            <a href='" + req.getContextPath() + "/login' class='btn btn-primary'>");
            out.println("                                <i class='bi bi-arrow-left-circle'></i> Volver al Login");
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