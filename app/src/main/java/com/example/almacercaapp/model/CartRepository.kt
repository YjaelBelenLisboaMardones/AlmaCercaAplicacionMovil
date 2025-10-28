package com.example.almacercaapp.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat
import java.util.Locale

// (Si 'CartUiState' está en tu ViewModel, bórralo de allá)
data class CartUiState(
    val items: List<CartItem> = emptyList(),
    val productAddedMessage: String? = null
) {
    val totalItems: Int get() = items.sumOf { it.quantity }
    private val totalPrice: Double get() = items.sumOf { it.product.price * it.quantity }
    val formattedTotalPrice: String get() {
        val format = NumberFormat.getCurrencyInstance(Locale.Builder().setLanguage("es").setRegion("CL").build())
        format.maximumFractionDigits = 0
        return format.format(totalPrice)
    }
}

// 2. EL SINGLETON (LA "BASE DE DATOS" EN MEMORIA)
object CartRepository {

    // Aquí vive el estado REAL del carrito
    private val _uiState = MutableStateFlow(CartUiState())
    val uiState = _uiState.asStateFlow() // La UI observará esto

    // Función para añadir productos
    fun addProduct(product: Product) {
        _uiState.update { currentState ->
            val existingItem = currentState.items.find { it.product.id == product.id }
            val newItems = if (existingItem != null) {
                currentState.items.map {
                    if (it.product.id == product.id) it.copy(quantity = it.quantity + 1) else it
                }
            } else {
                currentState.items + CartItem(product = product, quantity = 1)
            }
            currentState.copy(items = newItems, productAddedMessage = "Producto agregado exitosamente")
        }
    }

    fun incrementQuantity(productId: Int) {
        _uiState.update { currentState ->
            val newItems = currentState.items.map {
                if (it.product.id == productId) it.copy(quantity = it.quantity + 1) else it
            }
            currentState.copy(items = newItems)
        }
    }

    fun decrementQuantity(productId: Int) {
        _uiState.update { currentState ->
            val itemToDecrement = currentState.items.find { it.product.id == productId }
            val newItems = if (itemToDecrement != null && itemToDecrement.quantity > 1) {
                currentState.items.map {
                    if (it.product.id == productId) it.copy(quantity = it.quantity - 1) else it
                }
            } else {
                currentState.items.filterNot { it.product.id == productId }
            }
            currentState.copy(items = newItems)
        }
    }

    fun removeItem(productId: Int) {
        _uiState.update { currentState ->
            val newItems = currentState.items.filterNot { it.product.id == productId }
            currentState.copy(items = newItems)
        }
    }

    fun addAllProducts(products: List<Product>) {
        products.forEach { product ->
            // Reutiliza la lógica de 'addProduct' que ya existe
            addProduct(product)
        }
    }
    fun addMultipleProducts(product: Product, quantityToAdd: Int) {
        _uiState.update { currentState ->
            val existingItem = currentState.items.find { it.product.id == product.id }

            val newItems = if (existingItem != null) {
                // Producto ya existe, incrementa la cantidad
                currentState.items.map {
                    if (it.product.id == product.id) {
                        it.copy(quantity = it.quantity + quantityToAdd)
                    } else {
                        it
                    }
                }
            } else {
                // Producto nuevo, añádelo a la lista con la cantidad
                currentState.items + CartItem(product = product, quantity = quantityToAdd)
            }
            currentState.copy(items = newItems)
        }
    }

    fun messageShown() {
        _uiState.update { it.copy(productAddedMessage = null) }
    }

}