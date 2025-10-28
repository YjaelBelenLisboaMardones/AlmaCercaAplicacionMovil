package com.example.almacercaapp.ui.theme.screen

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.almacercaapp.navigation.SellerNavGraph
import kotlinx.coroutines.launch

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import com.example.almacercaapp.ui.theme.component.SellerDrawerContent
import androidx.compose.foundation.layout.padding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerMainScreen(parentNavController: NavHostController) { // Recibe el NavController principal (para logout)
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val sellerNavController = rememberNavController()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // Aquí va el contenido del menú lateral
            SellerDrawerContent(
                sellerNavController = sellerNavController,
                parentNavController = parentNavController, // Para logout
                closeDrawer = { scope.launch { drawerState.close() } }
            )
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Panel Vendedor") }, // O cambia según la pantalla actual
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                        }
                    }

                )
            }
        ) { paddingValues ->

            SellerNavGraph(
                navController = sellerNavController,
                parentNavController = parentNavController,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}