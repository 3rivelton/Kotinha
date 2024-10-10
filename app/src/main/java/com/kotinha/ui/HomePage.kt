package com.kotinha.ui

import android.app.Activity
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.kotinha.ui.theme.KotinhaTheme

@Composable
fun HomePage(modifier: Modifier = Modifier) {
    KotinhaTheme {

        val activity = LocalContext.current as? Activity

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Button(
                onClick = {
                    Toast.makeText(activity, "Saindo", Toast.LENGTH_LONG).show()
                    activity?.finish()
                }
            )
            {
                Text("Sair")
            }
        }

    }
}