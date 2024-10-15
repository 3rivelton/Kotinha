package com.kotinha.db.fb

import com.kotinha.model.Ticket

class FBTicket {

    var id: String? = null
    var dataCompra: String? = null
    var local: String? = null
    var valor: Double? = null

    fun toTicket(): Ticket {
        return Ticket(
            id = id ?: "",
            dataCompra = dataCompra ?: "",
            local = local ?: "",
            valor = valor ?: 0.00
        )
    }
}

fun Ticket.toFBTicket(): FBTicket {
    val fbTicket = FBTicket()
    fbTicket.id = this.id
    fbTicket.dataCompra = this.dataCompra
    fbTicket.local = this.local
    fbTicket.valor = this.valor
    return fbTicket
}