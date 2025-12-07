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
    // --- LÓGICA DE CÁLCULO DE TOTALES ---
    private val subtotal: Double get() = items.sumOf { it.product.price * it.quantity }
    val impuestos: Double get() = subtotal * 0.19 // 19% de IVA (Chile)
    val total: Double get() = subtotal + impuestos

    // --- PROPIEDADES FORMATEADAS PARA LA UI ---
    val formattedSubtotal: String get() = formatPrice(subtotal)
    val formattedImpuestos: String get() = formatPrice(impuestos)
    val formattedTotal: String get() = formatPrice(total)
    val totalItems: Int get() = items.sumOf { it.quantity }
    // Función de ayuda para formatear
    private fun formatPrice(price: Double): String {
        val format = NumberFormat.getCurrencyInstance(Locale.Builder().setLanguage("es").setRegion("CL").build())
        format.maximumFractionDigits = 0
        return format.format(price)
    }
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

    fun incrementQuantity(productId: String) {
        _uiState.update { currentState ->
            val newItems = currentState.items.map {
                if (it.product.id == productId) it.copy(quantity = it.quantity + 1) else it
            }
            currentState.copy(items = newItems)
        }
    }

    fun decrementQuantity(productId: String) {
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

    fun removeItem(productId: String) {
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

    fun clearCart() {
        _uiState.update { currentState ->
            currentState.copy(items = emptyList())
        }
    }

    fun messageShown() {
        _uiState.update { it.copy(productAddedMessage = null) }
    }

}
