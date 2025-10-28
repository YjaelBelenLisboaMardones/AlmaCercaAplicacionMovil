package com.example.almacercaapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.almacercaapp.model.FavoritesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


data class FavoritesUiState(
    val showNotification: Boolean = false,
    val notificationText: String = ""
)

class FavoritesViewModel : ViewModel() {
    // Expone la lista de productos favoritos desde el Repositorio
    val favoriteProducts = FavoritesRepository.favoriteProducts

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState = _uiState.asStateFlow()

    // Expone la acción de añadir todo al carrito
    fun addAllToCart() {
        // Cuenta los items ANTES de añadirlos
        val itemsCount = FavoritesRepository.favoriteProducts.value.size
        if (itemsCount == 0) return // No hagas nada si no hay favoritos

        // Llama a la lógica del Repositorio
        FavoritesRepository.addAllToCart()

        // Elige el texto correcto
        val text = if (itemsCount == 1) "1 favorito añadido" else "$itemsCount favoritos añadidos"

        // Actualiza el estado para mostrar la notificación
        _uiState.update { it.copy(showNotification = true, notificationText = text) }
    }

    fun notificationShown() {
        _uiState.update { it.copy(showNotification = false, notificationText = "") }
    }

}