package com.example.almacercaapp.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
// Importa todas las pantallas que usará este gráfico
import com.example.almacercaapp.ui.theme.screen.CartScreen
import com.example.almacercaapp.ui.theme.screen.ExploreScreen
import com.example.almacercaapp.ui.theme.screen.FavoritesScreen
import com.example.almacercaapp.ui.theme.screen.HomeScreen
import com.example.almacercaapp.navigation.Routes
import com.example.almacercaapp.ui.theme.screen.ProfileScreen
import com.example.almacercaapp.ui.theme.screen.FaqScreen
import com.example.almacercaapp.ui.theme.screen.HelpCenterScreen
import com.example.almacercaapp.ui.theme.screen.NotificationsScreen
import com.example.almacercaapp.ui.theme.screen.PersonalDataScreen
import com.example.almacercaapp.ui.theme.screen.SupportChatScreen // Asegúrate que el nombre de tu archivo de chat sea SupportScreen.kt

//Gestiona la navegacion interna entre las 5 pestañas del navigationbar
@RequiresApi(Build.VERSION_CODES.O) // <-- ANOTACIÓN ADJUNTADA A LA FUNCIÓN
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
            CartScreen(parentNavController = parentNavController)
        }
        composable(route = "favorites") {
            FavoritesScreen(parentNavController = parentNavController)        }

        // --- ESTA ES LA CORRECCIÓN ---
        // ProfileScreen necesita AMBOS controladores
        composable(route = "profile") {
            ProfileScreen(
                homeNavController = navController, // El controlador de las pestañas
                parentNavController = parentNavController  // El controlador principal
            )
        }

        // --- CORRECCIÓN DE LAS OTRAS RUTAS ---
        // El resto de las pantallas debe usar el 'navController' de las pestañas
        // para que la barra verde de abajo NO desaparezca.

        composable(Routes.PersonalData.route) {
            PersonalDataScreen(navController = navController)
        }
        composable(Routes.Notifications.route) {
            NotificationsScreen(navController = navController)
        }
        composable(Routes.Faq.route) {
            FaqScreen(navController = navController)
        }
        composable(Routes.HelpCenter.route) {
            HelpCenterScreen(navController = navController)
        }
        composable(Routes.Support.route) { // Usando la ruta 'support' que definimos
            SupportChatScreen(navController = navController) // Usando el archivo SupportScreen.kt
        }
    }
}





