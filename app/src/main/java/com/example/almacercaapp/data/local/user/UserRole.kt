package com.example.almacercaapp.data.local.user

// Enum para representar los roles de usuario en toda la aplicación.
// Usar un enum previene errores de tipeo con strings como "Admin" o "buyer".
enum class UserRole {
    ADMIN, // Rol de administrador
    BUYER, // Rol de comprador (cliente normal)
    SELLER // Rol de vendedor
}
