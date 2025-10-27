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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.almacercaapp.R
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

// --- Helper para formatear la fecha seleccionada ---
@RequiresApi(Build.VERSION_CODES.O)
fun Long.toFormattedDateString(): String {
    val date = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
    return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDataScreen(navController: NavController) {
    // Campos del formulario
    var nombre by remember { mutableStateOf("Juan Palma") }
    var email by remember { mutableStateOf("Juan.Palma45@example.com") }
    var fechaNacimiento by remember { mutableStateOf("12/07/1990") }
    var genero by remember { mutableStateOf("Masculino") }
    var telefono by remember { mutableStateOf("+56988887777") }

    // Estado para el selector de fecha
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())

    // --- Calendario ---
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                        datePickerState.selectedDateMillis?.let {
                            fechaNacimiento = it.toFormattedDateString()
                        }
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // --- Pantalla principal ---
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Barra superior
        TopAppBar(
            title = { Text("Datos Personales", fontWeight = FontWeight.Bold) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                }
            },
            actions = {
                IconButton(onClick = { /* Abrir configuración */ }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_set),
                        contentDescription = "Configuración"
                    )
                }
            }
        )

        // Contenido del formulario
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

            // Campo de fecha con calendario funcional
            OutlinedTextField(
                value = fechaNacimiento,
                onValueChange = { },
                label = { Text("Fecha de Nacimiento") },
                readOnly = true,
                trailingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_calendar),
                        contentDescription = "Calendario"
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true } // ✅ Ahora abre correctamente el calendario
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Campo de género (simple, no desplegable aún)
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

            // Campo de teléfono
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

            // Botón para guardar
            Button(
                onClick = { /* Guardar cambios */ },
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
