package com.example.almacercaapp.model

data class CarritoDto(
    val id: Long,             // ID del item del carrito
    val product: ProductDto,  // El producto completo anidado
    val quantity: Int         // La cantidad (ej: 2)
)
