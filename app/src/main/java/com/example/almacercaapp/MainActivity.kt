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
import com.example.almacercaapp.data.repository.ProductRepository
import com.example.almacercaapp.data.repository.UserRepository
import com.example.almacercaapp.navigation.NavGraph
import com.example.almacercaapp.network.ApiService
import com.example.almacercaapp.network.RetrofitClient
import com.example.almacercaapp.ui.theme.AlmaCercaAppTheme
import com.example.almacercaapp.viewmodel.AdminViewModel
import com.example.almacercaapp.viewmodel.AdminViewModelFactory
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
    // --- RAÍZ DE DEPENDENCIAS (MANUAL) ---
    val context = LocalContext.current.applicationContext
    val dataStore = context.dataStore
    val retrofitClient = RetrofitClient(dataStore)
    val apiService: ApiService = retrofitClient.instance

    // --- CREACIÓN DE REPOSITORIOS ---
    val userRepository = UserRepository(apiService, dataStore)
    val productRepository = ProductRepository(apiService)

    // --- CREACIÓN DE VIEWMODELS ---
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(userRepository)
    )
    val adminViewModel: AdminViewModel = viewModel(
        factory = AdminViewModelFactory(productRepository)
    )

    AlmaCercaAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController = rememberNavController()
            // Le pasamos TODOS los ViewModels al NavGraph para que los distribuya
            NavGraph(
                navController = navController,
                authViewModel = authViewModel,
                adminViewModel = adminViewModel
            )
        }
    }
}
