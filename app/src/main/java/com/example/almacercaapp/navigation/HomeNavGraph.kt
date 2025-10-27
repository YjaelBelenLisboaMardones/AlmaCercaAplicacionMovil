package com.example.almacercaapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.almacercaapp.ui.theme.screen.*

@Composable
fun HomeNavGraph(
    navController: NavHostController,
    parentNavController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
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
            ProfileScreen(navController = parentNavController)
        }
        composable(route = "faq") {
            FaqScreen(navController = parentNavController)
        }
        composable(route = "help_center") {
            HelpCenterScreen(navController = parentNavController)
        }
        composable(route = "support_chat") {
            SupportChatScreen(navController = parentNavController)
        }
    }
}
