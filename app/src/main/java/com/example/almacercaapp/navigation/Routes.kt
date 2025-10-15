package com.example.almacercaapp.navigation

sealed class Routes(val route: String) {
    object Splash : Routes("splash")
    object Onboarding : Routes("onboarding")
    object SignInMethod : Routes("signin_method")
    object SignIn : Routes("signin")
    object SignUp : Routes("signup")
    object Verification : Routes("verification")
    object Location : Routes("location")
}