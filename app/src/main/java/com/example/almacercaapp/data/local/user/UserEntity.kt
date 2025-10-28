package com.example.almacercaapp.data.local.user // <-- PAQUETE CORREGIDO

import androidx.room.Entity
import androidx.room.PrimaryKey

// @Entity declara una tabla SQLite manejada por Room.
// tableName = "users" define el nombre exacto de la tabla.
//Representará la tabla de usuarios en la base de datos.
// Cada instancia de UserEntity representa una fila en esta tabla. Las instancias son las que se insertan en la base de datos.

enum class UserRole {BUYER, SELLER}
@Entity(tableName = "users") //Acá le dice que haga una tabla
data class UserEntity(
    @PrimaryKey(autoGenerate = true)    // Clave primaria autoincremental
    val id: Long = 0L,

    val name: String,                   // Nombre completo del usuario
    val email: String,                  // Correo (idealmente único a nivel de negocio)
    val phone: String,                  // Teléfono del usuario
    val password: String,                // Contraseña (para demo; en prod usar hash)
    val role: UserRole
)