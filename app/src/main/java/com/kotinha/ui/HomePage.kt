package com.kotinha.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.kotinha.MainViewModel
import com.kotinha.R
import com.kotinha.ui.component.HorizontalBarChart

@Composable
fun HomePage(
    viewModel: MainViewModel
) {
    val ticketList = viewModel.tickets
    val chartData = if (ticketList.isNotEmpty()) {
        viewModel.ticketsPorLocal(ticketList)
    } else {
        emptyList<Pair<String, Float>>()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(id = R.color.teal_700))
            .wrapContentSize(Alignment.Center)
    ) {
        if (chartData.isNotEmpty()) {
            HorizontalBarChart(
                data = chartData,
                maxBarValue = viewModel.totalTickets.toFloat()
            )
        } else {
            Text(
                text = "Nenhum dado disponível",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}