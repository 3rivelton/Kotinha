package com.kotinha.ui

import android.app.DatePickerDialog
import java.text.SimpleDateFormat
import java.util.Locale
import android.icu.util.Calendar
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.kotinha.model.Ticket

@Composable
fun TicketDialog(
    onDismiss: () -> Unit,
    onConfirm: (ticket: Ticket, imageUri: Uri?) -> Unit
) {
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
    val calendar = remember { Calendar.getInstance() }
    val dataCompra = remember { mutableStateOf(dateFormat.format(calendar.time)) }
    val local = remember { mutableStateOf("") }
    val valor = remember { mutableStateOf("") }
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? -> imageUri.value = uri }
    )
    val context = LocalContext.current

    val openDatePicker = {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                dataCompra.value = dateFormat.format(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Dialog(onDismissRequest = { onDismiss() }) {
        Surface(shape = RoundedCornerShape(16.dp)) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Adicionar Compra:")
                    Icon(imageVector = Icons.Filled.Close,
                        contentDescription = "",
                        modifier = Modifier.clickable { onDismiss() })
                }
                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { openDatePicker() },
                    label = { Text(text = "Data da Compra") },
                    value = dataCompra.value,
                    onValueChange = { },
                    enabled = false
                )
                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Local da Compra") },
                    value = local.value,
                    onValueChange = { local.value = it })
                Spacer(modifier = Modifier.height(20.dp))

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Valor da Compra") },
                    value = valor.value,
                    onValueChange = { valor.value = it.replace(",", ".") })
                Spacer(modifier = Modifier.height(20.dp))

                Button(onClick = { launcher.launch("image/*") }) {
                    Text(text = "Selecionar Imagem")
                }
                Spacer(modifier = Modifier.height(10.dp))
                imageUri.value?.let { uri ->
                    Text(text = "Imagem selecionada $uri")
                }
                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        val ticket = Ticket(
                            id = "",
                            dataCompra = dataCompra.value,
                            local = local.value,
                            valor= valor.value.toDoubleOrNull() ?: 0.00)
                        onConfirm(ticket, imageUri.value)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(text = "OK")
                }
            }
        }
    }
}