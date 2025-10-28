package com.example.almacercaapp.model

import androidx.annotation.DrawableRes
import com.example.almacercaapp.model.Store
/**
 * Define una CATEGORÍA DE PRODUCTOS dentro de una tienda.
 * Ej: "Lácteos", "Bebestibles", "Carnes".
 */
data class ProductCategory(
    val id: Int, // ID único de esta categoría (ej: 101 para Lácteos)
    val name: String, // ej: "Lácteos"
    @DrawableRes val imageRes: Int,
    val storeId: Int // La FK que dice a qué tienda pertenece
)
