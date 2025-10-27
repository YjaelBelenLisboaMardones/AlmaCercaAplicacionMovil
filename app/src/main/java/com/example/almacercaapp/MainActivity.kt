package com.example.almacercaapp

import android.os.Build // Necesario para @RequiresApi
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi // Necesario para @RequiresApi
import androidx.compose.foundation.layout.fillMaxSize

import androidx.compose.material3.Surface // Importa Surface
import androidx.compose.material3.MaterialTheme // Importa MaterialTheme
import androidx.compose.runtime.Composable // Importa Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext // Importa LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel // Importa viewModel
import androidx.navigation.compose.rememberNavController
import com.example.almacercaapp.data.local.database.AppDatabase // Importa tu AppDatabase
import com.example.almacercaapp.data.repository.UserRepository // Importa tu UserRepository
import com.example.almacercaapp.navigation.NavGraph
import com.example.almacercaapp.ui.theme.AlmaCercaAppTheme
import com.example.almacercaapp.viewmodel.AuthViewModel // Importa tu AuthViewModel
import com.example.almacercaapp.viewmodel.AuthViewModelFactory // Importa tu Factory

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O) // Añade si NavGraph lo requiere
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Llama a la función raíz que contendrá la lógica de inicialización
            AppRoot()
        }
    }
}

// Nueva función raíz Composable para organizar la inicialización
@RequiresApi(Build.VERSION_CODES.O) // Añade si NavGraph lo requiere
@Composable
fun AppRoot() {
    // --- Creación de Dependencias (Composition Root) ---
    val context = LocalContext.current.applicationContext // Contexto para la BD
    val db = AppDatabase.getInstance(context)             // Obtiene instancia de la BD Room
    val userDao = db.userDao()                            // Obtiene el DAO desde la BD
    val userRepository = UserRepository(userDao)          // Crea el Repositorio pasándole el DAO

    // Crea el AuthViewModel usando la Factory y pasándole el Repositorio
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(userRepository)
    )


    // Tema de la aplicación
    AlmaCercaAppTheme {
        // Surface actúa como el contenedor principal de la UI
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background // Usa el color de fondo del tema
        ) {
            // Crea el controlador de navegación
            val navController = rememberNavController()
            // Llama al NavGraph principal, pasándole el controlador y el ViewModel ya creado
            NavGraph(
                navController = navController,
                authViewModel = authViewModel
            )
        }
    }
}