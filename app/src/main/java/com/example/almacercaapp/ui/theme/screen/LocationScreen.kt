package com.example.almacercaapp.ui.theme.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.almacercaapp.R
import com.example.almacercaapp.ui.theme.component.PrimaryButton
import com.example.almacercaapp.navigation.Routes
import com.example.almacercaapp.viewmodel.AuthViewModel
import androidx.lifecycle.viewmodel.compose.viewModel




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    var expandedCountry by remember { mutableStateOf(false) }
    var expandedArea by remember { mutableStateOf(false) }

    val countries = listOf("Chile", "Argentina", "Perú", "México")
    val areas = listOf("Santiago Centro", "Providencia", "Las Condes", "Ñuñoa")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            //  Botón para volver
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.Start)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🗺️ Imagen de ubicación
            Image(
                painter = painterResource(id = R.drawable.location_icon),
                contentDescription = "Ubicación",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Selecciona tu ubicación",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Activa tu ubicación o selecciona tu zona para ver productos cercanos.",
                fontSize = 15.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 🌎 Selección de país
            ExposedDropdownMenuBox(
                expanded = expandedCountry,
                onExpandedChange = { expandedCountry = !expandedCountry }
            ) {
                OutlinedTextField(
                    value = viewModel.selectedCountry.value,
                    onValueChange = {},
                    label = { Text("País") },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCountry) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedCountry,
                    onDismissRequest = { expandedCountry = false }
                ) {
                    countries.forEach { country ->
                        DropdownMenuItem(
                            text = { Text(country) },
                            onClick = {
                                viewModel.updateCountry(country)
                                expandedCountry = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Selección de área
            ExposedDropdownMenuBox(
                expanded = expandedArea,
                onExpandedChange = { expandedArea = !expandedArea }
            ) {
                OutlinedTextField(
                    value = viewModel.selectedArea.value,
                    onValueChange = {},
                    label = { Text("Zona o comuna") },
                    placeholder = { Text("Selecciona tu área") },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedArea) },
                    modifier = Modifier.fillMaxWidth().menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = expandedArea,
                    onDismissRequest = { expandedArea = false }
                ) {
                    areas.forEach { area ->
                        DropdownMenuItem(
                            text = { Text(area) },
                            onClick = {
                                viewModel.updateArea(area)
                                expandedArea = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Botón para continuar
            PrimaryButton(
                text = "Confirmar ubicación",
                onClick = {
                    if (viewModel.selectedCountry.value.isNotEmpty() &&
                        viewModel.selectedArea.value.isNotEmpty()
                    ) {
                        // Guarda la ubicación y pasa a Home
                        viewModel.updateLocation("${viewModel.selectedArea.value}, ${viewModel.selectedCountry.value}")
                        navController.navigate("home") {
                            popUpTo("location") { inclusive = true }
                        }
                    }
                }
            )
        }
    }
}