package com.kotinha.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kotinha.R
import com.kotinha.model.Ticket
import coil.compose.AsyncImage

@Composable
fun TicketPage(ticket: Ticket) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.teal_700))
    ) {
        Spacer(modifier = Modifier.size(24.dp))
        Text(
            text = "Local: ${ticket.local}",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.Start),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.size(24.dp))
        Text(
            text = "Valor: R$${ticket.valor}",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.Start),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.size(24.dp))
        Text(
            text = "Data: ${ticket.dataCompra}",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.Start),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.size(24.dp))

        AsyncImage(
            model = ticket.imageUrl,
            contentDescription = "Imagem do Ticket",
            placeholder = painterResource(id = R.drawable.loading),
            error = painterResource(id = R.drawable.noimg),
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )
    }
}