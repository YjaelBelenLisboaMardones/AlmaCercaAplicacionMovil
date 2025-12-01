package com.example.almacercaapp.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.almacercaapp.data.local.user.UserRole
import com.example.almacercaapp.model.LoginRequest
import com.example.almacercaapp.model.RegisterRequest
import com.example.almacercaapp.network.ApiService
import kotlinx.coroutines.flow.map

class UserRepository(
    private val apiService: ApiService,
    private val dataStore: DataStore<Preferences>
) {

    private object PreferencesKeys {
        val USER_TOKEN = stringPreferencesKey("user_token")
        val USER_ROLE = stringPreferencesKey("user_role")
    }

    // --- API CALLS ---

    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            val request = LoginRequest(email, password)
            val response = apiService.login(request)

            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!
                saveUserSession(token = loginResponse.token, role = loginResponse.role)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Credenciales inválidas"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(name: String, email: String, phone: String, password: String, role: UserRole): Result<Unit> {
        return try {
            val request = RegisterRequest(name, email, phone, password, role.name)
            val response = apiService.register(request)

            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!
                saveUserSession(token = loginResponse.token, role = loginResponse.role)
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error en el registro. El correo podría ya existir."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // --- LOCAL SESSION MANAGEMENT ---

    private suspend fun saveUserSession(token: String, role: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_TOKEN] = token
            preferences[PreferencesKeys.USER_ROLE] = role
        }
    }

    // Expone el Flow del token para que el interceptor pueda leerlo
    val userTokenFlow = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_TOKEN]
    }

    // Expone el Flow del rol para que el ViewModel pueda leerlo
    val userRoleFlow = dataStore.data.map { preferences ->
        preferences[PreferencesKeys.USER_ROLE]
    }

    suspend fun logout() {
        dataStore.edit { it.clear() }
    }
}