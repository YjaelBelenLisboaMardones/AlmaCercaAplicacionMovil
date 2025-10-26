package com.example.almacercaapp.ui.theme.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryProductsScreen(
    storeId: String?,
    categoryName: String?,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(text = categoryName ?: "Productos", fontWeight = FontWeight.Bold)
                        // En el futuro, aquí podrías poner el nombre de la tienda
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver atrás"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        // Aquí irá la cuadrícula de productos en el futuro
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            Text(text = "Mostrando productos de la tienda ID: $storeId en la categoría: $categoryName")
        }
    }
}
    