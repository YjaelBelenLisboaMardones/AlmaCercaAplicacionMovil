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




@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") { SplashScreen(navController) }
        composable("onboarding") { OnboardingScreen(navController) }
        composable("signup") { SignUpScreen(navController) }
        composable("verification") { VerificationScreen(navController) }
        composable("signin") { SignInScreen(navController) }
        composable("signin_method") { SignInMethodScreen(navController) }



    }
}