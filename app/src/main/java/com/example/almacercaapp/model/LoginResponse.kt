package com.example.almacercaapp.model

// Respuesta que se recibe del backend tras un login o registro exitoso
data class LoginResponse(
    val userId: String, // <-- AÑADIDO: El ID del usuario que inicia sesión
    val role: String   // El rol del usuario ("ADMIN" o "BUYER")
)
