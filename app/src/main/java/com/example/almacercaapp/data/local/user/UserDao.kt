package com.example.almacercaapp.data.local.user

import androidx.room.Dao                       // Marca esta interfaz como DAO de Room
import androidx.room.Insert                    // Para insertar filas
import androidx.room.OnConflictStrategy        // Estrategia de conflicto en inserción
import androidx.room.Query                     // Para queries SQL
// No necesita importar UserEntity explícitamente si está en el mismo paquete

// @Dao indica que define operaciones para la tabla users.
//Se define como interactuas con la tabla users. Contiene funciones marcadas con anaotacion de room. que le dicen a room
//como realizar operaciones en la base de datos. (insert, update, delete, query)
@Dao
interface UserDao {

    // Inserta un usuario. ABORT si hay conflicto de PK (no de email; ese lo controlamos a mano).
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(user: UserEntity): Long // <-- Usa la UserEntity de este paquete

    // Devuelve un usuario por email (o null si no existe).
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): UserEntity? // <-- Usa la UserEntity de este paquete

    // Cuenta total de usuarios (para saber si hay datos y/o para seeds).
    @Query("SELECT COUNT(*) FROM users")
    suspend fun count(): Int

    // Lista completa (útil para debug/administración).
    @Query("SELECT * FROM users ORDER BY id ASC")
    suspend fun getAll(): List<UserEntity> // <-- Usa la UserEntity de este paquete
}