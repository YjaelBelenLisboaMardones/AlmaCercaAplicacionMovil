package com.example.almacercaapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.almacercaapp.R
import com.example.almacercaapp.model.Product
import com.example.almacercaapp.model.ProductCategory
import com.example.almacercaapp.model.Store
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// 1. Define el estado de la UI para esta pantalla
data class CategoryProductsUiState(
    val products: List<Product> = emptyList(),
    val categoryName: String = "", // Para mostrar en el título
    val isLoading: Boolean = true
)

class CategoryProductsViewModel : ViewModel() {

    // --- SIMULACIÓN DE BASE DE DATOS DE PRODUCTOS ---
    // En una app real, esto vendría de un repositorio o una API

    // 1. "Tabla" de Tiendas (la que estaba en el otro ViewModel)
    private val allStores = listOf(
        Store(id = 1, name = "Botillería La Esquina", storeCategoryId = 502, address = "...", distance = "150 m", logoRes = R.drawable.logo_esquina, rating = 4.8f, description = "..."),
        Store(id = 2, name = "Almacen y Bazar Sandra", storeCategoryId = 505, address = "...", distance = "300 m", logoRes = R.drawable.logo_sandra, rating = 4.5f, description = "..."),
        Store(id = 3, name = "Almacen Mar", storeCategoryId = 505, address = "...", distance = "500 m", logoRes = R.drawable.logo_mariluna, rating = 4.2f, description = "...")
    )


    // "Tabla" de Categorías de Producto (la que estaba en el otro ViewModel)
    private val allProductCategories = listOf(
        ProductCategory(id = 101, name = "Vegetales y Frutas", imageRes = R.drawable.cat_verduras, storeId = 2),
        ProductCategory(id = 102, name = "Aceites", imageRes = R.drawable.cat_aceites, storeId = 2),
        ProductCategory(id = 103, name = "Lacteos", imageRes = R.drawable.cat_lacteos, storeId = 3),
        ProductCategory(id = 104, name = "Carnes", imageRes = R.drawable.cat_carnes, storeId = 3),
        ProductCategory(id = 105, name = "Bebestibles", imageRes = R.drawable.cat_bebestibles, storeId = 1),
        ProductCategory(id = 106, name = "Despensa", imageRes = R.drawable.cat_despensa, storeId = 1)
    )
    private val allProducts = listOf(
        // Bebestibles para Tienda 1
        Product(id = 301, name = "Diet Coke", imageRes = R.drawable.prod_diet_coke, price = 1000.0, size = "355ml", category = allProductCategories.find { it.id == 105 }!!, store = allStores.find { it.id == 1 }!!),
        Product(id = 302, name = "Sprite En Lata", imageRes = R.drawable.prod_sprite, price = 1500.0, size = "355ml", category = allProductCategories.find { it.id == 105 }!!, store = allStores.find { it.id == 1 }!!),
        Product(id = 303, name = "Jugo Natural Frutilla", imageRes = R.drawable.prod_jugo_frutilla, price = 1500.0, size = "2L", category = allProductCategories.find { it.id == 105 }!!, store = allStores.find { it.id == 1 }!!),
        Product(id = 304, name = "Jugo Naranja", imageRes = R.drawable.prod_jugo_frutilla, price = 1990.0, size = "2L", category = allProductCategories.find { it.id == 105 }!!, store = allStores.find { it.id == 1 }!!),
        Product(id = 305, name = "Coca-Cola En Lata", imageRes = R.drawable.prod_coca_lata_normal, price = 1200.0, size = "355ml", category = allProductCategories.find { it.id == 105 }!!, store = allStores.find { it.id == 1 }!!),
        Product(id = 306, name = "Pepsi en Lata", imageRes = R.drawable.prod_pepsi_lata_normal, price = 1200.0, size = "355ml", category = allProductCategories.find { it.id == 105 }!!, store = allStores.find { it.id == 1 }!!),
        Product(id = 307, name = "Tomates", imageRes = R.drawable.prod_tomate, price = 1200.0, size = "1kg", category = allProductCategories.find { it.id == 101 }!!, store = allStores.find { it.id == 2 }!!),
        Product(id = 308, name = "Manzana", imageRes = R.drawable.prod_manzana, price = 1100.0, size = "1kg", category = allProductCategories.find { it.id == 101 }!!, store = allStores.find { it.id == 2 }!!),
        Product(id = 309, name = "Lechuga", imageRes = R.drawable.prod_lechuga_costina, price = 800.0, size = "1 unidad", category = allProductCategories.find { it.id == 101 }!!, store = allStores.find { it.id == 2 }!!)
    )


    private val _uiState = MutableStateFlow(CategoryProductsUiState())
    val uiState: StateFlow<CategoryProductsUiState> = _uiState.asStateFlow()

    // 2. La función clave que la UI llamará

    // La función clave que ahora es "autosuficiente"
    fun loadProducts(storeId: String?, productCategoryId: Int?) {
        _uiState.update { it.copy(isLoading = true) }

        if (storeId == null || productCategoryId == null) {
            _uiState.update { it.copy(isLoading = false) }
            return
        }

        // 1. Busca el nombre de la categoría usando el ID que recibió
        val categoryObject = allProductCategories.find { it.id == productCategoryId }

        if (categoryObject == null) {
            _uiState.update { it.copy(isLoading = false, categoryName = "Categoría no encontrada") }
            return
        }

        // 2. Filtra los productos usando el ID del objeto ProductCategory
        val filteredProducts = allProducts.filter {
            it.store.id.toString() == storeId && it.category.id == productCategoryId
        }

        // 3. Actualiza el estado con la lista de productos y el nombre del objeto encontrado
        _uiState.update {
            it.copy(
                isLoading = false,
                products = filteredProducts,
                categoryName = categoryObject.name // <-- Accedemos al nombre de forma segura
            )
        }
    }
}