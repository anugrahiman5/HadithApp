package com.nusa.hadithapp.ui.components.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun JumpToHadithDialog(
    currentNumber: Int,
    totalHadith: Int,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    var inputText by remember { mutableStateOf(currentNumber.toString()) }
    val focusRequester = remember { FocusRequester() }

    val parsedNumber = inputText.toIntOrNull()
    val isValid = parsedNumber != null && (totalHadith <= 0 || (parsedNumber in 1..totalHadith))

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Lompat ke Nomor Hadits",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                val subtitle = if (totalHadith > 0) {
                    "Rentang nomor: 1 – $totalHadith"
                } else {
                    "Masukkan nomor hadits"
                }
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = inputText,
                    onValueChange = { newValue ->
                        if (newValue.all { it.isDigit() } && newValue.length <= 7) {
                            inputText = newValue
                        }
                    },
                    label = { Text("Nomor Hadits") },
                    singleLine = true,
                    isError = inputText.isNotBlank() && !isValid,
                    supportingText = {
                        if (inputText.isNotBlank() && !isValid) {
                            Text(
                                text = if (totalHadith > 0) "Nomor harus antara 1 dan $totalHadith" else "Nomor tidak valid",
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            parsedNumber?.takeIf { isValid }?.let {
                                onConfirm(it)
                            }
                        }
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    shape = RoundedCornerShape(12.dp)
                )

                if (totalHadith > 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        SuggestionChip(
                            onClick = { inputText = "1" },
                            label = { Text("1 (Awal)", fontSize = 11.sp) }
                        )
                        if (totalHadith > 10) {
                            SuggestionChip(
                                onClick = { inputText = (totalHadith / 2).toString() },
                                label = { Text("Tengah (${totalHadith / 2})", fontSize = 11.sp) }
                            )
                        }
                        SuggestionChip(
                            onClick = { inputText = totalHadith.toString() },
                            label = { Text("Akhir ($totalHadith)", fontSize = 11.sp) }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    parsedNumber?.takeIf { isValid }?.let {
                        onConfirm(it)
                    }
                },
                enabled = isValid
            ) {
                Text("Lompat", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}
