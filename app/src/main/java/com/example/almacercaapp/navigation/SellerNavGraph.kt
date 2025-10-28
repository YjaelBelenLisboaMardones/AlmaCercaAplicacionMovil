
package com.example.almacercaapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


@Composable
fun SellerNavGraph(
    navController: NavHostController,
    parentNavController: NavHostController, // Recíbelo si alguna pantalla interna lo necesita
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.SellerDashboard.route, // Empieza en el Dashboard
        modifier = modifier
    ) {
        composable(Routes.SellerDashboard.route) {
            // SellerDashboardScreen(navController)
            androidx.compose.material3.Text("TODO: Seller Dashboard") // Placeholder inicial
        }
        composable(Routes.SellerProducts.route) {
            // SellerProductsScreen(navController)
            androidx.compose.material3.Text("TODO: Seller Products") // Placeholder inicial
        }

    }
}