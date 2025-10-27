package com.example.almacercaapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.almacercaapp.ui.theme.screen.*
import com.example.almacercaapp.viewmodel.AuthViewModel

//Gestiona el flujo de alto nivel
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel //se añade el parametro
) {
    NavHost(
        navController = navController,
        startDestination = "splash" // muestra el punto de partida...
    ) {
        composable("splash") { SplashScreen(navController) }
        composable("onboarding") { OnboardingScreen(navController) }
        composable("signup") {
            // Pasa el ViewModel a SignUpScreen
            SignUpScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }
        composable("verification") {
            // Pasa el ViewModel a VerificationScreen (si lo necesita)
            // Si no lo necesita, puedes dejarlo como estaba.
            // Asumiendo que lo necesita para leer el email/teléfono o algo:
            VerificationScreen(
                navController = navController,
                viewModel = authViewModel // Ajusta si tu VerificationScreen no lo usa
            )
        }
        //composable("location") { LocationScreen(navController) }
        composable("signin") {
            // PASO 4: Pasa el ViewModel a SignInScreen
            SignInScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }
        composable("signin_method") {
            SignInMethodScreen(navController = navController) // Esta pantalla no necesita el VM
        }
        // `MainScreen` se encargará de mostrar `HomeScreen` y las otras pestañas.
        composable("main_screen") {
            // MainScreen tampoco necesita el VM directamente aquí
            MainScreen(parentNavController = navController)
        }

        // --- Estas rutas ya estaban bien movidas a HomeNavGraph ---
        // composable("personal_data") { ... }
        // composable("notifications") { ... }

        // --- Estas rutas también están bien aquí ---
        composable("details/{storeId}") { backStackEntry ->
            val storeId = backStackEntry.arguments?.getString("storeId")
            StoreDetailScreen(
                storeId = storeId,
                onBack = { navController.popBackStack() },
                parentNavController = navController
            )
        }
        composable("products/{storeId}/{categoryName}") { backStackEntry ->
            val storeId = backStackEntry.arguments?.getString("storeId")
            val categoryName = backStackEntry.arguments?.getString("categoryName")
            CategoryProductsScreen(
                storeId = storeId,
                categoryName = categoryName,
                onBack = { navController.popBackStack() }
            )
        }
    }
}