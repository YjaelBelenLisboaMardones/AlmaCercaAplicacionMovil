package com.example.almacercaapp.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun SellerDashboardScreen(navController: NavHostController) { // Recibe NavController
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()), // Permite scroll si hay mucho contenido
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Resumen de Ventas",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        // --- Tarjetas de Estadísticas (Placeholders) ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly // Espacia las tarjetas
        ) {
            StatisticCard("Ventas Hoy", "$0") // Placeholder
            StatisticCard("Ventas Semana", "$0") // Placeholder
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatisticCard("Pedidos Nuevos", "0") // Placeholder
            StatisticCard("Productos Activos", "0") // Placeholder
        }

        Spacer(modifier = Modifier.height(32.dp))


        Button(onClick = { navController.navigate(com.example.almacercaapp.navigation.Routes.SellerProducts.route) }) {
            Text("Ver Mis Productos")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(onClick = { /* TODO: Navegar a pantalla de agregar producto */ }) {
            Text("Agregar Nuevo Producto")
        }


    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.size(width = 150.dp, height = 100.dp), // Tamaño fijo para las tarjetas
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize() // Ocupa todo el espacio de la Card
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Centra el contenido
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant // Color secundario para el título
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}