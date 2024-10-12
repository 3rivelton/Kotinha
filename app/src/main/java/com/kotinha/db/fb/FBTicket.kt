package com.kotinha.db.fb

import com.kotinha.model.Ticket
import java.util.UUID

class FBTicket {

    var id: String? = null
    var dataCompra: String? = null
    var local: String? = null
    var valor: String? = null

    fun toTicket(): Ticket {
        return Ticket(
            id = id ?: UUID.randomUUID().toString(),
            dataCompra = dataCompra ?: "",
            local = local ?: "",
            valor = valor ?: ""
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