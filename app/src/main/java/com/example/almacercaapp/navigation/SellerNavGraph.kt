package com.example.almacercaapp.navigation

import android.os.Build // <-- AÑADE ESTA IMPORTACIÓN
import androidx.annotation.RequiresApi // <-- AÑADE ESTA IMPORTACIÓN
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.almacercaapp.ui.theme.screen.SellerDashboardScreen
import com.example.almacercaapp.ui.theme.screen.SellerProductsScreen
import com.example.almacercaapp.ui.theme.screen.AddEditProductScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
// --- AÑADE TODAS ESTAS IMPORTACIONES ---
import com.example.almacercaapp.ui.theme.screen.FaqScreen
import com.example.almacercaapp.ui.theme.screen.HelpCenterScreen
import com.example.almacercaapp.ui.theme.screen.NotificationsScreen
import com.example.almacercaapp.ui.theme.screen.PersonalDataScreen
import com.example.almacercaapp.ui.theme.screen.SupportChatScreen
// ----------------------------------------

@RequiresApi(Build.VERSION_CODES.O) // <-- AÑADE ESTO (requerido por PersonalDataScreen)
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
            SellerDashboardScreen(navController = navController)
        }
        composable(Routes.SellerProducts.route) {
            SellerProductsScreen(navController = navController)
        }

        composable(
            route = Routes.SellerAddEditProduct.route,
            arguments = listOf(navArgument("productId") { // Define el argumento
                type = NavType.StringType // Usamos String para permitir nulo fácilmente
                nullable = true
                defaultValue = null // Valor por defecto si no se pasa (añadir)
            })
        ) { backStackEntry ->
            // Extrae el productId (puede ser null)
            val productIdString = backStackEntry.arguments?.getString("productId")
            val productId = productIdString?.toLongOrNull() // Convierte a Long o null

            AddEditProductScreen(
                navController = navController,
                productId = productId // Pasa el ID (o null) a la pantalla
            )
        }

        // --- RUTAS NUEVAS AÑADIDAS PARA EL MENÚ ---
        composable(Routes.PersonalData.route) {
            PersonalDataScreen(navController = navController)
        }
        composable(Routes.Notifications.route) {
            NotificationsScreen(navController = navController)
        }
        composable(Routes.Faq.route) {
            FaqScreen(navController = navController)
        }
        composable(Routes.HelpCenter.route) {
            HelpCenterScreen(navController = navController)
        }
        composable(Routes.Support.route) {
            SupportChatScreen(navController = navController)
        }
        // ------------------------------------------
    }
}