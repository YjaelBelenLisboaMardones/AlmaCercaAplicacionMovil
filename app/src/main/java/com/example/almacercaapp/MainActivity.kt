package com.example.almacercaapp

import android.os.Build // Necesario para @RequiresApi
import android.os.Bundle
import android.util.Log
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
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel // Importa viewModel
import androidx.navigation.compose.rememberNavController
import com.example.almacercaapp.data.local.database.AppDatabase // Importa tu AppDatabase
import com.example.almacercaapp.data.repository.UserRepository // Importa tu UserRepository
import com.example.almacercaapp.navigation.NavGraph
import com.example.almacercaapp.network.RetrofitClient
import com.example.almacercaapp.ui.theme.AlmaCercaAppTheme
import com.example.almacercaapp.viewmodel.AuthViewModel // Importa tu AuthViewModel
import com.example.almacercaapp.viewmodel.AuthViewModelFactory // Importa tu Factory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O) // Añade si NavGraph lo requiere
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Llama a la función raíz que contendrá la lógica de inicialización
            AppRoot()
        }
        lifecycleScope.launch {
            Log.d("PRUEBA_API", "--------------------------------------------------")
            Log.d("PRUEBA_API", "🚀 INICIANDO PRUEBA DE CONEXIÓN A RAILWAY...")

            try {
                // Probamos obtener el carrito del usuario ID 1
                val respuesta = RetrofitClient.instance.obtenerCarrito(userId = "1")

                if (respuesta.isSuccessful) {
                    val listaProductos = respuesta.body()
                    Log.d("PRUEBA_API", "✅ ¡ÉXITO! Conexión establecida.")
                    Log.d("PRUEBA_API", "📦 Datos recibidos: $listaProductos")

                    if (listaProductos.isNullOrEmpty()) {
                        Log.d("PRUEBA_API", "ℹ️ El carrito está vacío (Corchetes []), pero funciona.")
                    }
                } else {
                    Log.e("PRUEBA_API", "❌ ERROR DEL SERVIDOR: Código ${respuesta.code()}")
                }
            } catch (e: Exception) {
                Log.e("PRUEBA_API", "❌ ERROR DE CONEXIÓN: ${e.message}")
                Log.e("PRUEBA_API", "💡 Pista: Revisa tu internet o el permiso en AndroidManifest.")
            }
            Log.d("PRUEBA_API", "--------------------------------------------------")
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