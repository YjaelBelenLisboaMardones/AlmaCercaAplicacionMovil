package com.example.almacercaapp.ui.theme.screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

// --- Maqueta temporal para que NavGraph compile ---

@Composable
fun CheckoutScreen(
    onBack: () -> Unit,
    onNavigateToProcessing: () -> Unit
    // (Luego añadiremos el ViewModel)
) {
    Text("Pantalla de Checkout (Temporal)")
}