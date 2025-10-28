package com.example.almacercaapp.model

import androidx.lifecycle.ViewModel
import com.example.almacercaapp.model.Product


// Define un ítem dentro del carrito (un producto + su cantidad)
data class CartItem(
    val product: Product,
    var quantity: Int
)

    