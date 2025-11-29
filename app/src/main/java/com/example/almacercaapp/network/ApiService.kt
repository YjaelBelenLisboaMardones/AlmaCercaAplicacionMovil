package com.example.almacercaapp.network

import com.example.almacercaapp.model.CartItemDto
import com.example.almacercaapp.model.LoginRequest
import com.example.almacercaapp.model.LoginResponse
import com.example.almacercaapp.model.ProductDto
import com.example.almacercaapp.model.RegisterRequest // 🔥 ¡AÑADE ESTE IMPORT!
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    // --- RUTAS PÚBLICAS ---
    // Login
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    // ✅ REGISTRO (Añadido)
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<LoginResponse>

    // Listar Productos (Público)
    @GET("api/products")
    suspend fun listarProductos(): Response<List<ProductDto>>

    // --- RUTAS PROTEGIDAS ---
    // Obtener Carrito
    @GET("api/cart")
    suspend fun obtenerCarrito(
        @Header("userId") userId: String
    ): Response<List<CartItemDto>>

    // Agregar al Carrito
    @POST("api/cart/add")
    suspend fun agregarProducto(
        @Header("userId") userId: String,
        @Body item: CartItemDto
    ): Response<Void>
}