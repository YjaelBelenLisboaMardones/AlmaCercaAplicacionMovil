package com.example.almacercaapp.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.almacercaapp.ui.theme.screen.CategoryProductsScreen
import com.example.almacercaapp.ui.theme.screen.MainScreen
import com.example.almacercaapp.ui.theme.screen.OnboardingScreen
import com.example.almacercaapp.ui.theme.screen.SignInMethodScreen
import com.example.almacercaapp.ui.theme.screen.SignInScreen
import com.example.almacercaapp.ui.theme.screen.SignUpScreen
import com.example.almacercaapp.ui.theme.screen.SplashScreen
import com.example.almacercaapp.ui.theme.screen.StoreDetailScreen
import com.example.almacercaapp.ui.theme.screen.VerificationScreen

//Gestiona el flujo de alto nivel
@Composable
fun NavGraph(navController: NavHostController) {

    val fadeAnimation = tween<Float>(400) // Duración de la animación

    NavHost(
        navController = navController,
        startDestination = "splash",

        // Parámetros de animación globales para todo el NavHost
        enterTransition = { fadeIn(animationSpec = fadeAnimation) },
        exitTransition = { fadeOut(animationSpec = fadeAnimation) },
        popEnterTransition = { fadeIn(animationSpec = fadeAnimation) },
        popExitTransition = { fadeOut(animationSpec = fadeAnimation) }
    ) {
        // Ahora el bloque de contenido solo tiene los 'composable'

        composable("splash") { SplashScreen(navController) }
        composable("onboarding") { OnboardingScreen(navController) }
        composable("signup") { SignUpScreen(navController) }
        composable("verification") { VerificationScreen(navController) }
        composable("signin") { SignInScreen(navController) }
        composable("signin_method") { SignInMethodScreen(navController) }

        // Hacemos que la ruta 'main_screen' acepte un argumento opcional 'start_destination'
        composable(
            route = "main_screen?start_destination={start_destination}",
            arguments = listOf(navArgument("start_destination") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            // Leemos el argumento que podría venir en la URL
            val startDestination = backStackEntry.arguments?.getString("start_destination")
            // Se lo pasamos a MainScreen para que sepa en qué pestaña empezar
            MainScreen(
                parentNavController = navController,
                startDestination = startDestination
            )
        }
        // ▲▲▲ FIN DE LA MODIFICACIÓN ▲▲▲

        composable("details/{storeId}") { backStackEntry ->
            val storeId = backStackEntry.arguments?.getString("storeId")
            StoreDetailScreen(
                storeId = storeId,
                onBack = { navController.popBackStack() },
                parentNavController = navController
            )
        }

        composable(
            route = "products/{storeId}/{productCategoryId}",
            arguments = listOf(
                navArgument("storeId") { type = NavType.StringType },
                navArgument("productCategoryId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            // Extraemos los 2 argumentos de la ruta.
            val storeId = backStackEntry.arguments?.getString("storeId")
            val productCategoryId = backStackEntry.arguments?.getInt("productCategoryId")

            // La llamada a CategoryProductsScreen ahora es válida porque
            // ya NO le pasamos el 'categoryName' que no necesita.
            CategoryProductsScreen(
                storeId = storeId,
                productCategoryId = productCategoryId,
                onBack = { navController.popBackStack() },
                parentNavController = navController
            )
        }
        // ▲▲▲ FIN DE LA CORRECCIÓN ▲▲▲
    }
}