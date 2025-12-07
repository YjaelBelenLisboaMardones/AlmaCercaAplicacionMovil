package com.example.almacercaapp.network

import com.example.almacercaapp.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.DELETE
import retrofit2.http.Path

/**
 * Define el contrato de comunicación entre la App (Frontend) y la API REST (Backend).
 * Cada función corresponde a un endpoint específico, con nombres descriptivos.
 */
interface ApiService {

    // =================================================================================
    // --- ENDPOINTS PÚBLICOS (No requieren autenticación especial) ---
    // =================================================================================

    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<LoginResponse>

    @GET("api/products")
    suspend fun getAllProductsFromDefaultStore(): Response<List<ProductDto>>

    @GET("api/products/category/{categoryId}")
    suspend fun getProductsByCategory(@Path("categoryId") categoryId: String): Response<List<ProductDto>>


    // =================================================================================
    // --- ENDPOINTS DE CLIENTE (Asumen un contexto de usuario en el backend) ---
    // =================================================================================

    @GET("api/cart")
    suspend fun getMyCart(): Response<CartItemDto>

    @POST("api/cart/add")
    suspend fun addProductToMyCart(@Body item: CartItemDto): Response<Void>


    // =================================================================================
    // --- ENDPOINTS DE ADMIN (Corresponden a `ProductAdminController`) ---
    // =================================================================================

    @POST("api/admin/products")
    suspend fun createProduct(@Body product: ProductDto): Response<ProductDto>

    @PUT("api/admin/products/{productId}")
    suspend fun updateProduct(@Path("productId") productId: String, @Body product: ProductDto): Response<ProductDto>

    @DELETE("api/admin/products/{productId}")
    suspend fun deleteProduct(@Path("productId") productId: String): Response<Void>
}
