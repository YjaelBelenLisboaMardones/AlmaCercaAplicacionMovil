package com.example.almacercaapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.almacercaapp.ui.theme.screen.OnboardingScreen
import com.example.almacercaapp.ui.theme.screen.SplashScreen
import com.example.almacercaapp.ui.theme.screen.SignUpScreen
import com.example.almacercaapp.ui.theme.screen.VerificationScreen
import com.example.almacercaapp.ui.theme.screen.SignInScreen
import com.example.almacercaapp.ui.theme.screen.SignInMethodScreen
import com.example.almacercaapp.ui.theme.screen.MainScreen

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
        composable("signin") { SignInScreen(navController) }
        composable("signin_method") { SignInMethodScreen(navController) }

        // `MainScreen` se encargará de mostrar `HomeScreen` y las otras pestañas.
        composable("main_screen") {
            MainScreen(parentNavController = navController)
        }
    }


}