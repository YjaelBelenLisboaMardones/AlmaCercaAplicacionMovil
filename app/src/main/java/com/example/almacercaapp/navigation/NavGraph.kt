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
import com.example.almacercaapp.ui.theme.screen.LocationScreen




@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {
        composable(Routes.Splash.route) { SplashScreen(navController) }
        composable(Routes.Onboarding.route) { OnboardingScreen(navController) }
        composable(Routes.SignInMethod.route) { SignInMethodScreen(navController) }
        composable(Routes.SignIn.route) { SignInScreen(navController) }
        composable(Routes.SignUp.route) { SignUpScreen(navController) }
        composable(Routes.Verification.route) { VerificationScreen(navController) }
        composable(Routes.Location.route) { LocationScreen(navController) }
    }
}