package com.example.almacercaapp.model

/**
 * DTO para representar una categoría en la API.
 * Es el equivalente en Kotlin del DTO del backend y es usado por Retrofit y Gson
 * para convertir el JSON de la respuesta en un objeto de Kotlin.
 */
data class CategoryDto(
    // Identificador de la categoría (String para compatibilidad con MongoDB ObjectId)
    val id: String,

    // Nombre legible de la categoría (ej. "Lácteos")
    val name: String
)
