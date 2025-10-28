package com.example.almacercaapp.ui.theme.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.almacercaapp.model.Product
import com.example.almacercaapp.viewmodel.FavoritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    parentNavController: NavHostController, // Para navegar al detalle
    viewModel: FavoritesViewModel = viewModel()
) {
    val favoritesList by viewModel.favoriteProducts.collectAsState()
    val uiState by viewModel.uiState.collectAsState() // <-- 1. Observa el nuevo estado

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Favoritos") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            Button(
                onClick = { viewModel.addAllToCart() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp)
            ) {
                Text("Agregar todo al carrito", fontSize = 18.sp)
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            if (favoritesList.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Aún no tienes productos favoritos.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(favoritesList) { product ->
                        FavoriteItemRow(
                            product = product,
                            onClick = {
                                // Navega al detalle del producto (donde puedes editar/quitar)
                                parentNavController.navigate("product_detail/${product.id}")
                            }
                        )
                        Divider()
                    }
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter) // <-- Lo alinea abajo
                    .padding(bottom = 80.dp) // Sube por encima de la barra de navegación
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center // Asegúrate de añadir esto
            ) {
                AddedToCartNotification(
                    visible = uiState.showNotification,
                    text = uiState.notificationText,
                    onDismissed = { viewModel.notificationShown() }
                )
            }
        }
    }
}
    /**
     * Composable de ayuda para mostrar una fila de producto favorito
     * (Basado en tu imagen de diseño)
     */
    @Composable
    private fun FavoriteItemRow(
        product: Product,
        onClick: () -> Unit
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() } // <-- Hace la fila clickeable
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.name,
                modifier = Modifier
                    .size(60.dp),
                contentScale = ContentScale.Fit
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = product.size,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            Text(
                text = "$${product.price.toInt()}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )

            Icon(
                imageVector = Icons.Default.ArrowForwardIos,
                contentDescription = "Ver detalle",
                tint = Color.Gray,
                modifier = Modifier.size(16.dp)
            )
        }
    }