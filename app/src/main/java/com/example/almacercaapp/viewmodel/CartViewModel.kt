package com.example.almacercaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.almacercaapp.model.CartRepository
import com.example.almacercaapp.model.Product
import kotlinx.coroutines.launch

class CartViewModel(
    private val repository: CartRepository
) : ViewModel() {

    val uiState = repository.uiState

    fun addProduct(product: Product) {
        viewModelScope.launch {
            repository.addProduct(product)
        }
    }

    fun incrementQuantity(productId: String) {
        viewModelScope.launch {
            repository.incrementQuantity(productId)
        }
    }

    fun decrementQuantity(productId: String) {
        viewModelScope.launch {
            repository.decrementQuantity(productId)
        }
    }

    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }

    fun removeItem(productId: String) {
        viewModelScope.launch {
            repository.removeItem(productId)
        }
    }

    fun messageShown() {
        repository.messageShown()
    }

    fun loadCart() {
        viewModelScope.launch {
            repository.loadCart()
        }
    }
}