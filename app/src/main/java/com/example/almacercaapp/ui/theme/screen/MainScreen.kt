package com.example.almacercaapp.ui.theme.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.almacercaapp.navigation.HomeNavGraph
import com.example.almacercaapp.ui.theme.component.NavigationBar // Asegúrate de que esta es la importación correcta de tu BottomBar
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.BlendMode

//el parentnavcontroller se utilizará mas adelante para hacer click en una tienda
@Composable
fun MainScreen(
    parentNavController: NavHostController,
    startDestination: String?// Recibe la instrucción de en qué pestaña empezar
) {
    // Este es el NavController para las pestañas INTERNAS (Home, Carrito, etc.)
    val homeNavController = rememberNavController()

    // Determina la ruta de inicio. Si viene una instrucción (ej: "cart_screen"), úsala.
    // Si no, usa la ruta de "home" por defecto.
    val startRoute = startDestination ?: "home"

    // Observamos la pila de navegación del NavController INTERNO para saber en qué pantalla estamos
    val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
    // La ruta actual para iluminar el ícono correcto en la BottomBar.
    // Usamos la ruta del backstack si existe, si no, usamos la ruta de inicio que calculamos.
    val currentRoute = navBackStackEntry?.destination?.route ?: startRoute

    // Scaffold es el "marco" de la ventana. Solo lo defines UNA VEZ.
    Scaffold(
        // 2. Asigna tu componente BottomBar aquí.
        bottomBar = {
            NavigationBar(
                selectedDestination = currentRoute,
                onItemSelected = { route ->// Le pasas la ruta actual
                    // Cuando se toca un ítem, navegas a esa ruta
                    homeNavController.navigate(route) {
                        popUpTo(homeNavController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            // Le decimos al Box que llene todo el tamaño y que
            // SOLO aplique el padding de ABAJO (para la barra de navegación).
            // Ignoramos el padding de ARRIBA (top).
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            HomeNavGraph(
                navController = homeNavController,
                parentNavController = parentNavController,
                startDestination = startRoute
            )
        }
    }
}