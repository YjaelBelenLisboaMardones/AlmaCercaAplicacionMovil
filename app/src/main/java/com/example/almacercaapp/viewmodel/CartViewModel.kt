package com.example.almacercaapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.almacercaapp.model.Product
import com.example.almacercaapp.model.CartRepository

class CartViewModel : ViewModel() {
    // 1. El ViewModel simplemente expone el estado del Repositorio.
    val uiState = CartRepository.uiState

    // 2. Las acciones del ViewModel ahora solo actúan como un puente,
    //    pasando los datos a la lógica central que vive en el Repositorio.

    fun addProduct(product: Product) {
        CartRepository.addProduct(product)
    }

    // --- ¡AQUÍ ESTÁ LA CORRECCIÓN FINAL! ---
    // Las funciones ahora aceptan `productId` como un String, que coincide
    // con el modelo de datos `Product` y la llamada desde `CartScreen`.

    fun incrementQuantity(productId: String) {
        CartRepository.incrementQuantity(productId)
    }

    fun decrementQuantity(productId: String) {
        CartRepository.decrementQuantity(productId)
    }

    fun removeItem(productId: String) {
        CartRepository.removeItem(productId)
    }

    fun messageShown() {
        CartRepository.messageShown()
    }
}
