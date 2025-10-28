package com.example.almacercaapp.ui.theme.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.almacercaapp.navigation.Routes

@Composable
fun SellerDrawerContent(
    sellerNavController: NavHostController, // Controlador para Seller
    parentNavController: NavHostController, // Para logout
    closeDrawer: () -> Unit
) {
    ModalDrawerSheet {
        Spacer(Modifier.height(12.dp))
        // Ítem Dashboard
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Dashboard, contentDescription = null) },
            label = { Text("Dashboard") },
            selected = false, // Puedes marcar la ruta actual si quieres
            onClick = {
                sellerNavController.navigate(Routes.SellerDashboard.route) { popUpTo(sellerNavController.graph.startDestinationId){inclusive = true} }
                closeDrawer()
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        // Ítem Mis Productos
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.Inventory, contentDescription = null) },
            label = { Text("Mis Productos") },
            selected = false,
            onClick = {
                sellerNavController.navigate(Routes.SellerProducts.route)
                // Decide si quieres limpiar la pila o no al ir a productos
                closeDrawer()
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        // Ítem Agregar Producto (Puedes navegar a una pantalla específica o mostrar diálogo)
        NavigationDrawerItem(
            icon = { Icon(Icons.Default.AddBusiness, contentDescription = null) },
            label = { Text("Agregar Producto") },
            selected = false,
            onClick = {
                // sellerNavController.navigate(Routes.SellerAddEditProduct.route) // Si tienes pantalla
                // O simplemente cierra el drawer y manejas la lógica en SellerProductsScreen
                closeDrawer()
            },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
        // Separador
        Divider(modifier = Modifier.padding(vertical = 12.dp))
        // Ítem Cerrar Sesión
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
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}