package services;

import models.Producto;
import java.util.Arrays;
import java.util.List;

public class ProductoServiceImplement implements ProductoService {
    @Override
    public List<Producto> listaProductos() {
        return Arrays.asList(
                new Producto(1L, "Laptop", "Computacion", 250.00),
                new Producto(2L, "Refrigeradora", "Cocina", 745.13),
                new Producto(3L, "Cama", "Dormitorio", 350.12));
    }
}
