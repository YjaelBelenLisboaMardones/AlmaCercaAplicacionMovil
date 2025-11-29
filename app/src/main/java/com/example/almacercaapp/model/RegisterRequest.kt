package com.example.almacercaapp.model

// El Backend espera estos tres campos para el registro
data class RegisterRequest(
    val email: String,
    val password: String,
    val role: String // Asegúrate de que el Backend espera "role"
)