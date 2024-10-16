package com.kotinha.db.fb

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import com.kotinha.model.Ticket
import com.kotinha.model.User

class FBDatabase(
    private val listener: Listener? = null
) {
    private val auth = Firebase.auth
    private val db = Firebase.firestore
    private val storage = Firebase.storage
    private val storageRef: StorageReference = storage.reference
    private var ticketsListReg: ListenerRegistration? = null

    interface Listener {
        fun onUserLoaded(user: User)
        fun onTicketAdded(ticket: Ticket)
        fun onTicketUpdated(ticket: Ticket)
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
        db.collection("users").document(uid + "").set(user.toFBUser())
    }

    fun add(ticket: Ticket) {
        if (auth.currentUser == null)
            throw RuntimeException("User not logged in!")

        val uid = auth.currentUser!!.uid

        db.collection("users").document(uid).collection("tickets")
            .add(ticket.toFBTicket())
            .addOnSuccessListener { documentReference ->

                val generatedId = documentReference.id

                val ticketId = ticket.copy(id = generatedId)

                db.collection("users").document(uid).collection("tickets")
                    .document(generatedId).set(ticketId)

                listener?.onTicketUpdated(ticketId)
            }
    }

    fun remove(ticket: Ticket) {
        if (auth.currentUser == null)
            throw RuntimeException("User not logged in!")
        val uid = auth.currentUser!!.uid
        db.collection("users").document(uid).collection("tickets")
            .document(ticket.id.toString()).delete()
    }

    fun uploadImage(fileUri: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {

        val imagesRef: StorageReference = storageRef.child("images/${fileUri.lastPathSegment}")

        val uploadTask = imagesRef.putFile(fileUri)
        uploadTask.addOnSuccessListener {
            imagesRef.downloadUrl.addOnSuccessListener { uri ->
                onSuccess(uri.toString())
            }
        }.addOnFailureListener {
            onFailure(it)
        }
    }

    fun removeImage(fileUri: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit){
        val imageRef = FirebaseStorage.getInstance().getReferenceFromUrl(fileUri)

        imageRef.delete().addOnSuccessListener {
            onSuccess()
        }.addOnFailureListener { exception ->
            onFailure(exception)
        }
    }
}