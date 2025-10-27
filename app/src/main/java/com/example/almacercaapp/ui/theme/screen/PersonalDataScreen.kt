package com.example.almacercaapp.ui.theme.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.almacercaapp.R
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

// --- Función helper para convertir Millis a String ---
@RequiresApi(Build.VERSION_CODES.O)
fun Long.toFormattedDateString(): String {
    val date = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
    return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDataScreen(navController: NavController) {
    var nombre by remember { mutableStateOf("Juan Palma") }
    var email by remember { mutableStateOf("Juan.Palma45@example") }
    var fechaNacimiento by remember { mutableStateOf("12/07/1990") }
    var genero by remember { mutableStateOf("Masculino") }
    var telefono by remember { mutableStateOf("+56988887777") }

    // --- Estados para el DatePicker ---
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        // Opcional: inicializar con la fecha actual o la guardada
        initialSelectedDateMillis = System.currentTimeMillis()
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                        // Actualizamos la fecha de nacimiento con la seleccionada
                        datePickerState.selectedDateMillis?.let {
                            fechaNacimiento = it.toFormattedDateString()
                        }
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDatePicker = false }
                ) { Text("Cancelar") }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // --- Se eliminó Scaffold y se reemplazó por Column ---
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        TopAppBar(
            title = { Text("Datos Personales", fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                }
            },
            actions = {
                IconButton(onClick = { /* abrir configuración */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_set),
                        contentDescription = "Configuración"
                    )
                }
            }
        )

        // --- Contenido de la pantalla ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre Completo") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo Electrónico") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(10.dp))

            // --- Campo de Fecha de Nacimiento modificado ---
            OutlinedTextField(
                value = fechaNacimiento,
                onValueChange = { /* No hacer nada, es de solo lectura */ },
                label = { Text("Fecha de Nacimiento") },
                readOnly = true, // Lo hacemos de solo lectura
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_calendar),
                        contentDescription = "Calendario"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true } // <-- Abre el diálogo al hacer clic
            )

            Spacer(modifier = Modifier.height(10.dp))

            ExposedDropdownMenuBox(
                expanded = false,
                onExpandedChange = { /* no desplegable funcional aún */ }
            ) {
                OutlinedTextField(
                    value = genero,
                    onValueChange = {},
                    label = { Text("Género") },
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(false) },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.flag_cl),
                    contentDescription = "Chile",
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text("Teléfono") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { /* guardar cambios */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6FCF97)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Guardar Cambios", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}