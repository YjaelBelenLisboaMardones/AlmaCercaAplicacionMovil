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
import com.example.almacercaapp.ui.theme.screen.* // Importa todas tus pantallas

//Gestiona la navegacion interna entre las 5 pestañas del navigationbar
@Composable
fun HomeNavGraph(
    navController: NavHostController,
    parentNavController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String
) {
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = startDestination// Ckya que recibe la funcion como parametro hacemos que sea flexible el homenavgraph
        //dejandonos que pueda moverse dependiendo del caso
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
            FavoritesScreen(parentNavController = parentNavController)
        }
        composable(route = "profile") {
            ProfileScreen()
        }
    }
}





