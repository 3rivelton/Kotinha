package com.kotinha.db.fb

import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import com.kotinha.model.Ticket
import com.kotinha.model.User

class FBDatabase(private val listener: Listener? = null) {

    private val auth = Firebase.auth
    private val db = Firebase.firestore
    private var ticketsListReg: ListenerRegistration? = null

    interface Listener {
        fun onUserLoaded(user: User)
        fun onTicketAdded(ticket: Ticket)
        fun onTicketRemoved(ticket: Ticket)
    }

    init {
        auth.addAuthStateListener { auth ->

            if (auth.currentUser == null) {
                ticketsListReg?.remove()
                return@addAuthStateListener
            }

            val refCurrUser = db.collection("users")
                .document(auth.currentUser!!.uid)
            refCurrUser.get().addOnSuccessListener {
                it.toObject(FBUser::class.java)?.let { user ->
                    listener?.onUserLoaded(user.toUser())
                }
            }

            ticketsListReg = refCurrUser.collection("tickets")
                .addSnapshotListener { snapshots, ex ->
                    if (ex != null) return@addSnapshotListener

                    snapshots?.documentChanges?.forEach { change ->
                        val fbTicket = change.document.toObject(FBTicket::class.java)
                        if (change.type == DocumentChange.Type.ADDED) {
                            listener?.onTicketAdded(fbTicket.toTicket())
                        } else if (change.type == DocumentChange.Type.REMOVED) {
                            listener?.onTicketRemoved(fbTicket.toTicket())
                        }
                    }
                }
        }
    }

    fun register(user: User) {
        if (auth.currentUser == null)
            throw RuntimeException("User not logged in!")
        val uid = auth.currentUser!!.uid
        db.collection ("users").document(uid + "").set(user.toFBUser());
    }

    fun add(ticket: Ticket) {
        if (auth.currentUser == null)
            throw RuntimeException("User not logged in!")
        val uid = auth.currentUser!!.uid
        val ticketId = ticket.id
        db.collection ("users").document(uid).collection("tickets")
            .document(ticketId).set(ticket.toFBTicket())
    }

    fun remove(ticket: Ticket) {
        if (auth.currentUser == null)
            throw RuntimeException("User not logged in!")
        val uid = auth.currentUser!!.uid
        db.collection ("users").document(uid).collection("tickets")
                .document(ticket.local).delete()
    }
}