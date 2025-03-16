package com.jhainusa.arthik_.uiComponents

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.jhainusa.arthik_.BackendFiles.LoanViewModel
import com.jhainusa.arthik_.BottomNavPages.boxIcon

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AddCategoryDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onCategoryAdded: (String,String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var text2 by remember { mutableStateOf("") }
    if (showDialog) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = onDismiss,
            title = { Text("कैटेगरी जोड़ें") },
            text = {
                Column() {
                    TextField(
                        value = text,
                        onValueChange = { text = it },
                        placeholder = { Text("कैटेगरी का नाम दर्ज करें") }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    TextField(
                        value = text2,
                        onValueChange = { text2 = it },
                        placeholder = { Text(
                                "राशि जोड़ें") }
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (text.isNotEmpty()) {
                        onCategoryAdded(text, text2)
                        onDismiss() // Close dialog
                    }
                }) {
                    Text("जोड़ें")
                }
            },
            dismissButton = {
                Button(onClick = onDismiss) {
                    Text("रद्द")
                }
            }
        )

    }


}

@Composable
fun CategoryScreen(viewModel: LoanViewModel, type:String, onTransactionAdded: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }
    Box(modifier = Modifier.clickable {
        showDialog = true }) {
        boxIcon("कैटेगरी जोड़ें")
    }

    AddCategoryDialog(
        showDialog = showDialog,
        onDismiss = { showDialog = false },
        onCategoryAdded = { person,amount->
            viewModel.addTransaction(amount.toDoubleOrNull() ?: 0.0, type, person,); onTransactionAdded() ;
        }
    )
}
