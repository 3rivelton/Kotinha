package com.kotinha

import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.kotinha.model.Ticket
import com.kotinha.model.User
import com.kotinha.repo.Repository
import java.text.SimpleDateFormat
import java.util.Locale

class MainViewModel : ViewModel(), Repository.Listener {

    private val _user = mutableStateOf(User("", ""))
    val user: User
        get() = _user.value

    private var _ticket = mutableStateOf<Ticket?>(null)
    var ticket: Ticket?
        get() = _ticket.value
        set(tmp) {
            _ticket = mutableStateOf(tmp?.copy())
        }

    private var _totalTickets = mutableDoubleStateOf(0.0)
    val totalTickets: Double
        get() = _totalTickets.doubleValue

    private var _loggedIn = mutableStateOf(false)
    val loggedIn: Boolean
        get() = _loggedIn.value

    private val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
        _loggedIn.value = firebaseAuth.currentUser != null
    }

    init {
        listener.onAuthStateChanged(Firebase.auth)
        Firebase.auth.addAuthStateListener(listener)
    }

    private val _tickets = mutableStateMapOf<String, Ticket>()
    val tickets: List<Ticket>
        get() {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            return _tickets.values
                .sortedByDescending { ticket ->
                    try {
                        dateFormat.parse(ticket.dataCompra)
                    } catch (e: Exception) {
                        null
                    }
                }
        }

    override fun onCleared() {
        Firebase.auth.removeAuthStateListener(listener)
    }

    override fun onUserLoaded(user: User) {
        _user.value = user
    }

    private fun somaTotalTickets(): Double {
        return _tickets.values.sumOf { it.valor }
    }

    private fun updateTotalTickets() {
        _totalTickets.doubleValue = somaTotalTickets()
    }

    fun getTicketById(ticketId: String): Ticket? {
        return _tickets[ticketId]
    }

    override fun onTicketAdded(ticket: Ticket) {
        if (!ticket.id.isNullOrBlank()) {
            _tickets[ticket.id.toString()] = ticket
        }
        updateTotalTickets()
    }

    override fun onTicketRemoved(ticket: Ticket) {
        _tickets.remove(ticket.id)
        updateTotalTickets()
    }

    override fun onTicketUpdated(ticket: Ticket) {
        if (!ticket.id.isNullOrBlank()) {
            _tickets[ticket.id.toString()] = ticket.copy()

            if (_ticket.value?.id == ticket.id) {
                _ticket.value = ticket.copy()
            }
        }
        updateTotalTickets()
    }
}