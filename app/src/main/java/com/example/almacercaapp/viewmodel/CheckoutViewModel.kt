package com.example.almacercaapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.almacercaapp.model.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Estado de la UI específico para esta pantalla
data class CheckoutUiState(
    val paymentDetails: String = "Visa *1234", // Valor inicial de maqueta
    val promoCode: String = "",
    val promoError: String? = null,
    val showConfirmDialog: Boolean = false
)

class CheckoutViewModel : ViewModel() {

    // 1. Conecta con el Repositorio para obtener los productos y totales
    val cartUiState = CartRepository.uiState

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
        // Lógica clave: el diálogo se abre, y la compra se prepara.
        // La navegación se hace DESDE el diálogo (en CheckoutScreen.kt).

        // ▼▼▼ AÑADE ESTO: VACÍA EL CARRITO AQUÍ ▼▼▼
        CartRepository.clearCart() // ¡Vacía el carrito!
        // ▲▲▲ HASTA AQUÍ ▲▲▲

        _uiState.update {
            it.copy(showConfirmDialog = true)
        }
    }
    fun onDialogDismiss() {
        _uiState.update { it.copy(showConfirmDialog = false) }
    }
}
