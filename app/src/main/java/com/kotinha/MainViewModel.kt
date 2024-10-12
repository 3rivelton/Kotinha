package com.kotinha

import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.kotinha.model.Ticket
import com.kotinha.model.User
import com.kotinha.repo.Repository

class MainViewModel : ViewModel(), Repository.Listener {

    private val _user = mutableStateOf (User("", ""))
    val user : User
        get() = _user.value

    private var _ticket = mutableStateOf<Ticket?>(null)
    var ticket: Ticket?
        get() = _ticket.value
        set(tmp) { _ticket = mutableStateOf(tmp?.copy()) }

    private var _loggedIn = mutableStateOf(false)
    val loggedIn: Boolean
        get() = _loggedIn.value

    private  val listener = FirebaseAuth.AuthStateListener {
            firebaseAuth ->
        _loggedIn.value = firebaseAuth.currentUser != null
    }
    init {
        listener.onAuthStateChanged(Firebase.auth)
        Firebase . auth . addAuthStateListener (listener)
    }

    private val _tickets = mutableStateMapOf<String, Ticket>()
    val tickets : List<Ticket>
        get() = _tickets.values.toList()

    override fun onCleared() {
        Firebase.auth.removeAuthStateListener(listener)
    }

    override fun onUserLoaded(user: User) {
        _user.value = user
    }

    override fun onTicketAdded(ticket: Ticket) {
        _tickets[ticket.local] = ticket
    }

    override fun onTicketRemoved(ticket: Ticket) {
        _tickets.remove(ticket.local)
    }

    override fun onTicketUpdated(ticket: Ticket) {
        _tickets.remove(ticket.local)
        _tickets[ticket.local] = ticket.copy()

        if (_ticket.value?.local == ticket.local) {
            _ticket.value = ticket.copy()
        }
    }
}