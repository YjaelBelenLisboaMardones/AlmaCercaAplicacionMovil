package com.example.almacercaapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.almacercaapp.ui.theme.screen.*




//Gestiona el flujo de alto nivel
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "splash" // muestra el punto de partida...
    ) {
        composable("splash") { SplashScreen(navController) } // si le pedimos ir a la vista splash nos muestra la SplashScreen
        composable("onboarding") { OnboardingScreen(navController) }
        composable("signup") { SignUpScreen(navController) }
        composable("verification") { VerificationScreen(navController) }
        //composable("location") { LocationScreen(navController) }
        composable("signin") { SignInScreen(navController) }
        composable("signin_method") { SignInMethodScreen(navController) }
        // `MainScreen` se encargará de mostrar `HomeScreen` y las otras pestañas.
        composable("main_screen") { MainScreen(parentNavController = navController) }
        composable("profile") { ProfileScreen(navController) }
        composable("faq") { FaqScreen(navController = navController) }
        composable("help_center") { HelpCenterScreen(navController = navController) }
        composable("support_chat") { SupportChatScreen(navController = navController) }


        composable("personal_data") {
            PersonalDataScreen(navController)
        }
        composable("notifications") {
            NotificationsScreen(navController)
        }

        // ruta para la pantalla store_details
        composable("details/{storeId}") { backStackEntry ->
            // Extraemos el ID de la tienda desde los argumentos de la ruta
            val storeId = backStackEntry.arguments?.getString("storeId")

            // Llamamos al Composable de la pantalla de detalles,
            // pasándole el ID que hemos recibido.
            StoreDetailScreen(
                storeId = storeId,
                // Le pasamos una función para que el botón de "atrás" en la UI funcione
                onBack = { navController.popBackStack() }, // Acción para volver atrás
                parentNavController = navController // Le pasas el NavController de este NavGraph
            )
        }
        //pantalla de lista de productos
        composable("products/{storeId}/{categoryName}") { backStackEntry ->
            // Extraemos los argumentos de la ruta
            val storeId = backStackEntry.arguments?.getString("storeId")
            val categoryName = backStackEntry.arguments?.getString("categoryName")

            CategoryProductsScreen(
                storeId = storeId,
                categoryName = categoryName,
                onBack = { navController.popBackStack() } // Acción para volver atrás
            )
        }

        composable("logout") {
            LogoutScreen(navController) {
                // Aquí haces el proceso de logout real (limpiar datos, etc.)
                navController.navigate("login") {
                    popUpTo("profile") { inclusive = true }
                }
            }
        }

    }
}