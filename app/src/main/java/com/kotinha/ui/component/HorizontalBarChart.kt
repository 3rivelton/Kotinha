package com.kotinha.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@SuppressLint("DefaultLocale")
@Composable
fun HorizontalBarChart(
    data: List<Pair<String, Float>>,
    maxBarValue: Float
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        data.forEach { (label, value) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Rótulo
                Text(
                    text = label,
                    modifier = Modifier.width(100.dp)
                )

                // Barra
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(20.dp)
                        .background(Color.LightGray) // Fundo da barra (opcional)
                ) {
                    // Desenhar a barra preenchida de acordo com o valor
                    drawRoundRect(
                        color = Color.Yellow, // Cor da barra
                        size = size.copy(width = (value / maxBarValue) * size.width),
//                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8.dp.toPx())
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Valor numérico ao lado da barra
                Text(
                    text = "R$ " + String.format("%.2f", value).replace(".", ","),
                    modifier = Modifier.width(100.dp)
                )
            }
        }
    }
}