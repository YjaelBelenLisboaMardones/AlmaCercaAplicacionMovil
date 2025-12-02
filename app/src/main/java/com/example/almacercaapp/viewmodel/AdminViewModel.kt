package com.example.almacercaapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.almacercaapp.data.repository.ProductRepository
import com.example.almacercaapp.model.ProductDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminViewModel(private val productRepository: ProductRepository) : ViewModel() {

    // --- ESTADO DE LA LISTA DE PRODUCTOS (AHORA INCLUIDO) ---
    private val _products = MutableStateFlow<List<ProductDto>>(emptyList())
    val products = _products.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // --- ESTADO DEL FORMULARIO DE CREACIÓN ---
    val productName = mutableStateOf("")
    val productDescription = mutableStateOf("")
    val productPrice = mutableStateOf("")
    val productStock = mutableStateOf("")
    val productImageUrl = mutableStateOf("")

    private val _creationStatus = MutableStateFlow<String?>(null)
    val creationStatus = _creationStatus.asStateFlow()

    init {
        // Carga los productos en cuanto el ViewModel se crea
        loadProducts()
    }

    /**
     * Carga la lista de productos desde el repositorio.
     */
    fun loadProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = productRepository.getProducts()
            if (result.isSuccess) {
                _products.value = result.getOrNull() ?: emptyList()
            }
            _isLoading.value = false
        }
    }

    /**
     * Llama al repositorio para crear un nuevo producto con los datos del formulario.
     */
    fun createProduct() {
        if (productName.value.isBlank() || productPrice.value.isBlank()) {
            _creationStatus.value = "Error: El nombre y el precio son obligatorios."
            return
        }

        viewModelScope.launch {
            _creationStatus.value = "Creando producto..."

            val product = ProductDto(
                id = "", // El backend debe generar el ID
                name = productName.value,
                description = productDescription.value,
                price = productPrice.value.toDoubleOrNull() ?: 0.0,
                stock = productStock.value.toIntOrNull() ?: 0,
                imageUrl = productImageUrl.value
            )

            val result = productRepository.createProduct(product)

            if (result.isSuccess) {
                _creationStatus.value = "¡Producto creado con éxito!"
                loadProducts() // Recarga la lista de productos para mostrar el nuevo
                // Limpiar campos
                productName.value = ""
                productDescription.value = ""
                productPrice.value = ""
                productStock.value = ""
                productImageUrl.value = ""
            } else {
                _creationStatus.value = result.exceptionOrNull()?.message ?: "Error desconocido"
            }
        }
    }

    /**
     * Limpia el mensaje de estado para que no se muestre indefinidamente.
     */
    fun clearStatusMessage() {
        _creationStatus.value = null
    }
}
