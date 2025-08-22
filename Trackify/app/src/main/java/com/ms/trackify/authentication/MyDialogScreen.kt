package com.ms.trackify.authentication

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun MyDialogScreen(navController: NavHostController) {
    AlertDialog(
        onDismissRequest = { navController.popBackStack() },
        title = { Text("Dialog Title") },
        text = { Text("This is a dialog opened via Navigation!") },
        confirmButton = {
            Button(onClick = { navController.popBackStack() }) {
                Text("OK")
            }
        }
    )
}
