package com.example.almacercaapp.model

// Data Transfer Object para Productos. 
// Esta es la representación de un producto que se envía y recibe de la API.
data class ProductDto(
    val id: String,          // ID de MongoDB (es un String)
    val name: String,
    val description: String,
    val price: Double,
    val stock: Int,
    val imageUrl: String
)
