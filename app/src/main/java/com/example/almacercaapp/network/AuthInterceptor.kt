package com.example.almacercaapp.network

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

// Este interceptor es el "guardia de seguridad" de nuestra red.
class AuthInterceptor(private val dataStore: DataStore<Preferences>) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        // Leemos el token actual desde DataStore de forma síncrona.
        // Esto es aceptable aquí porque DataStore es muy rápido leyendo desde memoria.
        val token = runBlocking {
            dataStore.data.map { preferences ->
                preferences[stringPreferencesKey("user_token")]
            }.first()
        }

        val originalRequest = chain.request()

        // Si no hay token, dejamos la petición como está (para endpoints públicos como login).
        if (token == null) {
            return chain.proceed(originalRequest)
        }

        // Si hay token, construimos una nueva petición con la cabecera de autorización.
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(newRequest)
    }
}