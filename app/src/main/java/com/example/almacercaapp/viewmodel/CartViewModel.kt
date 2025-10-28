package com.example.almacercaapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.almacercaapp.model.Product
import com.example.almacercaapp.model.CartRepository // <-- IMPORTANTE

/**
 *cartuistate fue movido a cart repository, para que no me cree otra instancia vacia
 * yy todo se almacene en repository como object y no me rompa la consistencia dedatos
 *  Modelo que representa el estado completo de la pantalla del carrito.
 */


// --- VIEWMODEL DEL CARRITO ---

class CartViewModel : ViewModel() {
    // 1. El ViewModel expone el estado del Repositorio
    val uiState = CartRepository.uiState

    // 2. Las acciones solo llaman al Repositorio
    fun addProduct(product: Product) {
        CartRepository.addProduct(product)
    }

    fun incrementQuantity(productId: Int) {
        CartRepository.incrementQuantity(productId)
    }

    fun decrementQuantity(productId: Int) {
        CartRepository.decrementQuantity(productId)
    }

    fun removeItem(productId: Int) {
        CartRepository.removeItem(productId)
    }

    fun messageShown() {
        CartRepository.messageShown()
    }
}
