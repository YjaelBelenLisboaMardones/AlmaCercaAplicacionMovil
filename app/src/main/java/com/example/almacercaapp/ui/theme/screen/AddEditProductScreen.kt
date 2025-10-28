package com.example.almacercaapp.ui.theme.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack // Importa el icono correcto
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.almacercaapp.ui.theme.GreenPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProductScreen(
    navController: NavHostController,
    productId: Long? // Recibe el ID del producto (null si es para añadir)
) {
    // Estados para los campos del formulario
    var productName by remember { mutableStateOf("") }
    var productDescription by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }
    // TODO: Añadir estado para imagen

    // Determina si estamos editando o añadiendo por el productId
    val isEditing = productId != null
    val title = if (isEditing) "Editar Producto" else "Agregar Producto"

    // TODO: Si isEditing es true, cargar los datos del producto usando el productId

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) { // Botón para volver atrás
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()) // Permite scroll
        ) {

            OutlinedTextField(
                value = productName,
                onValueChange = { productName = it },
                label = { Text("Nombre del Producto") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(16.dp))


            OutlinedTextField(
                value = productDescription,
                onValueChange = { productDescription = it },
                label = { Text("Descripción (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3 // Permite varias líneas
            )
            Spacer(modifier = Modifier.height(16.dp))


            OutlinedTextField(
                value = productPrice,
                onValueChange = { newValue ->

                    if (newValue.all { it.isDigit() || it == '.' } && newValue.count { it == '.' } <= 1) {
                        productPrice = newValue
                    }
                },
                label = { Text("Precio") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), // Teclado numérico decimal
                leadingIcon = { Text("$") } // Símbolo de peso
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Imagen
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(vertical = 8.dp)
                    .align(Alignment.CenterHorizontally) // Centra la caja
            ) {
                Text("TODO: Añadir selector de imagen aquí", modifier=Modifier.align(Alignment.Center))
            }
            Spacer(modifier = Modifier.height(32.dp))


            Button(
                onClick = {
                    // TODO: Validar campos y llamar al ViewModel para guardar/actualizar
                    // Después de guardar, volver a la pantalla anterior:
                    // navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            ) {
                Text(if (isEditing) "Guardar Cambios" else "Agregar Producto", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}