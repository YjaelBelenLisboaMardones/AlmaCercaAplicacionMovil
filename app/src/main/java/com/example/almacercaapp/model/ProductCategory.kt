package com.example.almacercaapp.model

import androidx.annotation.DrawableRes

data class ProductCategory(
    val id: Int,
    val name:String,
    @DrawableRes val imageRes: Int,
    val storeId: Int // Para saber a qué tienda pertenece esta categoría
)
