package com.example.almacercaapp.model

// Respuesta que se recibe del backend tras un login o registro exitoso
data class LoginResponse(
    val token: String, // El token JWT para autenticar futuras peticiones
    val role: String   // El rol del usuario ("ADMIN" o "BUYER")
)
