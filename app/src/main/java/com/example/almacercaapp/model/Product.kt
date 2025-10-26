package com.example.almacercaapp.model


import androidx.annotation.DrawableRes

data class Product(
    val id: Int,
    val name: String,
    @DrawableRes val imageRes: Int,
    val price: Double,
    val storeId: Int // Para saber a qué tienda pertenece
)