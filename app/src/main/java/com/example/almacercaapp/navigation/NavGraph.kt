package com.example.almacercaapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.almacercaapp.data.local.user.UserRole
import com.example.almacercaapp.ui.theme.screen.*
import com.example.almacercaapp.viewmodel.AuthViewModel

// Gestiona el flujo de alto nivel
@RequiresApi(Build.VERSION_CODES.O)
@Composable // <-- ¡LA ETIQUETA ESTÁ EN LA ÚNICA FUNCIÓN!
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel // (Viene de tu compañero)
) {

    val fadeAnimation = tween<Float>(400) // (Viene de tu código)

    // Ahora NavHost SÍ es llamado por una función @Composable
    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route, // (Viene de tu compañero)
        // (Tus animaciones)
        enterTransition = { fadeIn(animationSpec = fadeAnimation) },
        exitTransition = { fadeOut(animationSpec = fadeAnimation) },
        popEnterTransition = { fadeIn(animationSpec = fadeAnimation) },
        popExitTransition = { fadeOut(animationSpec = fadeAnimation) }
    ) {

        // --- RUTAS DE TU COMPAÑERO (Login, Onboarding, etc.) ---

        // Pantalla de carga inicial (Splash)
        composable(Routes.Splash.route) { SplashScreen(navController) }

        // Pantalla de bienvenida e introducción (Onboarding)
        composable(Routes.Onboarding.route) { OnboardingScreen(navController) }

        // Pantalla para seleccionar el rol (Comprador o Vendedor)
        composable(Routes.RoleSelection.route) { RoleSelectionScreen(navController) }

        // Pantalla de registro de nuevo usuario (recibe un rol)
        composable(
            route = Routes.SignUp.route,
            arguments = listOf(navArgument("userRole") { type = NavType.EnumType(UserRole::class.java) })
        ) { backStackEntry ->
            val role = backStackEntry.arguments?.getSerializable("userRole") as? UserRole ?: UserRole.BUYER
            SignUpScreen(
                navController = navController,
                viewModel = authViewModel,
                selectedRole = role
            )
        }

        // Pantalla de verificación de código (ej. email o SMS)
        composable(Routes.Verification.route) {
            VerificationScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }

        // Pantalla para configurar la ubicación
        composable(Routes.Location.route) { LocationScreen() }

        // Pantalla de inicio de sesión (Login)
        composable(Routes.SignIn.route) {
            SignInScreen(
                navController = navController,
                viewModel = authViewModel
            )
        }

        // Pantalla para elegir otros métodos de inicio de sesión
        composable(Routes.SignInMethod.route) {
            SignInMethodScreen(navController = navController)
        }

        // Pantalla principal para el rol de Vendedor
        composable(Routes.SellerMain.route) {
            SellerMainScreen(parentNavController = navController)
        }

        // --- FIN DE LAS RUTAS DE TU COMPAÑERO ---


        // --- TUS RUTAS (Y LAS DE ÉL) COMBINADAS ---

        // (Tu ruta 'main_screen' con el argumento 'start_destination')
        composable(
            route = "main_screen?start_destination={start_destination}",
            arguments = listOf(navArgument("start_destination") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val startDestination = backStackEntry.arguments?.getString("start_destination")
            MainScreen(
                parentNavController = navController,
                startDestination = startDestination
            )
        }

        // (Ruta 'details' que ambos tenían)
        composable("details/{storeId}") { backStackEntry ->
            val storeId = backStackEntry.arguments?.getString("storeId")
            StoreDetailScreen(
                storeId = storeId,
                onBack = { navController.popBackStack() },
                parentNavController = navController
            )
        }

        // (Tu ruta 'products' con 'productCategoryId' (Int) en lugar de 'categoryName')
        composable(
            route = "products/{storeId}/{productCategoryId}",
            arguments = listOf(
                navArgument("storeId") { type = NavType.StringType },
                navArgument("productCategoryId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val storeId = backStackEntry.arguments?.getString("storeId")
            val productCategoryId = backStackEntry.arguments?.getInt("productCategoryId")
            CategoryProductsScreen(
                storeId = storeId,
                productCategoryId = productCategoryId,
                onBack = { navController.popBackStack() },
                parentNavController = navController
            )
        }

        // (Tu nueva ruta 'product_detail')
        composable(
            route = "product_detail/{productId}",
            arguments = listOf(navArgument("productId") { type = NavType.StringType })
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")
            ProductDetailScreen(
                productId = productId,
                onBack = { navController.popBackStack() },
                onGoToCart = {
                    navController.navigate("main_screen?start_destination=cart") {
                        popUpTo(navController.graph.startDestinationId)
                    }
                }
            )
        }

        // (Tus rutas de checkout)
        composable(
            route = "checkout",
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }) }
        ) {
            CheckoutScreen(
                onBack = { navController.popBackStack() },
                onNavigateToProcessing = {
                    navController.navigate("processing_order") {
                        popUpTo("home") { inclusive = false }
                    }
                }
            )
        }

        composable("processing_order") {
            ProcessingScreen(
                onOrderProcessed = {
                    navController.navigate("order_ready") {
                        popUpTo("home") { inclusive = false }
                    }
                }
            )
        }

        composable("order_ready") {
            OrderReadyScreen(
                onGoToHome = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
    }
}