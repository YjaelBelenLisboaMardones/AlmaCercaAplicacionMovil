package com.example.almacercaapp.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
//Importa las pantallas de tus pestañas
import com.example.almacercaapp.ui.theme.screen.CartScreen
import com.example.almacercaapp.ui.theme.screen.ExploreScreen
import com.example.almacercaapp.ui.theme.screen.FavoritesScreen
import com.example.almacercaapp.ui.theme.screen.HomeScreen
import com.example.almacercaapp.ui.theme.screen.ProfileScreen


//Gestiona la navegacion interna entre las 5 pestañas del navigationbar
@Composable
fun HomeNavGraph(
    navController: NavHostController,
    parentNavController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier //Aplica el padding del Scaffold
    ) {

        composable(route = "home") {
            HomeScreen(parentNavController = parentNavController)
        }
        composable(route = "explore") {
            ExploreScreen()
        }
        composable(route = "cart") {
            CartScreen()
        }
        composable(route = "favorites") {
            FavoritesScreen()
        }
        composable(route = "profile") {
            ProfileScreen()
        }
    }
}





