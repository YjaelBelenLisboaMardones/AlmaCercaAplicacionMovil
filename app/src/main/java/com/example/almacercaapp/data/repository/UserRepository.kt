package com.example.almacercaapp.data.repository

import com.example.almacercaapp.data.local.user.UserDao
import com.example.almacercaapp.data.local.user.UserEntity
import com.example.almacercaapp.data.local.user.UserRole


// Repositorio: actua como intermediario entre la logica de negocio (el authViewModel) y la base de datos (el userDao).
//su proposito es abstraer de donde vienen los datos.
//el Viewmodel le pide al repo "registrar el usuario" o "valida este login", y es el repo quien sabe que tiene
//que usar el userdao para hablar con la base de datos sQLite.
class UserRepository(
    private val userDao: UserDao // Inyección del DAO
) {

    // Login: busca por email y valida contraseña
    suspend fun login(email: String, password: String): Result<UserEntity> {
        // Busca usuario por email usando el DAO
        val user = userDao.getByEmail(email)
        // Verifica si el usuario existe Y la contraseña coincide
        return if (user != null && user.password == password) {
            Result.success(user) // Éxito, devuelve el usuario encontrado
        } else {
            // Error, devuelve una excepción con un mensaje claro
            Result.failure(IllegalArgumentException("Credenciales inválidas"))
        }
    }

    // Registro: valida no duplicado y crea nuevo usuario (con teléfono)
    suspend fun register(name: String, email: String, phone: String, password: String, role: UserRole): Result<Long> {
        // Verifica si ya existe un usuario con ese email
        val exists = userDao.getByEmail(email) != null
        if (exists) {
            // Si existe, devuelve un error
            return Result.failure(IllegalStateException("El correo ya está registrado"))
        }
        // Si no existe, crea la entidad UserEntity con los datos
        val newUser = UserEntity(
            name = name,
            email = email,
            phone = phone, // Incluye el teléfono
            password = password, // Usa la contraseña proporcionada
            role = role
        )
        // Inserta el nuevo usuario usando el DAO
        val id = userDao.insert(newUser)
        // Devuelve el ID generado por la base de datos como señal de éxito
        return Result.success(id)
    }
}

/* Tu AuthViewModel sigue usando User.kt para manejar los datos que se muestran y se editan en las pantallas de SignInScreen y SignUpScreen.
Cuando el usuario presiona "Registrar" o "Entrar", el AuthViewModel toma los datos de tu User.kt (el del modelo UI) y se los pasa al UserRepository.
El UserRepository convierte esos datos al formato UserEntity.kt antes de guardarlos o buscarlos en la base de datos usando el UserDao*/