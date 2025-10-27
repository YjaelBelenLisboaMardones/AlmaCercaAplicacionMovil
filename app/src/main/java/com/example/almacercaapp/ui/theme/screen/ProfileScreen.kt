package com.example.almacercaapp.ui.theme.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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

@Composable
fun ProfileScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        //Encabezado
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp)
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

            IconButton(onClick = { /* abrir ajustes */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_set),
                    contentDescription = "Configuración"
                )
            }
        }

        Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)

        Spacer(modifier = Modifier.height(4.dp))

        //Opciones del perfil
        ProfileItem("Mis Pedidos", R.drawable.ic_pedidos) {
            // Navegación a pantalla de pedidos
        }
        ProfileItem("Datos Personales", R.drawable.ic_datos) {
            navController.navigate("personal_data")
        }
        ProfileItem("Dirección", R.drawable.ic_direccion) {

        }
        ProfileItem("Métodos de Pago", R.drawable.ic_pago) { }
        ProfileItem("Notificaciones", R.drawable.ic_noti) {
            navController.navigate("notifications")
        }
        ProfileItem("FAQs", R.drawable.ic_faq) {
            navController.navigate("faq")
        }
        ProfileItem("Centro de Ayuda", R.drawable.ic_ayuda) {
            navController.navigate("help_center")
        }

        Spacer(modifier = Modifier.weight(1f))

        //Botón de salir
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { /* acción logout */ }
                .padding(horizontal = 16.dp, vertical = 10.dp)
                .clickable { navController.navigate("logout") }

        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_logout),
                contentDescription = "Salir",
                tint = Color(0xFFE53935),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Salir",
                color = Color(0xFFE53935),
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ProfileItem(text: String, iconRes: Int, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = text,
            tint = Color.Black,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text, fontSize = 15.sp)
    }

    Divider(color = Color(0xFFE0E0E0), thickness = 0.8.dp)
}
