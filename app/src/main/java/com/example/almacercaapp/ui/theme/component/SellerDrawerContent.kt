package com.example.almacercaapp.ui.theme.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.almacercaapp.navigation.Routes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.almacercaapp.ui.theme.GreenPrimary // Importa tu color

@Composable
fun SellerDrawerContent(
    sellerNavController: NavHostController, // Controlador para Seller
    parentNavController: NavHostController, // Para logout
    closeDrawer: () -> Unit
) {
    // --- Lógica para saber qué ítem está seleccionado ---
    val navBackStackEntry by sellerNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Color verde (puedes moverlo a Color.kt si quieres)
    val darkGreenColor = Color(0xFF2E7D32)

    ModalDrawerSheet(
        drawerContainerColor = darkGreenColor // <-- 1. Fondo Verde
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // --- 2. Logo y Bienvenido ---
            HeaderLogo(modifier = Modifier.size(100.dp).padding(top = 24.dp))
            Text(
                "¡Bienvenido Vendedor!",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }

        // --- 3. Items de Navegación (con nuevo estilo) ---
        SellerNavItem(
            label = "Dashboard",
            icon = Icons.Default.Dashboard,
            route = Routes.SellerDashboard.route,
            currentRoute = currentRoute,
            selectedColor = darkGreenColor, // El texto se pone verde
            unselectedColor = Color.White.copy(alpha = 0.8f), // Texto blanco
            onClick = {
                sellerNavController.navigate(Routes.SellerDashboard.route) {
                    popUpTo(sellerNavController.graph.startDestinationId) { inclusive = true }
                }
                closeDrawer()
            }
        )

        SellerNavItem(
            label = "Mis Productos",
            icon = Icons.Default.Inventory,
            route = Routes.SellerProducts.route,
            currentRoute = currentRoute,
            selectedColor = darkGreenColor,
            unselectedColor = Color.White.copy(alpha = 0.8f),
            onClick = {
                sellerNavController.navigate(Routes.SellerProducts.route)
                closeDrawer()
            }
        )

        // --- Separador ---
        Divider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp), color = Color.White.copy(alpha = 0.2f))

        // --- 4. Nuevos Items Añadidos ---
        SellerNavItem(
            label = "Datos Personales",
            icon = Icons.Default.PersonOutline,
            route = Routes.PersonalData.route,
            currentRoute = currentRoute,
            selectedColor = darkGreenColor,
            unselectedColor = Color.White.copy(alpha = 0.8f),
            onClick = {
                sellerNavController.navigate(Routes.PersonalData.route)
                closeDrawer()
            }
        )

        SellerNavItem(
            label = "Notificaciones",
            icon = Icons.Default.NotificationsNone,
            route = Routes.Notifications.route,
            currentRoute = currentRoute,
            selectedColor = darkGreenColor,
            unselectedColor = Color.White.copy(alpha = 0.8f),
            onClick = {
                sellerNavController.navigate(Routes.Notifications.route)
                closeDrawer()
            }
        )

        SellerNavItem(
            label = "FAQs",
            icon = Icons.AutoMirrored.Filled.ListAlt, // Icono de FAQs
            route = Routes.Faq.route,
            currentRoute = currentRoute,
            selectedColor = darkGreenColor,
            unselectedColor = Color.White.copy(alpha = 0.8f),
            onClick = {
                sellerNavController.navigate(Routes.Faq.route)
                closeDrawer()
            }
        )

        SellerNavItem(
            label = "Centro de Ayuda",
            icon = Icons.AutoMirrored.Filled.Help, // Icono de Ayuda
            route = Routes.HelpCenter.route,
            currentRoute = currentRoute,
            selectedColor = darkGreenColor,
            unselectedColor = Color.White.copy(alpha = 0.8f),
            onClick = {
                sellerNavController.navigate(Routes.HelpCenter.route)
                closeDrawer()
            }
        )

        // --- Separador ---
        Divider(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp), color = Color.White.copy(alpha = 0.2f))

        // --- 5. Ítem Cerrar Sesión (Color Rojo) ---
        NavigationDrawerItem(
            icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null) },
            label = { Text("Cerrar sesión") },
            selected = false,
            onClick = {
                closeDrawer()
                parentNavController.navigate(Routes.SignInMethod.route) {
                    popUpTo(0) { inclusive = true }
                }
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
            colors = NavigationDrawerItemDefaults.colors(
                unselectedContainerColor = Color.Transparent, // Fondo transparente
                unselectedIconColor = Color(0xFFE53935), // <-- Icono Rojo
                unselectedTextColor = Color(0xFFE53935)  // <-- Texto Rojo
            )
        )
    }
}


/**
 * Composable de item personalizado que imita la barra de navegación del comprador.
 * Se vuelve blanco ("pill") al estar seleccionado.
 */
@Composable
private fun SellerNavItem(
    label: String,
    icon: ImageVector,
    route: String,
    currentRoute: String?,
    selectedColor: Color,
    unselectedColor: Color,
    onClick: () -> Unit
) {
    val isSelected = currentRoute == route

    // Animamos el color del fondo (pill)
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color.Transparent,
        label = "bgColor"
    )

    // Animamos el color del contenido (icono y texto)
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) selectedColor else unselectedColor,
        label = "contentColor"
    )

    // Usamos Surface para el fondo "pill" y el click
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50), // Forma de pill
        color = backgroundColor,
        contentColor = contentColor,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp) // Padding del NavigationDrawerItem
    ) {
        // Row para el contenido (Icono + Texto)
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp), // Padding interno
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                modifier = Modifier.size(24.dp)
            )
            Spacer(Modifier.width(12.dp)) // Espacio entre icono y texto
            Text(
                text = label,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )
        }
    }
}