package controllers;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Producto;
import services.ProductoService;
import services.ProductoServiceImplement;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Pablo Aguilar
 * Fecha: 11/11/2025
 * Descripción:
 * Servlet que genera una respuesta JSON con información de productos.
 * Proporciona diferentes niveles de acceso según el estado de autenticación del usuario.
 */
@WebServlet("/productos.json")
public class ProductoJsonServlet extends HttpServlet {

    /**
     * Maneja solicitudes GET para generar JSON de productos.
     * Los usuarios autenticados ven precios completos, los no autenticados ven información limitada.
     *
     * @param req Solicitud HTTP recibida
     * @param resp Respuesta HTTP con JSON generado
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Obtener lista de productos desde el servicio
        ProductoService service = new ProductoServiceImplement();
        List<Producto> productos = service.listaProductos();

        // Verificar si el usuario está autenticado
        boolean logueado = estaLogueado(req);

        // Configurar respuesta como JSON
        resp.setContentType("application/json;charset=UTF-8");

        // Configurar header para descarga con nombre de archivo específico según autenticación
        if (logueado) {
            resp.setHeader("Content-Disposition", "attachment; filename=productos_completos.json");
        } else {
            resp.setHeader("Content-Disposition", "attachment; filename=productos_sin_precios.json");
        }

        // Crear lista de productos para JSON (con o sin precios según autenticación)
        List<Map<String, Object>> productosJson = new ArrayList<>();
        for (Producto p : productos) {
            Map<String, Object> productoMap = new HashMap<>();
            productoMap.put("idProducto", p.getIdProducto());
            productoMap.put("nombre", p.getNombre());
            productoMap.put("categoria", p.getCategoria());
            if (logueado) {
                // Usuario logueado: mostrar precio real
                productoMap.put("precio", p.getPrecio());
            } else {
                // Usuario no logueado: ocultar precio y mostrar mensaje
                productoMap.put("precio", "Inicia sesión para ver el precio");
                productoMap.put("acceso", "limitado");
            }
            productosJson.add(productoMap);
        }

        // Agregar metadatos a la respuesta JSON
        Map<String, Object> respuestaCompleta = new HashMap<>();
        respuestaCompleta.put("productos", productosJson);
        respuestaCompleta.put("totalProductos", productos.size());
        respuestaCompleta.put("accesoCompleto", logueado);
        respuestaCompleta.put("mensaje", logueado ?
                "Datos completos - Sesión activa" :
                "Datos limitados - Inicia sesión para ver precios");

        // Convertir mapa a JSON con formato legible
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(respuestaCompleta);

        // Escribir JSON en la respuesta
        try (PrintWriter out = resp.getWriter()) {
            out.print(json);
        }
    }

    /**
     * Verifica si el usuario está autenticado revisando las cookies.
     *
     * @param req Solicitud HTTP para revisar cookies
     * @return true si el usuario está autenticado, false en caso contrario
     */
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