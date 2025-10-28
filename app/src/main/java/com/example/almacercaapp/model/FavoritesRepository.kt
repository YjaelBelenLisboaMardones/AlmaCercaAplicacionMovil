package com.example.almacercaapp.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Singleton (object) que actúa como "base de datos" en memoria
 * para los productos favoritos.
 */
object FavoritesRepository {

    private val _favoriteProducts = MutableStateFlow<List<Product>>(emptyList())
    val favoriteProducts = _favoriteProducts.asStateFlow() // La UI observará esto

    /**
     * Añade o quita un producto de la lista de favoritos.
     */
    fun toggleFavorite(product: Product) {
        _favoriteProducts.update { currentList ->
            val isAlreadyFavorite = currentList.any { it.id == product.id }
            if (isAlreadyFavorite) {
                // Si ya está, lo quita
                currentList.filterNot { it.id == product.id }
            } else {
                // Si no está, lo añade
                currentList + product
            }
        }
    }

    /**
     * Revisa si un producto ya es favorito (para la pantalla de detalle).
     */
    fun isFavorite(productId: Int): Boolean {
        return _favoriteProducts.value.any { it.id == productId }
    }

    /**
     * Añade 1 unidad de cada producto favorito al carrito.
     * (Usa la función 'addProduct' que ya existe en CartRepository)
     */
    fun addAllToCart() {
        _favoriteProducts.value.forEach { product ->
            CartRepository.addAllProducts(_favoriteProducts.value)        }
    }
}