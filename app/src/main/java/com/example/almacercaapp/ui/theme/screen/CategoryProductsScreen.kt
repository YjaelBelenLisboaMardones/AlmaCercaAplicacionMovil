package com.example.almacercaapp.ui.theme.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.almacercaapp.model.Product
import com.example.almacercaapp.viewmodel.CartViewModel
import com.example.almacercaapp.viewmodel.CategoryProductsViewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.delay


// (El composable ProductCard y AddedToCartNotification se quedan igual, están perfectos)
@Composable
fun ProductCard(product: Product, onAddClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface) // Color de fondo blanco
    ) {
        Column(
            modifier = Modifier.padding(12.dp) // Espaciado interno
        ) {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp), // Altura fija para la imagen
                contentScale = ContentScale.Fit // 'Fit' para que se vea el producto completo
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Nombre del producto
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1, // Solo una línea
                overflow = TextOverflow.Ellipsis // Pone "..." si el texto es muy largo
            )

            // Tamaño (ej: 355ml)
            Text(
                text = product.size,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray // Color gris para el tamaño
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Fila para el Precio y el Botón
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween, // Separa los elementos
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Precio
                Text(
                    // Formatea el precio para que muestre "$1.000" en lugar de "1000.0"
                    text = "$${product.price.toInt()}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary // Color del precio
                )

                // Botón de Añadir
                IconButton(
                    onClick = onAddClick,
                    modifier = Modifier.size(36.dp), // Tamaño del botón
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color(0xFF00C853), // Un verde similar al de tu botón
                        contentColor = Color.White // Ícono blanco
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Añadir al carro"
                    )
                }
            }
        }
    }
}
@Composable
fun AddedToCartNotification(visible: Boolean, onDismissed: () -> Unit) {

    // Este efecto se dispara solo cuando 'visible' cambia a 'true'
    LaunchedEffect(key1 = visible) {
        if (visible) {
            // Espera 2 segundos
            delay(2000L)
            // Llama a la función para que el estado 'visible' vuelva a ser 'false'
            onDismissed()
        }
    }

    // Anima la aparición y desaparición de la tarjeta
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(300))
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            // Usamos un color oscuro para que contraste
            colors = CardDefaults.cardColors(
                containerColor = Color.DarkGray
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Éxito",
                    // Tinte verde para el ícono de "éxito"
                    tint = Color(0xFF00C853)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Añadido al carro",
                    color = Color.White, // Texto blanco
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/**
 * Pantalla que muestra la lista de productos de una categoría.
 * VERSIÓN FINAL CON NAVEGACIÓN DIRECTA AL CARRITO.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryProductsScreen(
    storeId: String?,
    productCategoryId: Int?,
    onBack: () -> Unit,
    parentNavController: NavHostController,
    productsViewModel: CategoryProductsViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel()
) {
    val productsUiState by productsViewModel.uiState.collectAsState()
    val cartUiState by cartViewModel.uiState.collectAsState()
    var showAddedToCartNotification by remember { mutableStateOf(false) }

    // ▼▼▼ ¡CORRECCIÓN DE LÓGICA! El LaunchedEffect ahora observa los 3 parámetros. ▼▼▼
    LaunchedEffect(key1 = storeId, key2 = productCategoryId) {
        productsViewModel.loadProducts(storeId, productCategoryId)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = productsUiState.categoryName,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                "Volver atrás"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* TODO */ }) {
                            Icon(
                                Icons.Default.Tune,
                                "Filtrar"
                            )
                        }
                    }
                )
            },
        ) { innerPadding ->
            if (productsUiState.isLoading) {
                Box(
                    Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) { CircularProgressIndicator() }
            } else if (productsUiState.products.isEmpty()) {
                Box(
                    Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) { Text("No hay productos en esta categoría.") }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 90.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(productsUiState.products) { product ->
                        ProductCard(
                            product = product,
                            onAddClick = {
                                cartViewModel.addProduct(product)
                                showAddedToCartNotification = true
                            }
                        )
                    }
                }
            }
        }

        // --- BOTÓN "VER CARRO" FLOTANTE Y DINÁMICO ---
        Button(
            onClick = {
                parentNavController.navigate("main_screen?start_destination=cart") {
                    popUpTo(parentNavController.graph.startDestinationId) { inclusive = true }
                    launchSingleTop = true
                }
            },
            modifier = Modifier.fillMaxWidth().padding(16.dp).height(56.dp).align(Alignment.BottomCenter), // <-- Ahora es válido,
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            val buttonText =
                if (cartUiState.totalItems > 0) "Ver carro (${cartUiState.totalItems})" else "Ver carro"
            Text(buttonText, color = MaterialTheme.colorScheme.onPrimary, fontSize = 18.sp)
        }

        // --- NOTIFICACIÓN PERSONALIZADA ANIMADA ---
        Box(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 80.dp)
                .padding(horizontal = 16.dp)
        ) {
            AddedToCartNotification(visible = showAddedToCartNotification) {
                showAddedToCartNotification = false
            }
        }
    }
}
