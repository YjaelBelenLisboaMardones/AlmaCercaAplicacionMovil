package com.example.almacercaapp.network

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// AHORA ES UNA CLASE que recibe DataStore para poder crear el AuthInterceptor
class RetrofitClient(dataStore: DataStore<Preferences>) {

    private val baseUrl = "http://10.0.2.2:8080/"

    // El ApiService ahora se crea dentro de la clase
    val instance: ApiService

    init {
        // 1. Interceptor para ver los logs de la red (ya lo teníamos)
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // 2. NUEVO: Creamos el interceptor de autenticación, pasándole el DataStore
        val authInterceptor = AuthInterceptor(dataStore)

        // 3. Creamos el cliente OkHttp y AÑADIMOS AMBOS INTERCEPTORES.
        // El de autenticación DEBE ir antes que el de logging.
        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        // 4. Creamos la instancia de Retrofit usando el nuevo cliente
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // 5. Finalmente, creamos la instancia del ApiService
        instance = retrofit.create(ApiService::class.java)
    }
}
