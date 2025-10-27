package com.example.almacercaapp.ui.theme.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight

data class ChatMessage(val text: String, val isUser: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportChatScreen(navController: NavController) {
    var messages by remember {
        mutableStateOf(
            listOf(
                ChatMessage("Hola, Buenos días.", false),
                ChatMessage("Soy Marcelo de Soporte, ¿En qué puedo ayudarte?", false),
                ChatMessage("Hola, estoy teniendo problemas con mi pedido y con el proceso de pago.\n¿Me podrías ayudar?", true),
                ChatMessage("Claro...\n¿Me podrías decir el problema en específico que tienes? Así puedo brindarte ayuda.", false)
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Soporte", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    IconButton(onClick = { /* llamada */ }) {
                        Icon(Icons.Default.Call, contentDescription = "Llamar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9F9F9))
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messages.size) { index ->
                    val msg = messages[index]
                    Row(
                        horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            color = if (msg.isUser) Color(0xFF2E7D32) else Color(0xFFEFEFEF),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.widthIn(max = 260.dp)
                        ) {
                            Text(
                                msg.text,
                                color = if (msg.isUser) Color.White else Color.Black,
                                fontSize = 15.sp,
                                modifier = Modifier.padding(10.dp)
                            )
                        }
                    }
                }
            }

            // Campo de texto de entrada
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Escribe un mensaje...") },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    )
                )
                IconButton(onClick = { /* enviar */ }) {
                    Icon(
                        painter = painterResource(id = com.example.almacercaapp.R.drawable.ic_send),
                        contentDescription = "Enviar",
                        tint = Color(0xFF2E7D32)
                    )
                }
            }
        }
    }
}
