package com.example.almacercaapp.data.repository

import com.example.almacercaapp.model.ProductDto
import com.example.almacercaapp.network.ApiService

/**
 * Repositorio para gestionar todos los datos relacionados con productos.
 * Actúa como la única fuente de verdad para los datos de productos, ya sea
 * que vengan de la red o de una futura caché local.
 */
class ProductRepository(private val apiService: ApiService) {

    /**
     * Llama al backend para obtener solo los productos de una categoría específica.
     * Utilizado por la vista del cliente.
     */
    suspend fun getProductsByCategory(categoryId: String): Result<List<ProductDto>> {
        return try {
            // Volvemos a pasar el String directamente
            val response = apiService.getProductsByCategory(categoryId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Respuesta de error no disponible"
                Result.failure(Exception("Error ${response.code()}: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Llama al backend para crear un nuevo producto.
     * Utilizado por el panel de administración.
     */
    suspend fun createProduct(product: ProductDto): Result<ProductDto> {
        return try {
            val response = apiService.createProduct(product)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                 val errorBody = response.errorBody()?.string() ?: "Respuesta de error no disponible"
                Result.failure(Exception("Error ${response.code()}: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Llama al backend para obtener TODOS los productos (usado por el admin).
     */
    suspend fun getAllProducts(): Result<List<ProductDto>> {
        return try {
            val response = apiService.getAllProductsFromDefaultStore()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Respuesta de error no disponible"
                Result.failure(Exception("Error ${response.code()}: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
     suspend fun updateProduct(product: ProductDto): Result<ProductDto> {
        return try {
            val response = apiService.updateProduct(product.id!!, product)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Respuesta de error no disponible"
                Result.failure(Exception("Error ${response.code()}: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteProduct(productId: String): Result<Unit> {
        return try {
            val response = apiService.deleteProduct(productId)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                val errorBody = response.errorBody()?.string() ?: "Respuesta de error no disponible"
                Result.failure(Exception("Error ${response.code()}: $errorBody"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
