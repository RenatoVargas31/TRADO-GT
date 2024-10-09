package com.app.tradogt.helpers;

import com.app.tradogt.entity.Producto;
import jakarta.validation.constraints.NotNull;

public class ProductCodeGenerator {
    public static String generateProductCode(@NotNull Producto producto) {
        String code = producto.getNombre().substring(0, 3).toUpperCase();
        code += producto.getSubcategoriaIdsubcategoria().getNombre().substring(0, 3).toUpperCase();
        code += producto.getSubcategoriaIdsubcategoria().getCategoriaIdcategoria().getNombre().substring(0, 3).toUpperCase();
        code += producto.getId();
        return code;
    }
}
