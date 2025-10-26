package com.example.almacercaapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.almacercaapp.R
import com.example.almacercaapp.model.ProductCategory
import com.example.almacercaapp.model.Store
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update// ▼▼▼ 2. AÑADE la lista de categorías de productos al UiState ▼▼▼

// Define el estado de la UI
data class StoreDetailUiState(
    val store: Store? = null,
    val productCategories: List<ProductCategory> = emptyList(),
    val isLoading: Boolean = true
)

class StoreDetailViewModel : ViewModel() {
    //simulacion de datos
    // En una app real, esto vendría de un repositorio que consulta una API o DB
    private val storesData = listOf(
        Store(
            id = 1,
            name = "Botillería La Esquina",
            category = "Botillería",
            address = "...",
            distance = "150 m",
            logoRes = R.drawable.logo_esquina,
            // ▼▼▼ AÑADE LOS PARÁMETROS FALTANTES ▼▼▼
            rating = 4.8f,
            description = "La mejor botillería de la zona, con una gran variedad de licores y bebidas."
        ),
        Store(
            id = 2,
            name = "Almacen y Bazar Sandra",
            category = "Almacen & Bazar",
            address = "...",
            distance = "300 m",
            logoRes = R.drawable.logo_sandra,
            // ▼▼▼ AÑADE LOS PARÁMETROS FALTANTES ▼▼▼
            rating = 4.5f,
            description = "Tu almacén de confianza para las compras del día a día. Frutas, verduras y abarrotes."
        ),
        Store(
            id = 3,
            name = "Almacen Mar",
            category = "Almacen",
            address = "...",
            distance = "500 m",
            logoRes = R.drawable.logo_mariluna,
            // ▼▼▼ AÑADE LOS PARÁMETROS FALTANTES ▼▼▼
            rating = 4.2f,
            description = "Especialistas en productos del mar y conservas. Frescura garantizada."
        )
    )

    // ▼▼▼ CORRECCIÓN 1: Parámetros de ProductCategory arreglados ▼▼▼
    private val allProductCategories = listOf(
        // Tienda 2
        ProductCategory(101, "Vegetales y Frutas", R.drawable.cat_verduras, 2),
        ProductCategory(102, "Aceites", R.drawable.cat_aceites, 2),
        // Tienda 1
        ProductCategory(103, "Lacteos", R.drawable.cat_lacteos, 3),
        ProductCategory(104, "Carnes", R.drawable.cat_carnes, 3),
        ProductCategory(105, "Bebestibles", R.drawable.cat_bebestibles, 1),
        ProductCategory(106, "Despensa", R.drawable.cat_despensa, 1)
    )

    private val _uiState = MutableStateFlow(StoreDetailUiState())
    val uiState: StateFlow<StoreDetailUiState> = _uiState.asStateFlow()

    // La función CLAVE que la UI llamará
    fun loadStoreDetails(storeId: String?) {
        if (storeId == null) {
            _uiState.update { it.copy(isLoading = false, store = null) }
            return
        }

        // Busca la tienda en nuestra lista de datos de ejemplo
        val foundStore = storesData.find { it.id.toString() == storeId }

        // ▼▼▼ CORRECCIÓN 2: Filtrar y pasar las categorías a la UI ▼▼▼
        val storeProductCategories = allProductCategories.filter { it.storeId.toString() == storeId }

        _uiState.update {
            it.copy(
                isLoading = false,
                store = foundStore,
                productCategories = storeProductCategories // ¡Añadido!
            )
        }
    }
}

