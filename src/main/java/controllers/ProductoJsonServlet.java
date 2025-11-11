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

@WebServlet("/productos.json")
public class ProductoJsonServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        ProductoService service = new ProductoServiceImplement();
        List<Producto> productos = service.listaProductos();
        boolean logueado = estaLogueado(req);

        resp.setContentType("application/json;charset=UTF-8");

        if (logueado) {
            resp.setHeader("Content-Disposition", "attachment; filename=productos_completos.json");
        } else {
            resp.setHeader("Content-Disposition", "attachment; filename=productos_sin_precios.json");
        }

        // Crear lista de productos para JSON (con o sin precios)
        List<Map<String, Object>> productosJson = new ArrayList<>();
        for (Producto p : productos) {
            Map<String, Object> productoMap = new HashMap<>();
            productoMap.put("idProducto", p.getIdProducto());
            productoMap.put("nombre", p.getNombre());
            productoMap.put("categoria", p.getCategoria());
            if (logueado) {
                productoMap.put("precio", p.getPrecio());
            } else {
                productoMap.put("precio", "Inicia sesión para ver el precio");
                productoMap.put("acceso", "limitado");
            }
            productosJson.add(productoMap);
        }

        // Agregar metadatos
        Map<String, Object> respuestaCompleta = new HashMap<>();
        respuestaCompleta.put("productos", productosJson);
        respuestaCompleta.put("totalProductos", productos.size());
        respuestaCompleta.put("accesoCompleto", logueado);
        respuestaCompleta.put("mensaje", logueado ?
                "Datos completos - Sesión activa" :
                "Datos limitados - Inicia sesión para ver precios");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(respuestaCompleta);

        try (PrintWriter out = resp.getWriter()) {
            out.print(json);
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