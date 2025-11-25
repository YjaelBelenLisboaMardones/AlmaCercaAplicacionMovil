package com.example.almacercaapp.model

data class LoginResponse(
    val id: Long,        // Coincide con private Long id;
    val email: String,   // Coincide con private String email;
    val role: String,    // Coincide con private String role;
    val password: String? // Ponemos ? porque tu controller lo pone en null antes de enviarlo
)