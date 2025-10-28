package com.example.almacercaapp.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.almacercaapp.ui.theme.GreenPrimary // <-- 1. ASEGÚRATE DE IMPORTAR EL COLOR

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

        // --- Tarjetas de Estadísticas (Sin cambios) ---
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

        // --- BOTÓN MODIFICADO ---
        Button(
            onClick = { navController.navigate(com.example.almacercaapp.navigation.Routes.SellerProducts.route) },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            // --- 2. AÑADE ESTAS LÍNEAS PARA EL COLOR ---
            colors = ButtonDefaults.buttonColors(
                containerColor = GreenPrimary // <-- Este es el color verde
            )
            // ------------------------------------------
        ) {
            Text("Ver Mis Productos") // El texto será blanco automáticamente
        }

        // --- NUEVA SECCIÓN DE ACTIVIDAD RECIENTE ---
        Spacer(modifier = Modifier.height(32.dp))

        Text(
            "Actividad Reciente",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Placeholder para la actividad reciente
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.History,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    "Aún no hay actividad",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    "Aquí se mostrarán los productos que has agregado, modificado o eliminado.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
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