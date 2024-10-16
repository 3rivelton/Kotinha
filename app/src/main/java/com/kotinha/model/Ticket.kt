package com.kotinha.model

data class Ticket(
    val id: String? = null,
    var dataCompra: String,
    var local: String,
    var valor: Double,
    val imageUrl: String? = null
)