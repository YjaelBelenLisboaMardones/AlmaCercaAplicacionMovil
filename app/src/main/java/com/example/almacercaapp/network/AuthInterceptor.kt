package com.example.almacercaapp.network

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Este interceptor es el "guardia de seguridad" de nuestra red.
 * Ya no depende de UserRepository, sino directamente de DataStore para romper el ciclo.
 */
class AuthInterceptor(private val dataStore: DataStore<Preferences>) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        // Lee el ID de usuario directamente desde DataStore.
        val userId = runBlocking {
            dataStore.data.map { preferences ->
                preferences[stringPreferencesKey("user_id")]
            }.first()
        }

        val originalRequest = chain.request()

        // Si no hay userId, la petición continúa sin cabecera (para login, etc.)
        if (userId == null) {
            return chain.proceed(originalRequest)
        }

        // Si hay userId, se añade la cabecera.
        val newRequest = originalRequest.newBuilder()
            .header("userId", userId)
            .build()

        return chain.proceed(newRequest)
    }
}
