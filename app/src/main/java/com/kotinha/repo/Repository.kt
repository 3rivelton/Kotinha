package com.kotinha.repo

import android.net.Uri
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

    fun addTicket(ticket: Ticket, fileUri: Uri?) {
        if (fileUri != null) {
            uploadImage(fileUri, { imageUrl ->
                val updatedTicket = ticket.copy(imageUrl = imageUrl)
                fbDb.add(updatedTicket)
            }, {
                it.printStackTrace()
            })
        } else {
            fbDb.add(ticket)
        }
    }

    fun remove(ticket: Ticket) {
        if (!ticket.imageUrl.isNullOrEmpty()) {
            removeImage(ticket.imageUrl, {
                fbDb.remove(ticket)
            }, {
                it.printStackTrace()
            })
        } else {
            fbDb.remove(ticket)
        }
    }

    private fun uploadImage(fileUri: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit){
        fbDb.uploadImage(fileUri, onSuccess, onFailure)
    }

    private fun removeImage(fileUri: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit){
        fbDb.removeImage(fileUri, onSuccess, onFailure)
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