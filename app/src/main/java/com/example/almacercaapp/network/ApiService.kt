package com.example.almacercaapp.network

import com.example.almacercaapp.model.CartItemDto // Asegúrate de haber creado este (ver nota abajo)
import com.example.almacercaapp.model.LoginRequest
import com.example.almacercaapp.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    // --- RUTAS PÚBLICAS ---
    // Aquí ya usamos tus modelos reales
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    // --- RUTAS PROTEGIDAS ---

    // ⚠️ CAMBIO CRÍTICO:
    // Tu backend devuelve "[]" (una lista), NO un objeto "{...}".
    // Por eso usamos List<CartItemDto> en lugar de CarritoDto.
    @GET("api/cart")
    suspend fun obtenerCarrito(
        @Header("userId") userId: String
    ): Response<List<CartItemDto>>

    // Para agregar al carrito
    @POST("api/cart/add")
    suspend fun agregarProducto(
        @Header("userId") userId: String,
        @Body item: CartItemDto
    ): Response<Void>
}