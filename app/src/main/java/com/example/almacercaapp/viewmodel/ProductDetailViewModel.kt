package com.example.almacercaapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.almacercaapp.R
import com.example.almacercaapp.model.CartRepository
import com.example.almacercaapp.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat
import java.util.Locale
import com.example.almacercaapp.model.ProductCategory
import com.example.almacercaapp.model.Store
import com.example.almacercaapp.model.FavoritesRepository


// Estado de la UI para esta pantalla
data class ProductDetailUiState(
    val product: Product? = null,
    val quantity: Int = 1,
    val totalPrice: Double = 0.0,
    val isFavorite: Boolean = false
) {
    // Propiedad formateada para el total
    val formattedTotalPrice: String get() {
        val format = NumberFormat.getCurrencyInstance(Locale("es", "CL"))
        format.maximumFractionDigits = 0
        return format.format(totalPrice)
    }
}

class ProductDetailViewModel : ViewModel() {

    // --- Copia de tu lista de productos ---
    // (En una app real, esto vendría de un Repositorio)
    // ¡HE AÑADIDO LAS DESCRIPCIONES Y NUTRIENTES POR DEFECTO!
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
        Product(id = 301, name = "Diet Coke", imageRes = R.drawable.prod_diet_coke, price = 1000.0, size = "355ml", category = allProductCategories.find { it.id == 105 }!!, store = allStores.find { it.id == 1 }!!,description ="Lata de bebida Diet Coke sin azúcar.",nutrients="Calorías: 1, Azúcar: 0g (100ml)"),
        Product(id = 302, name = "Sprite En Lata", imageRes = R.drawable.prod_sprite, price = 1500.0, size = "355ml", category = allProductCategories.find { it.id == 105 }!!, store = allStores.find { it.id == 1 }!!,description ="Lata de bebida Sprite sabor lima-limón.",nutrients="Calorías: 39, Azúcar: 9g (100ml)"),
        Product(id = 303, name = "Jugo Natural Frutilla", imageRes = R.drawable.prod_jugo_frutilla, price = 1500.0, size = "2L", category = allProductCategories.find { it.id == 105 }!!, store = allStores.find { it.id == 1 }!!,description ="Jugo natural de frutilla endulzado.",nutrients="Calorías: 45, Azúcar: 10g (100ml)"),
        Product(id = 304, name = "Jugo Naranja", imageRes = R.drawable.prod_jugo_frutilla, price = 1990.0, size = "2L", category = allProductCategories.find { it.id == 105 }!!, store = allStores.find { it.id == 1 }!!,description ="Jugo natural de naranja 100% exprimido.",nutrients="Calorías: 42, Azúcar: 8g (100ml)"),
        Product(id = 305, name = "Coca-Cola En Lata", imageRes = R.drawable.prod_coca_lata_normal, price = 1200.0, size = "355ml", category = allProductCategories.find { it.id == 105 }!!, store = allStores.find { it.id == 1 }!!,description ="1 Lata de Bebida Coca Cola Original.",nutrients="Calorías: 42, Azúcar: 10.6g (100ml)"),
        Product(id = 306, name = "Pepsi en Lata", imageRes = R.drawable.prod_pepsi_lata_normal, price = 1200.0, size = "355ml", category = allProductCategories.find { it.id == 105 }!!, store = allStores.find { it.id == 1 }!!,description ="Lata de bebida Pepsi sabor original",nutrients="Calorías: 41, Azúcar: 11g (100ml)"),
        Product(id = 307, name = "Tomates", imageRes = R.drawable.prod_tomate, price = 1200.0, size = "1kg", category = allProductCategories.find { it.id == 101 }!!, store = allStores.find { it.id == 2 }!!,description ="Tomates frescos para ensalada, por kilo",nutrients="Calorías: 18, Vitamina C: 23% (100g)"),
        Product(id = 308, name = "Manzana", imageRes = R.drawable.prod_manzana, price = 1100.0, size = "1kg", category = allProductCategories.find { it.id == 101 }!!, store = allStores.find { it.id == 2 }!!,description ="Manzanas rojas (Royal Gala), por kilo.",nutrients="Calorías: 52, Fibra: 2.4g (100g)"),
        Product(id = 309, name = "Lechuga", imageRes = R.drawable.prod_lechuga_costina, price = 800.0, size = "1 unidad", category = allProductCategories.find { it.id == 101 }!!, store = allStores.find { it.id == 2 }!!,description ="Lechuga costina fresca, por unidad.",nutrients="Calorías: 15, Vitamina K: 102% (100g)")
    )
    // NOTA: Dejé category y store como TODO() porque no necesito
    // la lista completa de 'allCategories' aquí. La data principal está.


    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState = _uiState.asStateFlow()

    /**
     * Carga el producto basado en el ID y calcula el precio inicial.
     */
    fun loadProduct(productId: String?) {
        val id = productId?.toIntOrNull()
        val product = allProducts.find { it.id == id }

        if (product != null) {
            _uiState.update {
                it.copy(
                    product = product,
                    quantity = 1,
                    totalPrice = product.price, // Total inicial es el precio x 1
                    isFavorite = FavoritesRepository.isFavorite(product.id)
                )
            }
        }
    }

    /**
     * Cambia la cantidad y recalcula el total.
     */
    fun onQuantityChanged(change: Int) {
        _uiState.update { currentState ->
            val product = currentState.product ?: return
            val newQuantity = (currentState.quantity + change).coerceIn(1, 99) // Limita entre 1 y 99

            currentState.copy(
                quantity = newQuantity,
                totalPrice = product.price * newQuantity
            )
        }
    }

    /**
     * (Maqueta) Cambia el estado de favorito.
     */
    fun onToggleFavorite() {
        // ▼▼▼ ¡LÍNEAS MODIFICADAS! ▼▼▼
        val product = _uiState.value.product ?: return

        // 1. Llama al Repositorio para actualizar la lista central
        FavoritesRepository.toggleFavorite(product)

        // 2. Actualiza la UI local de esta pantalla inmediatamente
        _uiState.update { it.copy(isFavorite = !it.isFavorite) }
    }

    /**
     * Añade la cantidad seleccionada de productos al repositorio.
     */
    fun onAddToCart() {
        val product = _uiState.value.product ?: return
        val quantity = _uiState.value.quantity

        // ¡Necesitamos una nueva función en el Repositorio!
        CartRepository.addMultipleProducts(product, quantity)
    }
}
