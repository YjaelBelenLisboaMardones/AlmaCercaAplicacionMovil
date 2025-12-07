package com.example.almacercaapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.almacercaapp.data.repository.ProductRepository
import com.example.almacercaapp.model.ProductDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para la pantalla de Favoritos.
 * Por ahora, mostrará todos los productos de la tienda principal.
 */
class FavoritesViewModel(private val productRepository: ProductRepository) : ViewModel() {

    private val _products = MutableStateFlow<List<ProductDto>>(emptyList())
    val products = _products.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        // Carga los productos en cuanto el ViewModel se crea.
        loadAllProducts()
    }

    fun loadAllProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            // Llama al repositorio para obtener los productos desde la nube.
            productRepository.getAllProducts().onSuccess {
                _products.value = it
            }.onFailure {
                // Opcional: manejar el error, por ejemplo, mostrando un mensaje.
            }
            _isLoading.value = false
        }
    }
}
