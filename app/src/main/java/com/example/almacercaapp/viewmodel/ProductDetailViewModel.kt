package com.example.almacercaapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.almacercaapp.model.CartRepository
import com.example.almacercaapp.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat
import java.util.Locale
import com.example.almacercaapp.model.FavoritesRepository

// Estado de la UI para esta pantalla
data class ProductDetailUiState(
    val product: Product? = null,
    val quantity: Int = 1,
    val totalPrice: Double = 0.0,
    val isFavorite: Boolean = false
) {
    // Propiedad formateada para el total
    val formattedTotalPrice: String get() {
        val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
        format.maximumFractionDigits = 0
        return format.format(totalPrice)
    }
}

class ProductDetailViewModel : ViewModel() {

    // --- ¡CORREGIDO! Lista de productos de maqueta actualizada al nuevo modelo `Product` ---
    private val allProducts = listOf(
        Product(id = "301", name = "Diet Coke", price = 1000.0, description = "Lata de bebida Diet Coke sin azúcar.", imageUrl = "https://i.imgur.com/eG1423f.png", stock = 50, categoryId = "105", storeId = "1"),
        Product(id = "302", name = "Sprite En Lata", price = 1500.0, description = "Lata de bebida Sprite sabor lima-limón.", imageUrl = "https://i.imgur.com/2ma22So.png", stock = 40, categoryId = "105", storeId = "1"),
        Product(id = "303", name = "Jugo Natural Frutilla", price = 1500.0, description = "Jugo natural de frutilla endulzado.", imageUrl = "https://i.imgur.com/I40t2sI.png", stock = 20, categoryId = "105", storeId = "1"),
        Product(id = "304", name = "Jugo Naranja", price = 1990.0, description = "Jugo natural de naranja 100% exprimido.", imageUrl = "https://i.imgur.com/I40t2sI.png", stock = 25, categoryId = "105", storeId = "1"),
        Product(id = "305", name = "Coca-Cola En Lata", price = 1200.0, description = "1 Lata de Bebida Coca Cola Original.", imageUrl = "https://i.imgur.com/62g1h3w.png", stock = 100, categoryId = "105", storeId = "1"),
        Product(id = "306", name = "Pepsi en Lata", price = 1200.0, description = "Lata de bebida Pepsi sabor original", imageUrl = "https://i.imgur.com/eG1423f.png", stock = 80, categoryId = "105", storeId = "1"),
        Product(id = "307", name = "Tomates", price = 1200.0, description = "Tomates frescos para ensalada, por kilo", imageUrl = "https://i.imgur.com/I40t2sI.png", stock = 30, categoryId = "101", storeId = "2"),
        Product(id = "308", name = "Manzana", price = 1100.0, description = "Manzanas rojas (Royal Gala), por kilo.", imageUrl = "https://i.imgur.com/2ma22So.png", stock = 60, categoryId = "101", storeId = "2"),
        Product(id = "309", name = "Lechuga", price = 800.0, description = "Lechuga costina fresca, por unidad.", imageUrl = "https://i.imgur.com/eG1423f.png", stock = 15, categoryId = "101", storeId = "2")
    )

    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState = _uiState.asStateFlow()

    /**
     * Carga el producto basado en el ID y calcula el precio inicial.
     */
    fun loadProduct(productId: String?) {
        // --- ¡CORREGIDO! Se busca directamente por el String ID ---
        val product = allProducts.find { it.id == productId }

        if (product != null) {
            _uiState.update {
                it.copy(
                    product = product,
                    quantity = 1,
                    totalPrice = product.price, // Total inicial es el precio x 1
                    // --- ¡CORREGIDO! Se pasa el ID de tipo String ---
                    isFavorite = FavoritesRepository.isFavorite(product.id)
                )
            }
        }
    }

    /**
     * Cambia la cantidad y recalcula el total.
     */
    fun onQuantityChanged(change: Int) {
        _uiState.update { currentState ->
            val product = currentState.product ?: return@update currentState
            val newQuantity = (currentState.quantity + change).coerceIn(1, 99)

            currentState.copy(
                quantity = newQuantity,
                totalPrice = product.price * newQuantity
            )
        }
    }

    /**
     * Cambia el estado de favorito.
     */
    fun onToggleFavorite() {
        val product = _uiState.value.product ?: return

        FavoritesRepository.toggleFavorite(product)

        _uiState.update { it.copy(isFavorite = !it.isFavorite) }
    }

    /**
     * Añade la cantidad seleccionada de productos al repositorio.
     */
    fun onAddToCart() {
        val product = _uiState.value.product ?: return
        val quantity = _uiState.value.quantity

        CartRepository.addMultipleProducts(product, quantity)
    }
}
