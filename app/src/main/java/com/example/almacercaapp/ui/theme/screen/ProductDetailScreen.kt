package com.example.almacercaapp.ui.theme.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.almacercaapp.viewmodel.ProductDetailViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: String?,
    onBack: () -> Unit,
    onGoToCart: () -> Unit,
    viewModel: ProductDetailViewModel = viewModel()
) {
    // Carga el producto cuando la pantalla aparece
    LaunchedEffect(key1 = productId) {
        viewModel.loadProduct(productId)
    }

    val uiState by viewModel.uiState.collectAsState()
    val product = uiState.product

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Detalle del Producto", fontSize = 16.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Lógica de compartir */ }) {
                        Icon(Icons.Default.Share, "Compartir")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFF5F5F5) // Color de fondo gris claro
                )
            )
        },
        bottomBar = {
            // Botón "Agregar al carro"
            Button(
                onClick = {
                    viewModel.onAddToCart()
                    onGoToCart() // Navega al carrito después de añadir
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp)
            ) {
                Text(
                    text = "Agregar al carro(${uiState.quantity})",
                    fontSize = 18.sp
                )
            }
        }
    ) { innerPadding ->
        if (product == null) {
            Box(Modifier.fillMaxSize().padding(innerPadding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            // Contenido principal
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                // --- Sección de Imagen y Favorito ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(Color(0xFFF5F5F5)) // Fondo gris claro
                ) {
                    // ▼▼▼ ¡CORRECCIÓN 1! Se usa AsyncImage para cargar desde URL ▼▼▼
                    AsyncImage(
                        model = product.imageUrl,
                        contentDescription = product.name,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp), // Padding para que no toque los bordes
                        contentScale = ContentScale.Fit
                    )
                    // Botón de Favorito
                    IconButton(
                        onClick = { viewModel.onToggleFavorite() },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    ) {
                        Icon(
                            imageVector = if (uiState.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorito",
                            tint = if (uiState.isFavorite) Color.Red else Color.Gray
                        )
                    }
                }

                // --- Sección de Nombre y Cantidad ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = product.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        // ▼▼▼ ¡CORRECCIÓN 2! Se eliminó la referencia a `product.size` y se muestra el stock ▼▼▼
                        Text(
                            text = "Stock disponible: ${product.stock}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                    // Selector de Cantidad
                    QuantitySelector(
                        quantity = uiState.quantity,
                        onIncrease = { viewModel.onQuantityChanged(1) },
                        onDecrease = { viewModel.onQuantityChanged(-1) }
                    )
                }

                // --- Sección de Total ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = uiState.formattedTotalPrice,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Divider(modifier = Modifier.padding(16.dp))

                // --- Sección de Descripción ---
                ExpandableInfoRow(
                    title = "Descripción del Producto",
                    content = product.description
                )

                Divider(modifier = Modifier.padding(horizontal = 16.dp))

                // --- Sección de Nutrientes (Maqueta) ---
                ClickableInfoRow(
                    title = "Nutrientes",
                    tag = "100gr",
                    onClick = { /* TODO: Navegar a pantalla de nutrientes */ }
                )

                Divider(modifier = Modifier.padding(horizontal = 16.dp))

                Spacer(modifier = Modifier.height(16.dp)) // Espacio antes del botón
            }
        }
    }
}

// --- Composables de Ayuda para ProductDetailScreen ---

@Composable
fun QuantitySelector(
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        IconButton(onClick = onDecrease, enabled = quantity > 1) {
            Icon(Icons.Default.Remove, "Quitar")
        }
        Text(
            text = quantity.toString(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        IconButton(onClick = onIncrease) {
            Icon(Icons.Default.Add, "Añadir")
        }
    }
}

@Composable
fun ExpandableInfoRow(
    title: String,
    content: String
) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded }
            .padding(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = title, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            Icon(
                imageVector = Icons.Default.ArrowForwardIos, // (Debería rotar, pero esto es más rápido)
                contentDescription = if (isExpanded) "Colapsar" else "Expandir",
                modifier = Modifier.size(16.dp)
            )
        }
        AnimatedVisibility(visible = isExpanded) {
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun ClickableInfoRow(
    title: String,
    tag: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = title, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        Text(
            text = tag,
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier
                .background(Color(0xFFF0F0F0), CircleShape)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
        Icon(
            imageVector = Icons.Default.ArrowForwardIos,
            contentDescription = "Ver $title",
            modifier = Modifier
                .size(16.dp)
                .padding(start = 8.dp),
            tint = Color.Gray
        )
    }
}
