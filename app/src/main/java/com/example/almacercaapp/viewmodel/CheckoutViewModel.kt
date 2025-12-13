package com.example.almacercaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.almacercaapp.model.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Estado de la UI específico para esta pantalla
data class CheckoutUiState(
    val paymentDetails: String = "Visa *1234", // Valor inicial de maqueta
    val promoCode: String = "",
    val promoError: String? = null,
    val showConfirmDialog: Boolean = false
)

class CheckoutViewModel(
    private val cartRepository: CartRepository
) : ViewModel() {

    // 1. Conecta con el Repositorio para obtener los productos y totales
    val cartUiState = cartRepository.uiState

    // 2. Estado mutable para los campos de la UI
    private val _uiState = MutableStateFlow(CheckoutUiState())
    val uiState = _uiState.asStateFlow()

    // --- Funciones para actualizar la UI ---

    fun onPaymentDetailsChanged(text: String) {
        _uiState.update { it.copy(paymentDetails = text) }
    }

    fun onPromoCodeChanged(text: String) {
        _uiState.update {
            it.copy(
                promoCode = text,
                promoError = null
            )
        } // Limpia el error al escribir
    }

    // --- Lógica de Maqueta ---

    fun verifyPromoCode() {
        // Lógica de maqueta simple
        if (_uiState.value.promoCode.equals("PROMO10", ignoreCase = true)) {
            // Aquí podrías aplicar un descuento, pero por ahora solo limpiamos
            _uiState.update { it.copy(promoError = null) }
            // (En un futuro, aquí modificarías el total en el CartRepository)
        } else {
            _uiState.update { it.copy(promoError = "Código promocional inválido") }
        }
    }

    fun onConfirmPurchase() {
        // Vacía el carrito (suspend function, requiere coroutine)
        viewModelScope.launch {
            cartRepository.clearCart()
        }

        _uiState.update {
            it.copy(showConfirmDialog = true)
        }
    }

    fun onDialogDismiss() {
        _uiState.update { it.copy(showConfirmDialog = false) }
    }
}