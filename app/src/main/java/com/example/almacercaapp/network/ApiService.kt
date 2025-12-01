package com.example.almacercaapp.network

import com.example.almacercaapp.model.CartItemDto
import com.example.almacercaapp.model.LoginRequest
import com.example.almacercaapp.model.LoginResponse
import com.example.almacercaapp.model.ProductDto
import com.example.almacercaapp.model.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    // --- RUTAS PÚBLICAS ---
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<LoginResponse>

    @GET("api/products")
    suspend fun listarProductos(): Response<List<ProductDto>>

    // --- RUTAS PROTEGIDAS ---
    @GET("api/cart")
    suspend fun obtenerCarrito(): Response<List<CartItemDto>> // El token identificará al usuario

    @POST("api/cart/add")
    suspend fun agregarProducto(
        @Body item: CartItemDto
    ): Response<Void> // El token identificará al usuario
}
