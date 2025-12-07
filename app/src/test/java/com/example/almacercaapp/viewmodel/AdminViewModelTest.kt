package com.example.almacercaapp.viewmodel

import com.example.almacercaapp.data.repository.ProductRepository
import com.example.almacercaapp.model.ProductCategory
import com.example.almacercaapp.model.ProductDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class AdminViewModelTest {

    // Regla para manejar las corrutinas en los tests
    private val testDispatcher = UnconfinedTestDispatcher()

    // Mocks y SUT (System Under Test)
    private lateinit var viewModel: AdminViewModel
    private val mockProductRepository: ProductRepository = mock()

    // Datos de prueba
    private val testCategory = ProductCategory(id = 101, name = "Vegetales y Frutas", imageRes = 0, storeId = 0)
    private val newProduct = ProductDto(
        id = null,
        name = "Manzana",
        description = "Roja y jugosa",
        price = 1.5,
        stock = 100,
        imageUrl = "http://example.com/manzana.jpg",
        storeId = "TIENDA_ADMIN_PRINCIPAL",
        categoryId = "101"
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher) // Usar el dispatcher de prueba
        viewModel = AdminViewModel(mockProductRepository)
        viewModel.selectedCategory.value = testCategory
        viewModel.productName.value = newProduct.name
        viewModel.productDescription.value = newProduct.description
        viewModel.productPrice.value = newProduct.price.toString()
        viewModel.productStock.value = newProduct.stock.toString()
        viewModel.productImageUrl.value = newProduct.imageUrl
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain() // Limpiar el dispatcher
    }

    @Test
    fun `onSaveProduct WHEN creating a new product successfully THEN repository is called and status is updated`() = runTest {
        // Given (Dado que)
        val createdProduct = newProduct.copy(id = "prod-123")
        whenever(mockProductRepository.createProduct(newProduct)).thenReturn(Result.success(createdProduct))
        whenever(mockProductRepository.getAllProducts()).thenReturn(Result.success(listOf(createdProduct)))

        // When (Cuando)
        viewModel.onSaveProduct()

        // Then (Entonces)
        verify(mockProductRepository).createProduct(newProduct) // Verifica que se llamó a crear el producto
        verify(mockProductRepository).getAllProducts() // Verifica que se recargó la lista
        assertEquals("¡Producto creado con éxito!", viewModel.uiStatus.value)
        assertEquals(1, viewModel.products.value.size)
        assertEquals("Manzana", viewModel.products.value.first().name)
    }

    private fun TestScope.whenever(createProduct: Result<ProductDto>) {}

    @Test
    fun `deleteProduct WHEN product exists THEN repository is called and product list is updated`() = runTest {
        // Given (Dado que)
        val productToDelete = newProduct.copy(id = "prod-123")
        viewModel.products.value = listOf(productToDelete) // Simula que el producto ya existe en la lista
        whenever(mockProductRepository.deleteProduct(productToDelete.id!!)).thenReturn(Result.success(Unit))

        // When (Cuando)
        viewModel.deleteProduct(productToDelete)

        // Then (Entonces)
        verify(mockProductRepository).deleteProduct(productToDelete.id!!) // Verifica que se llamó a eliminar
        assertEquals("Producto eliminado.", viewModel.uiStatus.value)
        assertEquals(0, viewModel.products.value.size) // La lista de productos debe estar vacía
    }
}
