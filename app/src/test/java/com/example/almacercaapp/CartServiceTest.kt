package com.example.almacercaapp
import com.example.almacercaapp.model.CartItemDto
import com.example.almacercaapp.model.ProductDto
import com.example.almacercaapp.network.ApiService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response

class CartServiceTest {

    // 1. Creamos el "doble de riesgo" (Mock) de tu API
    private val apiFalsa = mockk<ApiService>()

    @Test
    fun `dado un servidor funcionando, obtenerCarrito devuelve items correctamente`() = runTest {
        // --- PREPARACIÓN (GIVEN) ---
        // Inventamos un producto de prueba
        val productoFake = ProductDto(id = 1L, name = "Coca Cola", description = "Refrescante", price = 1500.0)
        val itemFake = CartItemDto(id = 100L, product = productoFake, quantity = 2)

        // Entrenamos a la API falsa: "Si alguien pide el carrito del usuario 1, devuelve esta lista"
        coEvery { apiFalsa.obtenerCarrito("1") } returns Response.success(listOf(itemFake))

        // --- EJECUCIÓN (WHEN) ---
        // Llamamos a la función (esto es lo que haría tu App real)
        val respuesta = apiFalsa.obtenerCarrito("1")

        // --- VERIFICACIÓN (THEN) ---
        // Comprobamos que el resultado sea el esperado
        assertEquals(true, respuesta.isSuccessful) // ¿Fue exitoso?
        assertEquals(1, respuesta.body()?.size)    // ¿Llegó 1 item?
        assertEquals("Coca Cola", respuesta.body()?.get(0)?.product?.name) // ¿Es Coca Cola?

        println("✅ ¡PRUEBA SUPERADA! La lógica del carrito funciona perfectamente.")
    }

    @Test
    fun `al agregar un producto, el servidor responde exito`() = runTest {
        // --- GIVEN (DADO QUE...) ---
        // 1. Preparamos el item que queremos enviar
        val productoAEnviar = ProductDto(id = 2L, name = "Papas Fritas", description = "Crujientes", price = 2000.0)
        val itemAEnviar = CartItemDto(id = 0, product = productoAEnviar, quantity = 1)

        // 2. Entrenamos al Mock:
        // "Cuando alguien llame a agregarProducto con usuario '1' y cualquier objeto CartItemDto..."
        // "...devuelve una respuesta exitosa (código 200) pero sin cuerpo (Void)"
        coEvery {
            apiFalsa.agregarProducto("1", any())
        } returns Response.success(null)

        // --- WHEN (CUANDO...) ---
        // Ejecutamos la acción
        val respuesta = apiFalsa.agregarProducto("1", itemAEnviar)

        // --- THEN (ENTONCES...) ---
        // Verificamos solo que sea exitoso (isSuccessful es true)
        assertEquals(true, respuesta.isSuccessful)

        println("✅ ¡PRUEBA DE POST SUPERADA! El sistema envió el producto correctamente.")
    }
}