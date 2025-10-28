package com.example.almacercaapp.model

import com.example.almacercaapp.model.ProductCategory
import androidx.annotation.DrawableRes

data class Product(
    val id: Int,
    val name: String,
    @DrawableRes val imageRes: Int,
    val price: Double,
    val size: String,
    // ▼▼▼ ¡TU ARQUITECTURA! Ahora contiene el objeto completo ▼▼▼
    val category: ProductCategory,
    val store: Store,
    val description: String = "Descripción por defecto del producto.",
    val nutrients: String = "Información nutricional por defecto (100gr)."
)