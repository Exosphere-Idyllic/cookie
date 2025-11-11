package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Producto;
import services.ProductoService;
import services.ProductoServiceImplement;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet({"/productos.html", "/productos.xls"})
public class ProductoServletXls extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        ProductoService service = new ProductoServiceImplement();
        List<Producto> productos = service.listaProductos();
        boolean logueado = estaLogueado(req);

        String servletPath = req.getServletPath();
        boolean esXls = servletPath.endsWith(".xls");

        if (esXls) {
            resp.setContentType("application/vnd.ms-excel");
            resp.setHeader("Content-Disposition", "attachment; filename=productos.xls");
        } else {
            resp.setContentType("text/html;charset=UTF-8");
        }

        try (PrintWriter out = resp.getWriter()) {

            if (!esXls) {
                out.println("<!DOCTYPE html>");
                out.println("<html lang='es'>");
                out.println("<head>");
                out.println("    <meta charset='UTF-8'>");
                out.println("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>");
                out.println("    <title>Sistema de Productos</title>");
                out.println("    <link href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css' rel='stylesheet'>");
                out.println("    <link href='https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css' rel='stylesheet'>");
                out.println("    <link href='" + req.getContextPath() + "/css/styles.css' rel='stylesheet'>");
                out.println("</head>");
                out.println("<body class='d-flex flex-column min-vh-100'>");

                // Navbar
                out.println("    <nav class='navbar navbar-expand-lg navbar-dark " + (logueado ? "bg-primary" : "bg-warning") + "'>");
                out.println("        <div class='container'>");
                out.println("            <a class='navbar-brand' href='#'>");
                out.println("                <i class='bi bi-box-seam'></i> Sistema de Productos");
                out.println("            </a>");
                out.println("            <div class='navbar-nav ms-auto'>");
                if (logueado) {
                    out.println("                <span class='navbar-text text-light me-3'>");
                    out.println("                    <i class='bi bi-person-circle'></i> Sesión activa");
                    out.println("                </span>");
                    out.println("                <a class='nav-link text-white' href='" + req.getContextPath() + "/logout'>");
                    out.println("                    <i class='bi bi-box-arrow-right'></i> Cerrar Sesión");
                    out.println("                </a>");
                } else {
                    out.println("                <a class='nav-link text-white' href='" + req.getContextPath() + "/login'>");
                    out.println("                    <i class='bi bi-box-arrow-in-right'></i> Iniciar Sesión");
                    out.println("                </a>");
                }
                out.println("            </div>");
                out.println("        </div>");
                out.println("    </nav>");

                out.println("    <div class='container-fluid py-4 flex-grow-1'>");
                out.println("        <div class='container'>");

                // Header con estado de sesión
                out.println("            <div class='row mb-4'>");
                out.println("                <div class='col'>");
                out.println("                    <h1 class='display-5 text-center " + (logueado ? "text-primary" : "text-warning") + "'>");
                out.println("                        <i class='bi bi-list-ul'></i> Listado de Productos");
                out.println("                    </h1>");
                if (logueado) {
                    out.println("                    <p class='lead text-center text-muted'>Acceso completo - Precios visibles</p>");
                    out.println("                    <div class='alert alert-success text-center'>");
                    out.println("                        <i class='bi bi-check-circle'></i> Sesión activa - Precios habilitados");
                    out.println("                    </div>");
                } else {
                    out.println("                    <p class='lead text-center text-muted'>Vista limitada - Inicia sesión para ver precios</p>");
                    out.println("                    <div class='alert alert-warning text-center'>");
                    out.println("                        <i class='bi bi-exclamation-triangle'></i> Inicia sesión para ver los precios completos");
                    out.println("                    </div>");
                }
                out.println("                </div>");
                out.println("            </div>");

                // Botones de exportación (solo para usuarios logueados)
                if (logueado) {
                    out.println("            <div class='row mb-4'>");
                    out.println("                <div class='col'>");
                    out.println("                    <div class='d-flex gap-2 justify-content-center flex-wrap'>");
                    out.println("                        <a href='" + req.getContextPath() + "/productos.xls' class='btn btn-success'>");
                    out.println("                            <i class='bi bi-file-earmark-excel'></i> Exportar a Excel");
                    out.println("                        </a>");
                    out.println("                        <a href='" + req.getContextPath() + "/productos.json' class='btn btn-warning'>");
                    out.println("                            <i class='bi bi-file-earmark-code'></i> Exportar a JSON");
                    out.println("                        </a>");
                    out.println("                    </div>");
                    out.println("                </div>");
                    out.println("            </div>");
                }

                // Estadísticas (solo para usuarios logueados)
                if (logueado) {
                    out.println("            <div class='row mb-4'>");
                    out.println("                <div class='col-md-4 mb-3'>");
                    out.println("                    <div class='card stat-card bg-primary text-white'>");
                    out.println("                        <div class='card-body text-center'>");
                    out.println("                            <h3 class='card-title'>" + productos.size() + "</h3>");
                    out.println("                            <p class='card-text'>Total Productos</p>");
                    out.println("                        </div>");
                    out.println("                    </div>");
                    out.println("                </div>");
                    out.println("                <div class='col-md-4 mb-3'>");
                    out.println("                    <div class='card stat-card bg-success text-white'>");
                    out.println("                        <div class='card-body text-center'>");
                    out.println("                            <h3 class='card-title'>$" + String.format("%.2f", productos.stream().mapToDouble(Producto::getPrecio).average().orElse(0)) + "</h3>");
                    out.println("                            <p class='card-text'>Precio Promedio</p>");
                    out.println("                        </div>");
                    out.println("                    </div>");
                    out.println("                </div>");
                    out.println("                <div class='col-md-4 mb-3'>");
                    out.println("                    <div class='card stat-card bg-info text-white'>");
                    out.println("                        <div class='card-body text-center'>");
                    out.println("                            <h3 class='card-title'>" + productos.stream().map(Producto::getCategoria).distinct().count() + "</h3>");
                    out.println("                            <p class='card-text'>Categorías</p>");
                    out.println("                        </div>");
                    out.println("                    </div>");
                    out.println("                </div>");
                    out.println("            </div>");
                }

                // Tabla de productos
                out.println("            <div class='row'>");
                out.println("                <div class='col'>");
                out.println("                    <div class='card shadow'>");
                out.println("                        <div class='card-header " + (logueado ? "bg-light" : "bg-warning") + "'>");
                out.println("                            <h5 class='card-title mb-0'><i class='bi bi-table'></i> Detalle de Productos</h5>");
                out.println("                        </div>");
                out.println("                        <div class='card-body'>");
                out.println("                            <div class='table-responsive'>");
            }

            // Tabla de productos (común para HTML y Excel)
            out.println("<table class='table table-striped table-hover'>");
            out.println("    <thead class='table-dark'>");
            out.println("        <tr>");
            out.println("            <th>ID</th>");
            out.println("            <th>Nombre</th>");
            out.println("            <th>Categoría</th>");
            if (logueado || esXls) {
                out.println("            <th>Precio</th>");
            }
            out.println("        </tr>");
            out.println("    </thead>");
            out.println("    <tbody>");

            for (Producto p : productos) {
                out.println("        <tr>");
                out.println("            <td><strong>" + p.getIdProducto() + "</strong></td>");
                out.println("            <td>" + p.getNombre() + "</td>");
                out.println("            <td><span class='badge bg-secondary'>" + p.getCategoria() + "</span></td>");
                if (logueado || esXls) {
                    out.println("            <td><span class='price-badge'>$" + String.format("%.2f", p.getPrecio()) + "</span></td>");
                }
                out.println("        </tr>");
            }

            out.println("    </tbody>");
            out.println("</table>");

            if (!esXls) {
                out.println("                            </div>");
                // Mensaje adicional para usuarios no logueados
                if (!logueado) {
                    out.println("                            <div class='alert alert-info mt-3'>");
                    out.println("                                <h6><i class='bi bi-info-circle'></i> Información</h6>");
                    out.println("                                <p class='mb-2'>Actualmente estás viendo una vista limitada de los productos.</p>");
                    out.println("                                <p class='mb-0'>Para acceder a los precios completos y funciones de exportación, ");
                    out.println("                                <a href='" + req.getContextPath() + "/login' class='alert-link'>inicia sesión</a>.</p>");
                    out.println("                            </div>");
                }
                out.println("                        </div>");
                out.println("                    </div>");
                out.println("                </div>");
                out.println("            </div>");
                out.println("        </div>");
                out.println("    </div>");

                // Footer
                out.println("    <footer class='bg-dark text-white mt-auto'>");
                out.println("        <div class='container py-3'>");
                out.println("            <div class='row'>");
                out.println("                <div class='col text-center'>");
                out.println("                    <p class='mb-0'>&copy; 2024 Sistema de Productos. " +
                        (logueado ? "Sesión activa" : "Modo visita") + "</p>");
                out.println("                </div>");
                out.println("            </div>");
                out.println("        </div>");
                out.println("    </footer>");
                out.println("    <script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js'></script>");
                out.println("</body>");
                out.println("</html>");
            }
        }
    }

    private boolean estaLogueado(HttpServletRequest req) {
        if (req.getCookies() != null) {
            for (jakarta.servlet.http.Cookie cookie : req.getCookies()) {
                if ("username".equals(cookie.getName())) {
                    return true;
                }
            }
        }
        return false;
    }
}