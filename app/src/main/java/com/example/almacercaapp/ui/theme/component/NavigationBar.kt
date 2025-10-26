package com.example.almacercaapp.ui.theme.component


import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.almacercaapp.R


@Composable
fun NavigationBar(
    selectedDestination: String,
    onItemSelected: (String) -> Unit
) {
    NavigationBar(
        modifier = Modifier,
        containerColor = Color(0xFF2E7D32), // Verde AlmaCerca (#2E7D32)
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = selectedDestination == "home",
            onClick = { onItemSelected("home") }, //rutas
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_tiendas_inicio),//icono
                    contentDescription = "Tiendas", // descripcion del texto
                    tint = if (selectedDestination == "home") Color.White else Color.Black
                        )//cuando se seleccione que resalte
                   },
            label = {
                Text(
                    "Tiendas",
                    color = if (selectedDestination == "home") Color.White else Color.Black//cuando se seleccione que resalte el texto
                )
            },
            alwaysShowLabel = true//siempre muestra el texto
        )
        NavigationBarItem(
            selected = selectedDestination == "explore",
            onClick = { onItemSelected("explore") },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_tiendas_inicio),
                    contentDescription = "Explorar",
                    tint = if (selectedDestination == "explore") Color.White else Color.Black
                )
            },
            label = {
                Text(
                    "Explorar",
                    color = if (selectedDestination == "explore") Color.White else Color.Black
                )
            },
            alwaysShowLabel = true
        )
        NavigationBarItem(
            selected = selectedDestination == "cart",
            onClick = { onItemSelected("cart") },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_tiendas_inicio),
                    contentDescription = "Carrito",
                    tint = if (selectedDestination == "cart") Color.White else Color.Black
                )
            },
            label = {
                Text(
                    "Carrito",
                    color = if (selectedDestination == "cart") Color.White else Color.Black
                )
            },
            alwaysShowLabel = true
        )
        NavigationBarItem(
            selected = selectedDestination == "favorites",
            onClick = { onItemSelected("favorites") },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_tiendas_inicio),
                    contentDescription = "Favoritos",
                    tint = if (selectedDestination == "favorites") Color.White else Color.Black
                )
            },
            label = {
                Text(
                    "Favoritos",
                    color = if (selectedDestination == "favorites") Color.White else Color.Black
                )
            },
            alwaysShowLabel = true
        )
        NavigationBarItem(
            selected = selectedDestination == "profile",
            onClick = { onItemSelected("profile") },
            icon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_tiendas_inicio),
                    contentDescription = "Cuenta",
                    tint = if (selectedDestination == "profile") Color.White else Color.Black
                )
            },
            label = {
                Text(
                    "Cuenta",
                    color = if (selectedDestination == "profile") Color.White else Color.Black
                )
            },
            alwaysShowLabel = true
        )
    }
}