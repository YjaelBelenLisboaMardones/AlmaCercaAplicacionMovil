package com.example.almacercaapp.ui.theme.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaqScreen(navController: NavController) {
    val categories = listOf("General", "Cuenta", "Servicio", "Pagos")
    var selectedCategory by remember { mutableStateOf("General") }

    val faqs = listOf(
        "¿Cómo hacer una compra?" to "Cuando encuentres un producto que quieras comprar, tócala para ver los detalles. Revisa el precio, la descripción y las opciones disponibles. Luego toca el botón “Agregar al carrito”. Sigue las instrucciones para completar la compra.",
        "¿Qué métodos de pago se aceptan?" to "Aceptamos tarjetas de crédito, débito y otros métodos disponibles según tu región.",
        "¿Cómo puedo rastrear mis pedidos?" to "Puedes hacerlo desde la sección 'Mis pedidos' en tu perfil.",
        "¿Puedo cancelar o devolver un pedido?" to "Sí, siempre que el pedido no haya sido enviado aún. Contáctanos para más detalles.",
        "¿Cómo puedo contactar al servicio de atención al cliente?" to "Desde el Centro de Ayuda o por nuestras redes sociales.",
        "¿Cómo puedo crear una cuenta?" to "Ve a la sección de registro e ingresa tus datos personales."
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("FAQs", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color.White)
        ) {
            // Tabs
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { cat ->
                    val isSelected = cat == selectedCategory
                    Button(
                        onClick = { selectedCategory = cat },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) Color(0xFF2E7D32) else Color(0xFFF5F5F5),
                            contentColor = if (isSelected) Color.White else Color.Black
                        ),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 6.dp),
                        elevation = null
                    ) {
                        Text(cat, fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // FAQ list
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 12.dp)
            ) {
                faqs.forEach { (q, a) ->
                    var expanded by remember { mutableStateOf(false) }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        TextButton(
                            onClick = { expanded = !expanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(q, color = Color.Black, fontWeight = FontWeight.Medium)
                        }

                        if (expanded) {
                            Text(
                                text = a,
                                fontSize = 14.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                            )
                        }

                        Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
                    }
                }
            }
        }
    }
}
