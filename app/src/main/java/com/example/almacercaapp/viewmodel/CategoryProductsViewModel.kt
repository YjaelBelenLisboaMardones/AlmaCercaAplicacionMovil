package com.example.almacercaapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.almacercaapp.R
import com.example.almacercaapp.data.repository.ProductRepository
import com.example.almacercaapp.model.Product
import com.example.almacercaapp.model.ProductCategory
import com.example.almacercaapp.model.ProductDto
import com.example.almacercaapp.model.Store
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * Función de extensión para convertir el modelo de Red (DTO) al modelo de UI.
 * Esto desacopla la capa de red de la capa de presentación.
 */
private fun ProductDto.toProduct(): Product {
    return Product(
        id = this.id ?: "", // Aseguramos que el id no sea nulo para la UI
        name = this.name,
        description = this.description,
        price = this.price,
        imageUrl = this.imageUrl,
        stock = this.stock,
        categoryId = this.categoryId,
        storeId = this.storeId
    )
}

// ¡CORREGIDO! El estado de la UI ahora usa el modelo `Product` que es compatible con la red.
data class CategoryProductsUiState(
    val products: List<Product> = emptyList(), // <-- ¡CAMBIO IMPORTANTE!
    val categoryName: String = "",
    val store: Store? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

class CategoryProductsViewModel(private val productRepository: ProductRepository) : ViewModel() {
    // --- DATOS DE REFERENCIA (HARDCODEADOS) para obtener nombres, logos, etc. ---
    private val allStores = listOf(
        Store(id = 1, name = "Botillería La Esquina", storeCategoryId = 502, address = "...", distance = "150 m", logoRes = R.drawable.logo_esquina, rating = 4.8f, description = "..."),
        Store(id = 2, name = "Almacen y Bazar Sandra", storeCategoryId = 505, address = "...", distance = "300 m", logoRes = R.drawable.logo_sandra, rating = 4.5f, description = "..."),
        Store(id = 3, name = "Almacen Mar", storeCategoryId = 505, address = "...", distance = "500 m", logoRes = R.drawable.logo_mariluna, rating = 4.2f, description = "...")
    )

    private val allProductCategories = listOf(
        ProductCategory(id = "101", name = "Vegetales y Frutas", imageRes = R.drawable.cat_verduras, storeId = "2"),
        ProductCategory(id = "102", name = "Aceites", imageRes = R.drawable.cat_aceites, storeId = "2"),
        ProductCategory(id = "103", name = "Lacteos", imageRes = R.drawable.cat_lacteos, storeId = "3"),
        ProductCategory(id = "104", name = "Carnes", imageRes = R.drawable.cat_carnes, storeId = "3"),
        ProductCategory(id = "105", name = "Bebestibles", imageRes = R.drawable.cat_bebestibles, storeId = "1"),
        ProductCategory(id = "106", name = "Despensa", imageRes = R.drawable.cat_despensa, storeId = "1")
    )

    private val _uiState = MutableStateFlow(CategoryProductsUiState())
    val uiState: StateFlow<CategoryProductsUiState> = _uiState.asStateFlow()

    fun loadProductsForCategory(categoryId: String) {
        val category = allProductCategories.find { it.id == categoryId }
        val categoryName = category?.name ?: "Categoría"

        val storeIdAsInt = category?.storeId?.toIntOrNull()
        val store = allStores.find { it.id == storeIdAsInt }

        _uiState.update { it.copy(isLoading = true, categoryName = categoryName, store = store, error = null) }

        viewModelScope.launch {
            productRepository.getProductsByCategory(categoryId)
                .onSuccess { productsFromCloud -> // `productsFromCloud` es una List<ProductDto>
                    // ▼▼▼ ¡AQUÍ ESTÁ LA SOLUCIÓN! ▼▼▼
                    // Convertimos la lista de DTOs a una lista del modelo de UI (`Product`)
                    val uiProducts = productsFromCloud.map { it.toProduct() }

                    _uiState.update { it.copy(products = uiProducts, isLoading = false) }
                }
                .onFailure { exception ->
                    _uiState.update { it.copy(
                        products = emptyList(),
                        isLoading = false,
                        error = exception.message ?: "Error al cargar productos."
                    ) }
                }
        }
    }
}
