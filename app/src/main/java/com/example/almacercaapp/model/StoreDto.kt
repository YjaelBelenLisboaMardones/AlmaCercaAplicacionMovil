package com.example.almacercaapp.model

// DTO para las Tiendas que vienen de la API
data class StoreDto(
    val id: String,
    val name: String,
    val imageUrl: String,
    val address: String
)
