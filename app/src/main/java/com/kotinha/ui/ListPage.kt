package com.kotinha.ui

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.kotinha.MainViewModel
import com.kotinha.model.Ticket
import com.kotinha.repo.Repository

@Composable
fun ListPage(
    viewModel: MainViewModel,
    repository: Repository,
    context: Context,
    navController: NavHostController
) {

    val ticketList = viewModel.tickets

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        items(ticketList) { ticket ->
            TicketItem(ticket = ticket, onClose = {
                repository.remove(ticket)
                Toast.makeText(context, "Ticket removido com sucesso", Toast.LENGTH_SHORT).show()
            }, onClick = {
                navController.navigate("ticket_page/${ticket.id}") {
                    navController.graph.startDestinationRoute?.let {
                        popUpTo(it) { saveState = true }
                    }
                    restoreState = true
                    launchSingleTop = true
                }
            })
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun TicketItem(
    ticket: Ticket,
    onClick: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.size(12.dp))
        Column(modifier = modifier.weight(1f)) {
            Text(
                modifier = Modifier,
                text = ticket.local,
                fontSize = 24.sp
            )
            Text(
                modifier = Modifier,
                text = ticket.dataCompra,
                fontSize = 20.sp
            )
            Text(
                modifier = Modifier,
                text = "R$ " + String.format("%.2f", ticket.valor).replace(".", ","),
                fontSize = 16.sp
            )
        }
        IconButton(onClick = onClose) {
            Icon(Icons.Filled.Close, contentDescription = "Close")
        }
    }
}