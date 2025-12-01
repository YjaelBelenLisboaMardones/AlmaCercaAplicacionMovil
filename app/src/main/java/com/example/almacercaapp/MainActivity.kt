package com.example.almacercaapp

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.almacercaapp.data.repository.UserRepository
import com.example.almacercaapp.navigation.NavGraph
import com.example.almacercaapp.network.ApiService
import com.example.almacercaapp.network.RetrofitClient
import com.example.almacercaapp.ui.theme.AlmaCercaAppTheme
import com.example.almacercaapp.viewmodel.AuthViewModel
import com.example.almacercaapp.viewmodel.AuthViewModelFactory

// Definición de DataStore para toda la app
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_session")

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppRoot()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppRoot() {
    // --- CREACIÓN DE DEPENDENCIAS (LA ARQUITECTURA FINAL Y CORRECTA) ---
    val context = LocalContext.current.applicationContext

    // 1. Obtener la instancia de DataStore (nuestra fuente de sesión local)
    val dataStore = context.dataStore

    // 2. Crear una instancia del cliente Retrofit, pasándole el DataStore
    //    para que pueda construir el AuthInterceptor.
    val retrofitClient = RetrofitClient(dataStore)

    // 3. Obtener la instancia de ApiService desde nuestro cliente Retrofit ya configurado.
    val apiService: ApiService = retrofitClient.instance

    // 4. Crear el UserRepository con las dependencias de Red y Sesión.
    val userRepository = UserRepository(apiService, dataStore)

    // 5. Crear el AuthViewModel usando la Factory (esto no cambia).
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(userRepository)
    )

    AlmaCercaAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            // Le pasamos el ViewModel ya creado y listo para usar.
            NavGraph(
                navController = navController,
                authViewModel = authViewModel
            )
        }
    }
}
