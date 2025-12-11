package com.example.almacercaapp.ui.theme.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.almacercaapp.R
import com.example.almacercaapp.model.ProductDto
import com.example.almacercaapp.viewmodel.FavoritesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    parentNavController: NavHostController,
    viewModel: FavoritesViewModel = viewModel() // Asumimos que se inyectará correctamente
) {
    // 1. Observa los nuevos estados desde el ViewModel
    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Favoritos") },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            // 2. Muestra un indicador de carga mientras los datos están en camino
            if (isLoading) {
                CircularProgressIndicator()
            } 
            // 3. Muestra un mensaje si, después de cargar, la lista está vacía
            else if (products.isEmpty()) {
                Text("Aún no tienes productos favoritos.")
            } 
            // 4. Muestra la lista de productos que vienen de la nube
            else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(products) { product ->
                        FavoriteItemRow(
                            product = product,
                            onClick = {
                                parentNavController.navigate("product_detail/${product.id}")
                            }
                        )
                        Divider()
                    }
                }
            }
        }
    }
}

/**
 * Composable de ayuda que AHORA USA ProductDto y carga imágenes desde la nube.
 */
@Composable
private fun FavoriteItemRow(
    product: ProductDto, // <-- Acepta el modelo de la nube
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Usa Coil (AsyncImage) para cargar la imagen desde la URL
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(product.imageUrl)
                .crossfade(true)
                .error(R.drawable.placeholder_image) // Muestra esto si la imagen falla
                .build(),
            contentDescription = product.name,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = product.description.take(40) + "...", // Muestra una breve descripción
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                maxLines = 1
            )
        }

        Text(
            text = String.format("$%.0f", product.price),
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
