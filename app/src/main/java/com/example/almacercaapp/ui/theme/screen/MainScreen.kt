package com.example.almacercaapp.ui.theme.screen

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

//el parentnavcontroller se utilizará mas adelante para hacer click en una tienda
@Composable
fun MainScreen(parentNavController: NavHostController) {
    // Este controlador es para las 5 pantallas de las pestañas
    val bottomBarNavController = rememberNavController()
    val navBackStackEntry by bottomBarNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "home" // Si es nulo, usa "home" como fallback

    // Scaffold es el "marco" de la ventana. Solo lo defines UNA VEZ.
    Scaffold(
        // 2. Asigna tu componente BottomBar aquí.
        bottomBar = {
            NavigationBar(
                selectedDestination = currentRoute,
                onItemSelected = { route ->// Le pasas la ruta actual
                    // Cuando se toca un ítem, navegas a esa ruta
                    bottomBarNavController.navigate(route) {
                        popUpTo(bottomBarNavController.graph.startDestinationId) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    ) { innerPadding ->
        // 3. Llama al NavGraph de las pestañas, que crearemos a continuación.
        HomeNavGraph(
            navController = bottomBarNavController,
            parentNavController = parentNavController,
            modifier = Modifier.padding(innerPadding) // Aplica el padding para que la BottomBar no tape el contenido
        )
    }
}