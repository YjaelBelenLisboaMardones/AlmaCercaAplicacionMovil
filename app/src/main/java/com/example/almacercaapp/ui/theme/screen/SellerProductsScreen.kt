package com.example.almacercaapp.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items // Importa items para LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add // Importa el icono '+'
import androidx.compose.material.icons.filled.Delete // Importa icono Delete
import androidx.compose.material.icons.filled.Edit // Importa icono Edit
import androidx.compose.material3.*
import androidx.compose.runtime.* // Importa Composable y otros necesarios
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.almacercaapp.navigation.Routes // Importa tus rutas
import com.example.almacercaapp.ui.theme.GreenPrimary // Importa tu color verde

// Data class temporal para representar un producto en la lista (reemplazar con ProductEntity luego)
data class TempProduct(val id: Int, val name: String, val price: Double)

@OptIn(ExperimentalMaterial3Api::class) // Necesario para Scaffold y FAB
@Composable
fun SellerProductsScreen(navController: NavHostController) { // Recibe NavController

    // Lista temporal de productos (reemplazar con datos reales del ViewModel más adelante)
    val products = remember { mutableStateListOf<TempProduct>() }

    LaunchedEffect(Unit) {
        if (products.isEmpty()) { // Solo añade si la lista está vacía
            products.addAll(listOf(
                TempProduct(1, "Producto Ejemplo 1", 1000.0),
                TempProduct(2, "Producto Ejemplo 2", 2500.0)
            ))
        }
    }

    Scaffold(

        floatingActionButton = {
            FloatingActionButton(
                onClick = {

                    navController.navigate(Routes.SellerAddEditProduct.addRoute())
                },
                containerColor = GreenPrimary // Color del FAB
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Agregar Producto", tint = MaterialTheme.colorScheme.onPrimary) // Icono '+'
            }
        },
        floatingActionButtonPosition = FabPosition.End // Posición del FAB

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // Aplica el padding del Scaffold
                .padding(16.dp) // Añade padding adicional
        ) {
            Text(
                "Mis Productos",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )


            if (products.isEmpty()) {
                // Mensaje si no hay productos
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Aún no tienes productos. ¡Agrega uno con el botón +!")
                }
            } else {
                // Muestra la lista si hay productos
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp) // Espacio entre items
                ) {
                    items(products) { product ->
                        ProductListItem(
                            product = product,
                            onEditClick = {
                                // Navega a editar, pasando el ID del producto
                                navController.navigate(Routes.SellerAddEditProduct.editRoute(product.id.toLong()))
                            },
                            onDeleteClick = {
                                // TODO: Añadir lógica para confirmar y eliminar el producto (usando ViewModel)
                            }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Composable para mostrar un ítem de producto en la lista.
 * Incluye botones para editar y eliminar.
 */
@Composable
fun ProductListItem(
    product: TempProduct,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween // Separa el contenido
        ) {
            Column(modifier = Modifier.weight(1f)) { // Columna para nombre y precio
                Text(product.name, fontWeight = FontWeight.Medium)
                // Formatea el precio para mostrarlo correctamente
                Text(
                    "CLP ${"%,.0f".format(product.price).replace(',', '.')}", // Formato de moneda Chilena
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Row { // Fila para los botones de acción
                IconButton(onClick = onEditClick) { // Llama a la acción de editar
                    Icon(Icons.Filled.Edit, contentDescription = "Editar")
                }
                IconButton(onClick = onDeleteClick) { // Llama a la acción de eliminar
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                }
            }
        }
    }
}