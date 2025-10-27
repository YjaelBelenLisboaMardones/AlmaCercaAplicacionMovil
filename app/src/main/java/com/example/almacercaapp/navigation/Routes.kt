package com.example.almacercaapp.navigation

import androidx.compose.material3.Text
//archivo que se asegura que las rutas esten bien escritas
sealed class Routes(val route: String) {
    object Splash : Routes("splash")
    object Onboarding : Routes("onboarding")
    object SignUp : Routes("signup")
    object Verification : Routes("verification")
    object Location : Routes("location")
    object SignIn : Routes("signin")
    object SignInMethod : Routes("signin_method")
    object Home : Routes("home")
    object Explore : Routes("explore")
    object Cart : Routes("cart")
    object Profile : Routes("profile")
    object PersonalData : Routes("personal_data")
    object Notifications : Routes("notifications")


}

