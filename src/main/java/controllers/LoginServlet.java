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

@WebServlet({"/login", "/login.html"})
public class LoginServlet extends HttpServlet {
    final static String USERNAME = "admin";
    final static String PASSWORD = "12345";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie[] cookies = req.getCookies() != null ? req.getCookies() : new Cookie[0];
        Optional<String> cookieOptional = Arrays.stream(cookies)
                .filter(c -> "username".equals(c.getName()))
                .map(Cookie::getValue)
                .findAny();

        if (cookieOptional.isPresent()) {
            // Redirigir a productos si ya está logueado
            resp.sendRedirect(req.getContextPath() + "/productos.html");
        } else {
            getServletContext().getRequestDispatcher("/Login.jsp").forward(req,resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("user");
        String password = req.getParameter("password");

        if (USERNAME.equals(username) && PASSWORD.equals(password)) {
            // Crear cookie de sesión
            Cookie cookie = new Cookie("username", username);
            cookie.setMaxAge(3600); // 1 hora
            resp.addCookie(cookie);

            // Redirigir a la página de productos
            resp.sendRedirect(req.getContextPath() + "/productos.html");
        } else {
            resp.setContentType("text/html;charset=UTF-8");
            try (PrintWriter out = resp.getWriter()) {
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