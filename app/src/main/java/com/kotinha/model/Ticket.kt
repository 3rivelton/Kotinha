package com.kotinha.model

import java.util.UUID

data class Ticket(
    val id: String = UUID.randomUUID().toString(),
    var dataCompra: String,
    var local: String,
    var valor: String,
)