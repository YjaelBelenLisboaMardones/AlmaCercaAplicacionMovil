package com.example.almacercaapp.model

data class ProductDto(
    val id: Long,             // Coincide con: private Long id;
    val name: String,         // Coincide con: private String name;
    val description: String,  // Coincide con: private String description;
    val price: Double         // Coincide con: private Double price;
)