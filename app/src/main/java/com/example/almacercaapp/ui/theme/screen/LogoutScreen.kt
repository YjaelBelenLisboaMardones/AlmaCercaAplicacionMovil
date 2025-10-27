package com.example.almacercaapp.ui.theme.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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

@Composable
fun LogoutScreen(navController: NavController, onConfirmLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
            }

            Text(
                text = "Mi Perfil",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.width(48.dp)) // balancear el espacio
        }

        Spacer(modifier = Modifier.height(100.dp))

        Icon(
            painter = painterResource(id = R.drawable.ic_warning),
            contentDescription = "Advertencia",
            tint = Color(0xFFE53935),
            modifier = Modifier.size(70.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Texto principal
        Text(
            text = "¿Cerrar Sesión?",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = Color(0xFFE53935),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Estas seguro que vas a salir de tu cuenta",
            fontSize = 15.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Botones
        Button(
            onClick = { onConfirmLogout() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE53935)),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Sí, cerrar sesión", color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("No, cancelar")
        }
    }
}
