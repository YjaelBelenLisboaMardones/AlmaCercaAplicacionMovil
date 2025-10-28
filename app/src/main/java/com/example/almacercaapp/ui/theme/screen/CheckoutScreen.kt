package com.example.almacercaapp.ui.theme.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.almacercaapp.viewmodel.CheckoutViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    onBack: () -> Unit,
    onNavigateToProcessing: () -> Unit,
    checkoutViewModel: CheckoutViewModel = viewModel()
) {
    val cartUiState by checkoutViewModel.cartUiState.collectAsState()
    val uiState by checkoutViewModel.uiState.collectAsState()

    // --- Diálogo de Confirmación (Paso 6 del usuario) ---
    if (uiState.showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { checkoutViewModel.onDialogDismiss() },
            title = { Text("Pago procesado") },
            text = { Text("Tu pago ha sido procesado exitosamente. Prepararemos tu pedido.") },
            confirmButton = {
                Button(
                    onClick = {
                        checkoutViewModel.onDialogDismiss()
                        onNavigateToProcessing() // Navega a la pantalla de carga
                    }
                ) {
                    Text("¡Genial!")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Pantalla de pago") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            Button(
                onClick = { checkoutViewModel.onConfirmPurchase() }, // Muestra el diálogo
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp)
            ) {
                Text("Realizar la compra", fontSize = 18.sp)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()) // Permite scroll
                .padding(horizontal = 16.dp)
        ) {

            // --- 1. Sección de Retiro (Obligatoria) ---
            SectionHeader(title = "RETIRO")
            ClickableInfoRow(
                label = "Retiro en Tienda",
                value = "Gratis",
                detail = "Estándar | 10 minutos"
            )

            // --- 2. Sección de Pago (Editable) ---
            SectionHeader(title = "PAGO")
            OutlinedTextField(
                value = uiState.paymentDetails,
                onValueChange = { checkoutViewModel.onPaymentDetailsChanged(it) },
                label = { Text("Método de pago") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Ej: Visa *1234") }
            )

            // --- 3. Sección de Promociones (Editable) ---
            SectionHeader(title = "PROMOCIONES")
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = uiState.promoCode,
                    onValueChange = { checkoutViewModel.onPromoCodeChanged(it) },
                    label = { Text("Código promocional") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    isError = uiState.promoError != null
                )
                Button(
                    onClick = { checkoutViewModel.verifyPromoCode() },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Verificar")
                }
            }
            if (uiState.promoError != null) {
                Text(
                    text = uiState.promoError!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Divider()


            // --- 4. Artículos (del Carrito) ---
            SectionHeader(title = "ARTÍCULOS")
            cartUiState.items.forEach { cartItem ->
                // (Puedes usar tu 'CartItemRow' aquí si lo tienes, o este simple)
                SimpleCheckoutItem(
                    title = cartItem.product.name,
                    description = "Cantidad: ${cartItem.quantity}",
                    price = "$${(cartItem.product.price * cartItem.quantity).toInt()}"
                )
                Divider()
            }

            // --- 5. Resumen de Total ---
            Spacer(modifier = Modifier.height(24.dp))
            SummaryRow(label = "Subtotal (${cartUiState.totalItems})", value = cartUiState.formattedSubtotal)
            SummaryRow(label = "Impuestos", value = cartUiState.formattedImpuestos)
            SummaryRow(
                label = "Total",
                value = cartUiState.formattedTotal,
                isBold = true
            )
            Spacer(modifier = Modifier.height(16.dp)) // Espacio antes del botón
        }
    }
}

// --- Composables de Ayuda para CheckoutScreen ---

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelSmall,
        color = Color.Gray,
        modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
    )
}

@Composable
fun ClickableInfoRow(label: String, value: String, detail: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Opcional: abrir selector */ }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, fontWeight = FontWeight.SemiBold)
            Text(text = detail, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        Text(text = value, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
        Icon(
            Icons.Default.ArrowForwardIos,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Color.Gray
        )
    }
}

@Composable
fun SimpleCheckoutItem(title: String, description: String, price: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // (Aquí iría la Image(product.imageRes) si la tienes)
        // Image(painter = ..., modifier = Modifier.size(60.dp))
        // Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, fontWeight = FontWeight.SemiBold)
            Text(text = description, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
        Text(text = price, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

@Composable
fun SummaryRow(label: String, value: String, isBold: Boolean = false) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            fontSize = if (isBold) 18.sp else 16.sp
        )
        Text(
            text = value,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.SemiBold,
            fontSize = if (isBold) 18.sp else 16.sp
        )
    }
}