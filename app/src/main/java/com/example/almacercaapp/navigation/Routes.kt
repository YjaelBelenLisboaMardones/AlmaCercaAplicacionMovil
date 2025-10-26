package com.example.almacercaapp.navigation

import androidx.compose.material3.Text
//archivo que se asegura que las rutas esten bien escritas
sealed class Route(val route: String) {
    object Splash : Route("splash")
    object Onboarding : Route("onboarding")
    object SignUp : Route("signup")
    object Verification : Route("verification")
    object SignIn : Route("signin")
    object SignInMethod : Route("signin_method")
    object Home : Route("home")
    object Explore : Route("explore")
    object Cart : Route("cart")
    object Profile : Route("profile")

}

