package com.example.almacercaapp.ui.theme.screen


import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold // <-- La más importante
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.* // Asegúrate de tener esta importación
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.almacercaapp.model.CartItem
import com.example.almacercaapp.viewmodel.CartViewModel
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.WindowInsets

/**
 * Esta es la pantalla que muestra el contenido del carrito de compras.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    cartViewModel: CartViewModel = viewModel() // Recibe el ViewModel compartido
) {
    val uiState by cartViewModel.uiState.collectAsState()
    Scaffold(
        // Le decimos a este Scaffold anidado que ignore los espacios (insets) de la
        // barra de estado, porque el Scaffold de MainScreen ya los maneja.
        contentWindowInsets = WindowInsets(0.dp),
        modifier = modifier, // Pasa el modifier por si acaso
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Mi carrito") })
        },
        // Tu UI de "Total" e "Ir a pagar"
        bottomBar = {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Total",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        // Formatea el precio para que muestre $0 o $1500
                        Text(
                            text = uiState.formattedTotalPrice,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Button(
                        onClick = { /* TODO: Lógica para ir a pagar */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        Text("Ir a pagar", fontSize = 18.sp)
                    }
                }
            }
        }
    ) { innerPadding ->
// --- ARREGLO 2: Añadir la línea divisoria y organizar contenido ---
        Column(
            // Aplica el padding de este Scaffold (para que el contenido no se ponga
            // detrás del topBar/bottomBar de "Total")
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 1. La línea divisoria que pediste para una separación clara
            Divider(color = Color(0xFFF0F0F0)) // Una línea gris muy clara

            // 2. El contenido (vacío o la lista)
            if (uiState.items.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(), // Llena el espacio de la Column
                    contentAlignment = Alignment.Center
                ) {
                    Text("Tu carrito está vacío")
                }
            } else {
                // Muestra la lista de productos
                LazyColumn(
                    modifier = Modifier.fillMaxSize(), // Llena el espacio de la Column
                    // El 'contentPadding' (incluyendo top=16.dp) crea el espacio
                    // que te gusta entre la línea divisoria y el primer producto.
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp) // Espacio ENTRE items
                ) {
                    items(uiState.items) { cartItem ->
                        CartItemRow(
                            cartItem = cartItem,
                            onIncrement = {
                                cartViewModel.incrementQuantity(cartItem.product.id)
                            },
                            onDecrement = {
                                cartViewModel.decrementQuantity(cartItem.product.id)
                            },
                            onRemove = {
                                cartViewModel.removeItem(cartItem.product.id)
                            }
                        )
                        // Este Divider es opcional, ya que tu diseño no lo tiene
                        // entre productos, pero lo puedes dejar si te gusta.
                        // Divider(modifier = Modifier.padding(vertical = 16.dp))
                    }
                }
            }
        }
    }
}

/**
 * Composable de ayuda para mostrar una fila de producto en el carrito.
 * (Basado en tu imagen de diseño)
 */
@Composable
private fun CartItemRow(
    cartItem: CartItem,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onRemove: () -> Unit
) {
    val product = cartItem.product
    val itemTotalPrice = product.price * cartItem.quantity

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top, // Alinea todo arriba
        horizontalArrangement = Arrangement.spacedBy(16.dp) // Espacio entre imagen e info
    ) {
        // --- 1. IMAGEN ---
        Image(
            painter = painterResource(id = product.imageRes),
            contentDescription = product.name,
            modifier = Modifier
                .size(80.dp)
                .clip(MaterialTheme.shapes.medium) // Esquinas redondeadas
                .background(Color.White) // Fondo blanco por si la imagen es transparente
                .border(1.dp, Color(0xFFF0F0F0), MaterialTheme.shapes.medium),
            contentScale = ContentScale.Fit
        )

        // --- 2. COLUMNA DE INFO (Nombre, Tamaño, Controles) ---
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = product.size,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Controles de Cantidad
            Row(verticalAlignment = Alignment.CenterVertically) {
                QuantityButton(icon = Icons.Default.Remove, onClick = onDecrement)
                Text(
                    text = cartItem.quantity.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                QuantityButton(icon = Icons.Default.Add, onClick = onIncrement)
            }
        }

        // --- 3. COLUMNA DE ACCIONES (Quitar, Precio) ---
        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.SpaceBetween, // Pone el 'X' arriba y el precio abajo
            modifier = Modifier.height(80.dp) // Iguala la altura de la imagen
        ) {
            IconButton(onClick = onRemove, modifier = Modifier.size(24.dp)) {
                Icon(Icons.Default.Close, "Quitar", tint = Color.Gray)
            }
            Text(
                text = "$${itemTotalPrice.toInt()}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Composable de ayuda para los botones '+' y '-'
 */
@Composable
private fun QuantityButton(
    icon: ImageVector,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier
            .size(32.dp)
            .clip(CircleShape)
            .border(1.dp, Color(0xFFE0E0E0), CircleShape) // Borde gris claro
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary // Color verde del tema
        )
    }
}