package com.example.almacercaapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.almacercaapp.data.local.user.UserRole
import com.example.almacercaapp.ui.theme.screen.*
import com.example.almacercaapp.viewmodel.AuthViewModel

//Gestiona el flujo de alto nivel
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel // Recibe el ViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route // Usamos la ruta definida en Routes
    ) {

        composable(Routes.Splash.route) { SplashScreen(navController) }
        composable(Routes.Onboarding.route) { OnboardingScreen(navController) } // Navega a RoleSelection


        composable(Routes.RoleSelection.route) {
            RoleSelectionScreen(navController)
        }

        composable(
            route = Routes.SignUp.route, // Usa la ruta "signup/{userRole}"
            arguments = listOf(navArgument("userRole") { type = NavType.EnumType(UserRole::class.java) }) // Define el argumento
        ) { backStackEntry ->
            // Extrae el rol de la ruta, con fallback seguro a BUYER
            val role = backStackEntry.arguments?.getSerializable("userRole") as? UserRole ?: UserRole.BUYER
            SignUpScreen(
                navController = navController,
                viewModel = authViewModel,
                selectedRole = role // Pasa el rol extraído
            )
        }


        // --- Pantalla Verificación ---
        composable(Routes.Verification.route) {
            VerificationScreen(
                navController = navController,
                viewModel = authViewModel // Pasa el ViewModel
            )
        }



        composable(Routes.Location.route) {
            LocationScreen(/* navController = navController */)
        }



        composable(Routes.SignIn.route) {
            SignInScreen(
                navController = navController,
                viewModel = authViewModel // Pasa el ViewModel
            )
        }
        composable(Routes.SignInMethod.route) {
            SignInMethodScreen(navController = navController)
        }



        composable("main_screen") { // Pantalla principal del Comprador
            MainScreen(parentNavController = navController)
        }
        composable(Routes.SellerMain.route) { // Pantalla principal del Vendedor
            SellerMainScreen(parentNavController = navController)
        }



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