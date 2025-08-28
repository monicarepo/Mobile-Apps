package com.ms.countryapp.dialogs



import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ms.countryapp.R

@Composable
fun MyAlertDialog(
    showDialog: MutableState<Boolean>,
    title: String,
    message: String,
    currentCapital: String,
    positiveAction: (String) -> Unit
) {
    var newCapital by remember { mutableStateOf("") }

    if(showDialog.value) {
        AlertDialog(
            onDismissRequest = {
                showDialog.value = false
            },
            title = {
                Text(text = title, fontSize = 30.sp)
            },
            text = {
                Column {
                    Text(text = message, fontSize = 20.sp)
                    Spacer(Modifier.height(5.dp))
                    Text(text = "${stringResource(R.string.current_country_capital)}: $currentCapital", fontSize = 15.sp)
                    Spacer(Modifier.height(5.dp))
                    OutlinedTextField(
                        value = newCapital,
                        onValueChange = {newCapital = it},
                        label = { Text(stringResource(R.string.new_capital)) }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    positiveAction(newCapital)
                    showDialog.value = false
                }) {
                    Text(stringResource(R.string.confirm))
                }
            },
            dismissButton = {
                Button(onClick = {
                    showDialog.value = false
                }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

}
