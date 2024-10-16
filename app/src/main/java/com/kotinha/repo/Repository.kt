package com.kotinha.repo

import com.kotinha.db.fb.FBDatabase
import com.kotinha.model.Ticket

import com.kotinha.model.User

class Repository(private var listener: Listener) : FBDatabase.Listener {
    private var fbDb = FBDatabase(this)

    interface Listener {
        fun onUserLoaded(user: User)
        fun onTicketAdded(ticket: Ticket)
        fun onTicketRemoved(ticket: Ticket)
        fun onTicketUpdated(ticket: Ticket)
    }

    fun remove(ticket: Ticket) {
        fbDb.remove(ticket)
    }

    fun register(userName: String, email: String) {
        fbDb.register(User(userName, email))
    }

    fun addTicket(ticket: Ticket) {
        fbDb.add(ticket)
    }

    override fun onUserLoaded(user: User) {
        listener.onUserLoaded(user)
    }

    override fun onTicketAdded(ticket: Ticket) {
        listener.onTicketAdded(ticket)
    }

    override fun onTicketUpdated(ticket: Ticket) {
        listener.onTicketUpdated(ticket)
    }

    override fun onTicketRemoved(ticket: Ticket) {
        listener.onTicketRemoved(ticket)
    }
}