package com.example.almacercaapp.data.repository

import com.example.almacercaapp.model.ProductDto
import com.example.almacercaapp.network.ApiService

// Repositorio dedicado a gestionar la lógica de datos de los productos.
class ProductRepository(private val apiService: ApiService) {

    /**
     * Llama al backend para obtener la lista completa de productos.
     * Esta es una función pública que usan los clientes.
     */
    suspend fun getProducts(): Result<List<ProductDto>> {
        return try {
            val response = apiService.listarProductos()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener los productos del servidor."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Llama al backend para crear un nuevo producto.
     * Esta es una función protegida para administradores.
     */
    suspend fun createProduct(product: ProductDto): Result<ProductDto> {
        return try {
            val response = apiService.createProduct(product)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al crear el producto en el servidor."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProduct(product: ProductDto): Result<ProductDto> {
        return try {
            // Asume que el ID de ProductDto no está vacío para una actualización
            if (product.id.isBlank()) {
                return Result.failure(IllegalArgumentException("El ID del producto es necesario para actualizar."))
            }

            val response = apiService.updateProduct(product.id, product)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al actualizar el producto."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteProduct(productId: String): Result<Unit> {
        return try {
            val response = apiService.deleteProduct(productId)
            if (response.isSuccessful) {
                Result.success(Unit) // Éxito si el código de respuesta es 2xx
            } else {
                Result.failure(Exception("Error al eliminar el producto."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
