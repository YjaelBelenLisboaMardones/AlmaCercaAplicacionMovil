package com.example.almacercaapp.ui.theme.screen

import android.Manifest
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.almacercaapp.viewmodel.ExploreViewModel
import com.google.accompanist.permissions.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import android.provider.Settings
import android.net.Uri        // Para la clase Uri (Uri.fromParts)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ExploreScreen(
    modifier: Modifier = Modifier,
    viewModel: ExploreViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current // <-- Contexto disponible aquí

    // --- Gestión de Permisos ---
    val locationPermissionState = rememberPermissionState(
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    LaunchedEffect(locationPermissionState.status) {
        if (locationPermissionState.status.isGranted) {
            viewModel.startLocationUpdates(context)
        }
    }

    // --- UI Principal ---
    Column(modifier = modifier.fillMaxSize()) {

        // ... (Aquí irían SearchBar y Filtros) ...

        // --- Contenedor del Mapa ---
        Box(modifier = Modifier.weight(1f)) {
            when {
                // Estado 1: Permiso concedido, muestra el mapa
                locationPermissionState.status.isGranted -> {
                    MapContent(
                        userLocation = uiState.userLocation,
                        stores = uiState.stores,
                        cameraPosition = uiState.cameraPosition
                    )
                }

                // Estado 2: Debemos mostrar la explicación (el usuario lo negó, pero no permanentemente)
                locationPermissionState.status.shouldShowRationale -> {
                    PermissionRationale(locationPermissionState)
                }

                // Estado 3: Permiso denegado permanentemente (último caso)
                else -> {
                    PermissionDenied(context) // <-- CONTEXTO PASADO AQUÍ
                }
            } // <-- ¡FIN DEL WHEN!

            // ▼▼▼ ARREGLO ESTRUCTURAL: Condición de carga fuera del when ▼▼▼
            // Indicador de carga mientras se obtiene la ubicación
            if (uiState.isLoadingLocation && locationPermissionState.status.isGranted) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            // ▲▲▲ FIN DEL ARREGLO ESTRUCTURAL ▲▲▲
        }

        // Limpia el listener de ubicación cuando la pantalla se va
        DisposableEffect(Unit) {
            onDispose {
                viewModel.stopLocationUpdates()
            }
        }
    }
}

// --- Composables de Ayuda ---

@Composable
fun MapContent(
    userLocation: LatLng?,
    stores: List<com.example.almacercaapp.model.Store>,
    cameraPosition: LatLng
) {
    // ... (Tu código de MapContent, sin cambios) ...
    // Controla la posición de la cámara del mapa
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(cameraPosition, 15f) // Zoom inicial
    }

    // Actualiza la cámara si la posición del ViewModel cambia (ej. al obtener ubicación)
    LaunchedEffect(cameraPosition) {
        cameraPositionState.animate(
            update = CameraUpdateFactory.newLatLngZoom(cameraPosition, 15f),
            durationMs = 1000
        )
    }

    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState,
        // Habilita el punto azul de "Mi Ubicación" en el mapa
        properties = MapProperties(isMyLocationEnabled = userLocation != null),
        uiSettings = MapUiSettings(myLocationButtonEnabled = true) // Habilita el botón de centrar
    ) {
        // Marcador para la ubicación del usuario (opcional)
        userLocation?.let {
            // Marker(state = MarkerState(position = it), title = "Tú estás aquí")
        }

        // Marcadores para las tiendas
        stores.forEach { store ->
            Marker(
                state = MarkerState(position = LatLng(store.lat, store.lng)),
                title = store.name,
                snippet = store.address // Texto que aparece al tocar el marcador
                // Puedes personalizar el ícono: icon = BitmapDescriptorFactory.fromResource(...)
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionRationale(locationPermissionState: PermissionState) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Necesitamos permiso de ubicación para mostrar tiendas cercanas.")
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { locationPermissionState.launchPermissionRequest() }) {
            Text("Conceder Permiso")
        }
    }
}

@Composable
fun PermissionDenied(
    context: Context // <-- ¡CONTEXTO RECIBIDO AQUÍ!
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Permiso de ubicación denegado.")
        Spacer(modifier = Modifier.height(8.dp))
        Text("Por favor, activa el permiso en los ajustes de la aplicación.")
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                // Navega a la configuración de la aplicación
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(intent)
            }
        ) {
            Text("Abrir Ajustes")
        }
    }
}